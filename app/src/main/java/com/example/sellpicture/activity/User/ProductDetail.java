

package com.example.sellpicture.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.R;
import com.example.sellpicture.model.Product;
import com.example.sellpicture.DAO.CartManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductDetail extends AppCompatActivity {

    private TextView productName, productDescription, productPrice, productQuantity;
    private ImageView productImage;
    private Button purchaseButton, viewCartButton;
    private CreateDatabase dbHelper;
    private int productId;
    private CartManager cartManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize views
        productName = findViewById(R.id.detail_Title);
        productDescription = findViewById(R.id.detail_Des);
        productPrice = findViewById(R.id.detail_Price);
        productQuantity = findViewById(R.id.detail_Quantity);
        productImage = findViewById(R.id.detail_Image);
        purchaseButton = findViewById(R.id.detail_Purchase);

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            // Chuyển hướng về LoginActivity nếu chưa đăng nhập
            Intent loginIntent = new Intent(ProductDetail.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Initialize database helper and cart manager
        dbHelper = new CreateDatabase(this);
        cartManager = new CartManager(this);

        // Get product ID from Intent
        Intent intent = getIntent();
        productId = intent.getIntExtra("product_id", -1);
        if (productId != -1) {
            loadProductDetails(productId);
        }

        // Set up purchase button click listener
        purchaseButton.setOnClickListener(v -> {
            addToCart(userId, productId); // Thêm sản phẩm vào giỏ hàng với userId
        });

        // Thêm xử lý cho BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, ProductList.class)); // Chuyển về màn hình danh sách sản phẩm
                return true;

            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class)); // Chuyển về CartActivity
                return true;
            } else if (itemId == R.id.nav_profile) {
                Log.e("TAG123", "onCreate: " );
                startActivity(new Intent(this, UserProfileActivity.class)); // Chuyển về UserProfileActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_more) {
                showMoreOptions(); // Hiển thị thêm tùy chọn
                return true;
            }

            return false;
        });
    }

    private void showMoreOptions() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.nav_more));
        popup.getMenuInflater().inflate(R.menu.more_options_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.shop_location) {
                // Xử lý khi chọn Shop Location
                startActivity(new Intent(this, MapActivity.class));
            } else if (item.getItemId() == R.id.chat_with_shop){
                startActivity(new Intent(this, ChatActivity.class));

            }else if (item.getItemId() == R.id.support_chat ) {
                startActivity(new Intent(this,SupportChatActivity.class));
            }
            else if (item.getItemId() == R.id.call ) {
                startActivity(new Intent(this, CallActivity.class));
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

    private void addToCart(int userId, int productId) {
        cartManager.addToCart(userId, productId, 1); // Thêm 1 sản phẩm vào giỏ hàng cho userId
        Toast.makeText(this, "Product has been added to cart", Toast.LENGTH_SHORT).show();
    }

    private void loadProductDetails(int productId) {
        SQLiteDatabase db = dbHelper.open();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CreateDatabase.TB_products + " WHERE " + CreateDatabase.TB_products_product_id + "=?", new String[]{String.valueOf(productId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Create a Product object from cursor
            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_product_id)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_product_name)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_description)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_price)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_stock_quantity)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_image))
            );

            // Update UI with product details
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(String.format("$%.2f", product.getPrice()));
            productQuantity.setText(String.valueOf(product.getStockQuantity()));

            // Load image into ImageView using Glide
            Glide.with(this)
                    .load(product.getImage())
                    .into(productImage);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartManager.close();
    }
}
