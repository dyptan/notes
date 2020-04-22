package src.jdbc;

import java.sql.*;

public class Connect {
    public static void main(String[] args) {
        try (Connection dbConnection = DriverManager.getConnection("jdbc:postgresql://localhost:54321/zoomdata", "postgres", "postgres");){
            Statement st = dbConnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT fullname FROM users");
            while(rs.next()){
                System.out.println(rs.getString(1));
                /*int id = rs.getInt(1);
                String isAvailable =rs.getString(2);
                String descr =rs.getString(3);
                System.out.println(id + " " + isAvailable + " " + descr);*/
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }


}

