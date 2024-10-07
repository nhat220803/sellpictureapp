package com.example.sellpicture;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.sellpicture.R;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private Connection connectionClass;
    private TextView txtResult;

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
//        finish();
        setContentView(R.layout.activity_main);

        txtResult = findViewById(R.id.textView);

        // Khởi tạo lớp kết nối và DatabaseHelper
        connectionClass = new Connection();
//        databaseHelper = new DatabaseHelper();

        // Kết nối và lấy dữ liệu
        fetchDataFromMySQL();
    }

    // Hàm này sẽ thực hiện việc kết nối và gọi hàm để lấy dữ liệu
    private void fetchDataFromMySQL() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                java.sql.Connection conn = connectionClass.CONN(); // Kết nối tới database
                if (conn != null) {
                    showToastMessage("Kết nối thành công");
                } else {
                    showToastMessage("Không thể kết nối tới database");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToastMessage("Đã xảy ra lỗi: " + e.getMessage());
            }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // Sử dụng ExecutorService để tải sản phẩm từ cơ sở dữ liệu
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Product> products = loadProductsFromDatabase();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (products != null && !products.isEmpty()) {
                            productList.addAll(products);
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Không có sản phẩm nào!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // Hàm để hiển thị thông báo bằng Toast
    private void showToastMessage(String message) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    // Hàm để cập nhật TextView với dữ liệu từ cơ sở dữ liệu
    private void updateUIWithResult(String result) {
        runOnUiThread(() -> txtResult.setText(result));
    }







    private List<Product> loadProductsFromDatabase() {
        List<Product> productList = new ArrayList<>();
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection conn = connectionClass.CONN();

            if (conn != null) {
                Log.d("DatabaseConnection", "Kết nối thành công");
                String query = "SELECT product_id, product_name, description, price, stock_quantity, image FROM Products"; // Thêm cột image
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    int id = rs.getInt("product_id");
                    String name = rs.getString("product_name");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock_quantity");
                    String image = rs.getString("image"); // Lấy đường dẫn hình ảnh

                    Product product = new Product(id, name, description, price, stock, image); // Thêm image vào constructor
                    productList.add(product);
                }
                conn.close();
            } else {
                Log.e("Error", "Kết nối đến cơ sở dữ liệu thất bại!");
            }
        } catch (Exception e) {
            Log.e("Error", "Lỗi: " + e.getMessage());
        }
        return productList;
    }

}
