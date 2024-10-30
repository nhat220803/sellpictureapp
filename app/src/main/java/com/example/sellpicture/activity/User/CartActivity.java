package com.example.sellpicture.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellpicture.R;
import com.example.sellpicture.adapter.CartAdapter;
import com.example.sellpicture.model.CartItem;
import com.example.sellpicture.util.CartManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemListener {

    private RecyclerView recyclerView;
    private TextView emptyCartText, totalPriceText;
    private Button buyButton;
    private CartManager cartManager;
    private CartAdapter cartAdapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Ánh xạ các thành phần trong layout
        recyclerView = findViewById(R.id.recycleviewcart);
        emptyCartText = findViewById(R.id.emptycart);
        totalPriceText = findViewById(R.id.total);
        buyButton = findViewById(R.id.btnbuy);

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            // Nếu chưa đăng nhập, chuyển hướng về LoginActivity
            Intent loginIntent = new Intent(CartActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        cartManager = new CartManager(this);
        setupRecyclerView();
        loadCartItems(userId);

        buyButton.setOnClickListener(v -> {
            double totalPrice = cartAdapter.getTotalPrice(); // Lấy tổng tiền từ giỏ hàng
            if (totalPrice > 0) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("totalPrice", totalPrice); // Truyền tổng tiền sang PaymentActivity
                intent.putExtra("userId", userId); // Truyền userId sang PaymentActivity
                startActivity(intent);
            } else {
                Toast.makeText(this, "Giỏ hàng trống. Vui lòng thêm sản phẩm.", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, ProductList.class)); // Chuyển về màn hình danh sách sản phẩm
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class)); // Chuyển về CartActivity (sẽ thêm sau)
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
                startActivity(new Intent(this, MapActivity.class));
            }else if (item.getItemId() == R.id.chat_with_shop){
                       startActivity(new Intent(this, ChatActivity.class));

            }else if (item.getItemId() == R.id.support_chat ) {
                    startActivity(new Intent(this,SupportChatActivity.class));
            }
            return false;
        });

        popup.show();
    }





    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, this);
        recyclerView.setAdapter(cartAdapter);
    }

    private void loadCartItems(int userId) {
        // Lấy giỏ hàng của user cụ thể
        List<CartItem> cartItems = cartManager.getCartItemsByUserId(userId);

        // Cập nhật adapter với danh sách sản phẩm trong giỏ hàng
        if (cartItems != null && !cartItems.isEmpty()) {
            cartAdapter.setCartItems(cartItems);
            recyclerView.setVisibility(View.VISIBLE);
            emptyCartText.setVisibility(View.GONE);
            buyButton.setVisibility(View.VISIBLE);
            updateTotalPrice(); // Cập nhật tổng tiền
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyCartText.setVisibility(View.VISIBLE);
            buyButton.setVisibility(View.GONE);
            totalPriceText.setText("Total: $0.00"); // Đặt tổng tiền bằng 0 khi giỏ hàng trống
        }
    }

    private void updateTotalPrice() {
        double total = cartAdapter.getTotalPrice();
        totalPriceText.setText(String.format("Total: $%.2f", total));
    }

    @Override
    public void onQuantityChanged(int cartItemId, int newQuantity) {
        // Cập nhật số lượng sản phẩm trong giỏ hàng
        cartManager.updateCartItemQuantity(cartItemId, newQuantity);
        loadCartItems(userId); // Tải lại giỏ hàng sau khi cập nhật
    }

    @Override
    public void onItemRemoved(int cartItemId) {
        // Xóa sản phẩm khỏi giỏ hàng
        cartManager.removeCartItem(cartItemId);
        loadCartItems(userId); // Tải lại giỏ hàng sau khi xóa sản phẩm
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartManager.close();
    }
}
