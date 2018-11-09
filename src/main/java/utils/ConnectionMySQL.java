package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionMySQL {
    private static Connection conn = null;

    public ConnectionMySQL() {
    }

    public static Connection getConnection() {

        Props props = new Props();
//            String url = "jdbc:mysql://" + props.getProperty("DBHost") + ":" + "3306" + "/" + props.getProperty("DBName");
        String url = "jdbc:mysql://localhost:3306/manga";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println(props.getProperty("DBUser"));
            conn = DriverManager.getConnection(url, "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error: " + e);
        }
        return conn;
    }
}
