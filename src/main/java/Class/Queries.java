package Class;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Queries {



    public boolean authentication(String user_username, String user_password) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Props props = new Props();

        try {
            String query = props.getProperty("query1");
            ps = ConnectionMySQL.getConnection().prepareStatement(query);
            ps.setString(1, user_username);
            ps.setString(2, user_password);
            rs = ps.executeQuery();

            if (rs.absolute(1)) {
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error " + e);
        }
        return false;
    }

    public boolean registry(String user_name, String last_name, String user_email, String user_username, String user_password) {

        PreparedStatement ps = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Props props = new Props();


        try {

            String query = "select * from users where email = ?";
            pst = ConnectionMySQL.getConnection().prepareStatement(query);
            pst.setString(1, user_username);
            rs = pst.executeQuery();

            if (!rs.absolute(1)) {
                ps = ConnectionMySQL.getConnection().prepareStatement(props.getProperty("query2"));
                ps.setString(1, user_name);
                ps.setString(2, last_name);
                ps.setString(3, user_email);
                ps.setString(4, user_password);

                if (ps.executeUpdate() == 1) {
                    return true;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error " + e);
            System.out.println("Hola");

        }
        return false;
    }

}
 