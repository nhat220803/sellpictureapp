package com.example.sellpicture;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tranh.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

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
