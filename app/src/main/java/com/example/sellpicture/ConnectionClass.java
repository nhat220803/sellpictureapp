package com.example.sellpicture;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class ConnectionClass {
    // Thông tin cấu hình kết nối cơ sở dữ liệu
    protected static String db = "tranh_shop"; // Tên cơ sở dữ liệu
    protected static String ip = "10.0.2.2"; // Địa chỉ IP của máy chủ MySQL
    protected static String port = "3306"; // Cổng mặc định của MySQL
    protected static String username = "root";
    protected static String password = "7789";

    // Phương thức kết nối đến cơ sở dữ liệu
    public Connection CONN() {
        Connection conn = null; // Khai báo biến kết nối
        try {
            // Sử dụng driver MySQL
            Class.forName("com.mysql.jdbc.Driver"); // Tải driver MySQL
            // Tạo chuỗi kết nối tới cơ sở dữ liệu
            String connectionString = "jdbc:mysql://" + ip + ":" + port + "/" + db;
            // Kết nối đến cơ sở dữ liệu với thông tin đã cấu hình
            conn = DriverManager.getConnection(connectionString, username, password);

        } catch (Exception e) {
            // Ghi lại thông tin lỗi nếu kết nối không thành công
            Log.e("Error: ", Objects.requireNonNull(e.getMessage()));
        }
        return conn; // Trả về kết nối (null nếu thất bại)
    }
}
