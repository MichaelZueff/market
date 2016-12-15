package market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

abstract class AuthServlet extends HttpServlet {
    protected static final Logger LOG = LoggerFactory.getLogger(AuthServlet.class);
    private Set<String> authKeys = new HashSet<>();

    public AuthServlet() {
        updateAuth();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (auth(req)) {
            LOG.info(req.getRequestURI() + " start");
            initDataPost(req, resp);
            LOG.info(req.getRequestURI() + " end");
        } else {
            resp.sendError(403);
        }
    }

    abstract void initDataPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    private boolean auth(HttpServletRequest req) throws IOException, ServletException {
        return req.getParameter("auth-token") != null && authKeys.contains(req.getParameter("auth-token"));
    }

    protected void updateAuth() {
        for (int i = 0; i <= 5; i++) {
            try {
                authKeys = DbDao.getAuthKeys();
            } catch (SQLException e) {
                LOG.error("Failed to get auth keys", e);
            }
            if (authKeys.size() > 0) {
                LOG.info("Auth keys updated, size " + authKeys.size());
                break;
            }
        }
    }

    protected String getRequestBody(HttpServletRequest req) {
        try {
            // Read from request
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (Exception e) {
            LOG.error("Failed to read request body", e);
        }
        return null;
    }

    protected void writeResponse(HttpServletResponse resp, Response response) throws IOException {
        if (response.getCode() == 200) {
            writeResponseBody(resp, response.getBody());
        } else {
            resp.sendError(response.getCode(), response.getBody());
        }
    }

    private void writeResponseBody(HttpServletResponse resp, String data) throws IOException {
        if (data != null) {
            resp.setContentType("application/xml; charset=UTF-8");
            resp.getWriter().write(data);
        }
    }
}
