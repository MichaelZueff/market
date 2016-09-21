package market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 19.09.2016.
 */
public class DbDao {
    protected DataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(DbDao.class);

    DbDao(String resourceName) {
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/" + resourceName);
            if (pool == null) {
                throw new ServletException("Unknown DataSource " + resourceName);
            }
        } catch (NamingException | ServletException ex) {
            LOG.error("Failed to create connection pool for class " + this.getClass(), ex);
        }
    }

    void execute(String sql,
                 ResultSetExecuteProcessor processor,
                 Object... params) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    processor.process(rs);
                }
            }
        }
    }

    protected <T> T selectValue(String sql,
                                ResultSetSelectProcessor<T> processor,
                                Object... params) throws SQLException {
        T value = null;
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    value = processor.process(rs);
                }
            }
        }
        return value;
    }
}
