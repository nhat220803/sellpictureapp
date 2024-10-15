package com.example.sellpicture;

import static com.example.sellpicture.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        // Thêm xử lý cho BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, ProductList.class)); // Chuyển về màn hình danh sách sản phẩm
                return true;
            } else if (itemId == R.id.nav_search) {
                showSearchBar(); // Hiển thị thanh tìm kiếm
                return true;
            } else if (itemId == R.id.nav_cart) {
                // startActivity(new Intent(this, CartActivity.class)); // Chuyển về CartActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_profile) {
                // startActivity(new Intent(this, UserProfileActivity.class)); // Chuyển về UserProfileActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_more) {
                showMoreOptions(); // Hiển thị thêm tùy chọn
                return true;
            }

            return false;
        });

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
                            Toast.makeText(ProductList.this, "Không có sản phẩm nào!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showMoreOptions() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.nav_more));
        popup.getMenuInflater().inflate(R.menu.more_options_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.phone_shop) {
                // Xử lý khi chọn Phone Shop
                Toast.makeText(this, "Phone Shop được chọn", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.shop_location) {
                // Xử lý khi chọn Shop Location
                Toast.makeText(this, "Shop Location được chọn", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        popup.show();
    }

    // Hàm để hiển thị thanh tìm kiếm
    private void showSearchBar() {
        // Xử lý hiển thị thanh tìm kiếm ở đây
        Toast.makeText(this, "Thanh tìm kiếm được hiển thị", Toast.LENGTH_SHORT).show();
    }

    private List<Product> loadProductsFromDatabase() {
        List<Product> productList = new ArrayList<>();
        try {
            com.example.sellpicture.Connection connectionClass = new com.example.sellpicture.Connection();
            java.sql.Connection conn = connectionClass.CONN();

            if (conn != null) {
                Log.d("DatabaseConnection", "Kết nối thành công");
                String query = "SELECT product_id, product_name, description, price, stock_quantity, image FROM Products";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    int id = rs.getInt("product_id");
                    String name = rs.getString("product_name");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock_quantity");
                    String image = rs.getString("image");

                    Product product = new Product(id, name, description, price, stock, image);
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
