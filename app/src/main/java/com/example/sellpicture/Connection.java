package com.example.sellpicture;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    // Thông tin kết nối
    private static final String db = "pictureshop";
    private static final String user = "root";
    private static final String pass = "12345678";
    private static final String ip = "10.0.2.2";
    private static final String port = "3306";

    public java.sql.Connection CONN() {
        java.sql.Connection conn = null;
        String connURL;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connURL = "jdbc:mysql://" + ip + ":" + port + "/" + db;
            conn = DriverManager.getConnection(connURL, user, pass);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}