package market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

abstract class AuthServlet extends HttpServlet {
    private final static String TOKEN = "710000015653998B";
    protected static final Logger LOG = LoggerFactory.getLogger(AuthServlet.class);
    protected DbDao db = new DbDao();

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
        return req.getParameter("auth-token") != null && req.getParameter("auth-token").equals(TOKEN);
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

    protected void writeResponseBody(HttpServletResponse resp, String data) throws IOException {
        resp.setContentType("application/xml; charset=UTF-8");
        resp.getWriter().write(data);
    }
}
