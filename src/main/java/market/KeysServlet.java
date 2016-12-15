package market;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by PerevalovaMA on 29.11.2016.
 */
@WebServlet("/update_auth")
public class KeysServlet extends AuthServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        updateAuth();
    }

    @Override
    void initDataPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
