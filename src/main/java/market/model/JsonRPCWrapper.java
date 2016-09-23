package market.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by PerevalovaMA on 21.09.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonRPCWrapper<ResultType> {
    private String jsonrpc = "2.0";
    private String method;
    private String id;
    private Object params;
    private ResultType result;

    public JsonRPCWrapper() {
    }

    public JsonRPCWrapper(String method, Object params) {
        id = Integer.toString(new Random().nextInt(Integer.SIZE - 1));
        this.method = method;
        this.params = params;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public String getId() {
        return id;
    }

    public Object getParams() {
        return params;
    }

    public ResultType getResult() {
        return result;
    }
}
