package utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


public class Queries {



    public String authentication(String user_username, String user_password) {
        PreparedStatement ps = null;
        ResultSet rs = null;
//        Props props = new Props();

        try {
//            String query = props.getProperty("query1");
            String query = "Select * from users where user_username = ? and user_password = ?";
            ps = ConnectionMySQL.getConnection().prepareStatement(query);
            ps.setString(1, user_username);
            ps.setString(2, user_password);
            rs = ps.executeQuery();

            if (rs.absolute(1)) {
                String id =  rs.getObject("user_id").toString();
                System.out.println("absolute(1) : "+id );
                return id;
            }


        } catch (SQLException e) {
            System.err.println("Error " + e);
        }
        return null;
    }

    public boolean registry(String user_name, String last_name, String user_email, String user_username, String user_password) {

        PreparedStatement ps = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Props props = new Props();


        try {
            String query = "select * from users where user_username = ?";
            pst = ConnectionMySQL.getConnection().prepareStatement(query);
            pst.setString(1, user_username);
            rs = pst.executeQuery();

            if (!rs.absolute(1)) {
                String insertQuery ="insert into users (type_id,user_creation_time,user_email,user_name,user_password,user_username) values(2,?,?,?,?,?)";
                ps = ConnectionMySQL.getConnection().prepareStatement(insertQuery);
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
                ps.setString(1, timestamp);
                ps.setString(2, user_email);
                ps.setString(3, user_name+" "+last_name);
                ps.setString(4, user_password);
                ps.setString(5, user_username);
                System.out.println(ps.toString());
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

    public boolean createManga(int user_id, String manga_genre, String manga_name, int manga_status, String manga_synopsis, String timestamp) {

        PreparedStatement ps = null;

        PreparedStatement pst = null;
        int rs = 0;
//        Props props = new Props();
        try (Connection connection = ConnectionMySQL.getConnection()) {
            connection.setAutoCommit(false);

            try {
                System.out.println("starting transaction");
                ArrayList<String> queries = new ArrayList<String>();
                queries.add("insert into manga (user_id,manga_name,manga_synopsis,manga_status,manga_creation_time) values (?,?,?,?,?)");
                queries.add("select genres_id from genres where genre_des = ?");
                queries.add("insert into manga_genre (genres_id,manga_id) values (?,?)");

                pst = connection.prepareStatement(queries.get(0));
                pst.setInt(1, user_id);
                pst.setString(2, manga_name);
                pst.setString(3, manga_synopsis);
                pst.setInt(4, manga_status);
                pst.setString(5, timestamp);
                System.out.println(pst.toString());
                pst.executeUpdate();
                pst = connection.prepareStatement("SELECT manga_id from manga where manga_name = ?");
                pst.setString(1,manga_name);
                ResultSet mangaIdRow = pst.executeQuery();
                mangaIdRow.next();
                int manga_id = mangaIdRow.getInt("manga_id");
                pst = connection.prepareStatement(queries.get(1));
                pst.setString(1, manga_genre);
                pst.executeQuery();
                pst = connection.prepareStatement("SELECT genres_id from genres where genre_des = ?");
                pst.setString(1,manga_genre);
                ResultSet idRow = pst.executeQuery();
                idRow.next();
                int genres_id = idRow.getInt("genres_id");
                pst = connection.prepareStatement(queries.get(2));
                pst.setInt(1, genres_id);
                pst.setInt(2, manga_id);
                pst.executeUpdate();
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addChapter(String chapter_title, int chapter_number, int chapter_num_pages, String chapter_location, String manga_genre, int manga_id, String manga_name) {

        PreparedStatement ps = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

//        Props props = new Props();


        try {
                String insertQuery ="insert into chapters ( manga_id, chapter_number, chapter_title, chapter_creation_time, chapter_location, chapter_num_pages) values (?,?,?,?,?,?)";
                ps = ConnectionMySQL.getConnection().prepareStatement(insertQuery);
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
                ps.setString(4, timestamp);
                ps.setInt(1, manga_id);
                ps.setInt(2, chapter_number);
                ps.setString(3, chapter_title);
                ps.setString(4, timestamp);
                ps.setString(5, chapter_location);
                ps.setInt(6, chapter_num_pages);
                System.out.println(ps.toString());
                if (ps.executeUpdate() == 1) {
                    return true;
                }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;

    }

    public HashMap<String,String> getMangaData(int manga_id) {
        PreparedStatement ps = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

//        Props props = new Props();


        try {
            String select ="select a.manga_name,b.genres_id from manga a join manga_genre b on a.manga_id = b.manga_id where a.manga_id = ?";
            ps = ConnectionMySQL.getConnection().prepareStatement(select);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
            ps.setInt(1, manga_id);
            System.out.println(ps.toString());
            rs = ps.executeQuery();
            rs.next();
            int genres_id = rs.getInt("genres_id") ;
            String manga_name = rs.getString("manga_name");
            ps = ConnectionMySQL.getConnection().prepareStatement("select genre_des from genres where genres_id = ?");
            ps.setInt(1,genres_id);
            rs = ps.executeQuery();
            rs.next();
            String manga_genre = rs.getString("genre_des");
            HashMap dataMap = new HashMap();
            dataMap.put("manga_name",manga_name);
            dataMap.put("manga_genre",manga_genre);
            dataMap.put("genres_id",Integer.toString(genres_id));
            return dataMap;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;

    }

    public boolean updateManga(String manga_name, String manga_genre,int genres_id, int manga_id, String new_genre, String new_name, String new_synopsis) {

        PreparedStatement ps = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        Props props = new Props();

        try (Connection connection = ConnectionMySQL.getConnection()) {
            connection.setAutoCommit(false);

            try {
                String update = "update manga set manga_name = ?, manga_synopsis = ? where manga_id = ? ";
                ps = connection.prepareStatement(update);

                ps.setInt(3, manga_id);
                ps.setString(1, new_name);
                ps.setString(2, new_synopsis);
                System.out.println(ps.toString());
               ps.executeUpdate();
               ps=connection.prepareStatement("select  genres_id from genres where genre_des=?");
               ps.setString(1,new_genre);
               System.out.println(ps.toString());
               rs = ps.executeQuery();
               rs.next();
               int new_genre_id = rs.getInt("genres_id");

               update = "update manga_genre set genres_id = ? where manga_id = ?";
               ps = connection.prepareStatement(update);
               ps.setInt(1,new_genre_id);
               ps.setInt(2,manga_id);
               ps.executeUpdate();

               connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();

                return false;

            }
        }catch(Exception e) {

            e.printStackTrace();

        }
        return false;
    }

    public boolean deleteChapter(int chapter_id) {

        PreparedStatement ps = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

//        Props props = new Props();


        try {
            String delete ="delete from chapters where chapter_id = ?";
            ps = ConnectionMySQL.getConnection().prepareStatement(delete);
            ps.setInt(1, chapter_id);
            System.out.println(ps.toString());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }

    }

    public boolean deleteManga(int manga_id) {
        PreparedStatement ps = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

//        Props props = new Props();


        try {
            String delete ="delete from manga where manga_id = ?";
            ps = ConnectionMySQL.getConnection().prepareStatement(delete);
            ps.setInt(1, manga_id);
            System.out.println(ps.toString());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }
}
 