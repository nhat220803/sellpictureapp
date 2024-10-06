package com.example.sellpicture;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private Connection connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        connectionClass = new Connection();

        fetchDataFromMySQL();

        // Thêm nút để chuyển đến CartActivity
        Button buttonViewCart = findViewById(R.id.buttonViewCart);
        buttonViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void fetchDataFromMySQL() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            java.sql.Connection conn = null;
            try {
                conn = connectionClass.CONN();
                if (conn != null) {
                    String query = "SELECT * FROM Products";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        String productName = rs.getString("product_name");
                        String description = rs.getString("description");
                        double price = rs.getDouble("price");

                        productList.add(new Product(productName, description, price));
                    }

                    runOnUiThread(() -> {
                        productAdapter = new ProductAdapter(productList, MainActivity.this, new ProductAdapter.OnProductClickListener() {
                            @Override
                            public void onAddToCartClick(Product product) {
                                CartManager.getInstance().addToCart(product);
                                Toast.makeText(MainActivity.this, product.getProductName() + " đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            }
                        });
                        recyclerView.setAdapter(productAdapter);
                    });

                    showToastMessage("Kết nối thành công");
                } else {
                    showToastMessage("Không thể kết nối tới database");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showToastMessage("Đã xảy ra lỗi: " + e.getMessage());
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showToastMessage(String message) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}