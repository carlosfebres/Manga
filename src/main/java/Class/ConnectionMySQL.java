package Class;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {
    private static Connection conn = null;

    public ConnectionMySQL() {
    }

    public static Connection getConnection() {
        if (conn == null) {
            Props props = new Props();
            String url = "jdbc:mysql://" + props.getProperty("DBHost") + ":" + props.getProperty("DBPort") + "/" + props.getProperty("DBName");
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, props.getProperty("DBUser"), props.getProperty("DBPass"));
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Error: " + e);
            }
        }
        return conn;
    }
}
