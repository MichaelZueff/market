package market;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import market.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by PerevalovaMA on 26.08.2016.
 */
@WebServlet("/cart")
public class CartServlet extends AuthServlet {
    private static final String REGION_SEARCHED_LEVEL = "SUBJECT_FEDERATION";
    private OracleDao oracleDao = new OracleDao();
    private MysqlDao mysqlDao = new MysqlDao();

    void initDataPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ClientCart cart = parseJson(req.getInputStream());
        Integer regionId = getRegionId(cart.getDelivery().getRegion());

        if (regionId == null) {
            //TODO: response with delivery = false?
        } else {
            List<Item> items = cart.getItems();
            String itemIds = items.stream().map(Item::getOfferId).collect(Collectors.joining(", "));
            try {
                setRemains(items, mysqlDao.getRemains(itemIds, regionId));
                setPrices(items, mysqlDao.getPrices(itemIds, regionId));

                CartDelivery cartDelivery = getCartDelivery(items, regionId);
                setItemsDelivery(items, cartDelivery.getItems());

                CartResponse cartResponse = new CartResponse(getItems(items), getPaymentMethods(cartDelivery), getDeliveryOptions(cartDelivery));
                String response = makeJson(cartResponse);
                LOG.debug("/cart: " + response);
                writeResponseBody(resp, response);
            } catch (UnirestException e) {
                LOG.error("/cart: site cart request failed", e);
                resp.sendError(500);
            } catch (SQLException e) {
                LOG.error("/cart: database exception", e);
                resp.sendError(500);
            }
        }
    }

    private ClientCart parseJson(InputStream reqStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        ClientCart cart = mapper.readValue(reqStream, ClientCart.class);
        LOG.debug("/cart: json parsed");

        return cart;
    }

    private String makeJson(CartResponse cart) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        return mapper.writeValueAsString(cart);
    }

    private void setOutlets(DeliveryOptions delivery) throws SQLException {
        delivery.setOutlets(oracleDao.getOutlets(delivery.getStores()));
        LOG.debug("/cart: outlets set");
    }

    private void setItemsDelivery(List<Item> items, Map<String, ItemDelivery> itemsDelivery) {
        for (Item item : items) {
            ItemDelivery itemDelivery = itemsDelivery.get(item.getOfferId());
            item.setDelivery(itemDelivery.isCourier() || itemDelivery.isPickup());
        }
    }

    private void setPrices(List<Item> items, Map<String, Integer> prices) throws SQLException {
        items.stream().forEach(item -> item.setPrice(prices.get(item.getOfferId())));
        LOG.debug("/cart: prices set");
    }

    private void setRemains(List<Item> items, Map<String, List<Remains>> remains) throws SQLException {
        items.stream().forEach(item -> item.setRemains(remains.get(item.getOfferId())));
        LOG.debug("/cart: remains set");
    }

    //returns item information
    private List<ItemResponse> getItems(List<Item> items) {
        return items.stream().map(Item::getItemResponse).collect(Collectors.toList());
    }

    //returns list of available payment methods
    private List<String> getPaymentMethods(CartDelivery cartDelivery) {
        String cashPayment = "cash";
        String cardPayment = "cardOnDelivery";

        HashSet<String> paymentMethodsCourier = cartDelivery.getCourier().getPayments();
        HashSet<String> paymentMethodsPickup = cartDelivery.getPickup().getPayments();

        boolean isCash = paymentMethodsCourier.contains(cashPayment) && paymentMethodsPickup.contains(cashPayment);
        boolean isCard = paymentMethodsCourier.contains(cardPayment) && paymentMethodsPickup.contains(cardPayment);

        List<String> paymentMethods = new ArrayList<>();
        if (isCash) {
            paymentMethods.add("CASH_ON_DELIVERY");
        }
        if (isCard) {
            paymentMethods.add("CARD_ON_DELIVERY");
        }

        return paymentMethods;
    }

    //returns available delivery options
    private List<DeliveryOptionsResponse> getDeliveryOptions(CartDelivery cartDelivery) throws SQLException {
        DeliveryOptions courier = cartDelivery.getCourier();
        DeliveryOptions pickup = cartDelivery.getPickup();
        setOutlets(pickup);

        List<DeliveryOptionsResponse> deliveryOptions = new ArrayList<>();
        deliveryOptions.add(courier.getDeliveryOptionsResponse("DELIVERY", "Собственная служба доставки"));
        deliveryOptions.add(pickup.getDeliveryOptionsResponse("PICKUP", "Самовывоз"));

        return deliveryOptions;
    }

    //returns oracle region id, null if not found
    private Integer getRegionId(Region region) {
        Integer regionId = null;

        if (region != null) {
            if (region.getType().equals(REGION_SEARCHED_LEVEL)) {
                try {
                    regionId = oracleDao.getRegionId(region.getId());
                } catch (SQLException e) {
                    LOG.error("/cart: couldn't select from regions", e);
                }
            } else {
                return getRegionId(region.getParent());
            }

            if (regionId == null) {
                throw new DataException("Region " + region.getName() + " was not found");
            }
        } else {
            LOG.error("/cart: Region with searched level " + REGION_SEARCHED_LEVEL + " was not found in input request");
        }

        LOG.debug("/cart: region id found");
        return regionId;
    }

    //returns cart delivery info using site API
    private CartDelivery getCartDelivery(List<Item> items, Integer regionId) throws IOException, UnirestException {
        Map itemIds = items.stream().collect(Collectors.toMap(Item::getOfferId, Item::getOrderCount));
        JsonRPCWrapper jsonReq = new JsonRPCWrapper("cart", Arrays.asList(itemIds, regionId));
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse response = Unirest.post("http://www.tehnosila.ru/api/item/json").body(mapper.writeValueAsString(jsonReq)).asBinary();
        JsonRPCWrapper<CartDelivery> jsonResp = mapper.readValue(response.getRawBody(), new TypeReference<JsonRPCWrapper<CartDelivery>>() {
        });

        LOG.debug("/cart: delivery info from site obtained");
        return jsonResp.getResult();
    }
}
