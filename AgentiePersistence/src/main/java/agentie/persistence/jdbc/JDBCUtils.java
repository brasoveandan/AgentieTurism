package agentie.persistence.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    private Properties props;

    public JDBCUtils(Properties props){
        this.props=props;
    }

    private static Connection instance=null;

    private Connection getNewConnection(){
        String driver=props.getProperty("agentie.jdbc.driver");
        String url=props.getProperty("agentie.jdbc.url");
        String user=props.getProperty("agentie.jdbc.user");
        String pass=props.getProperty("agentie.jdbc.pass");
        Connection con=null;
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver "+e);
        } catch (SQLException e) {
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return instance;
    }
}
