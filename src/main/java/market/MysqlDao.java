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
public class MysqlDao extends DbDao{
    MysqlDao() {
        super("jdbc/mysql");
    }
}
