package com.example.sellpicture.activity.User;

import android.content.Intent;
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
    private Button buyButton ;
    private CartManager cartManager;
    private CartAdapter cartAdapter;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        recyclerView = findViewById(R.id.recycleviewcart);
        emptyCartText = findViewById(R.id.emptycart);
        totalPriceText = findViewById(R.id.total);
        buyButton = findViewById(R.id.btnbuy);
//        backButton = findViewById(R.id.btn_back);

        cartManager = new CartManager(this);

        setupRecyclerView();
        loadCartItems();

        buyButton.setOnClickListener(v -> {
            // Implement checkout process
        });

//        backButton.setOnClickListener(v -> {
//            // Implement checkout process
//            Intent detailintent = new Intent(CartActivity.this, ProductList.class); // Điều hướng đến trang CartActivity
//            startActivity(detailintent);
//        });

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



    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, this);
        recyclerView.setAdapter(cartAdapter);
    }

    private void loadCartItems() {
        List<CartItem> cartItems = cartManager.getCartItems();
        if (cartItems.isEmpty()) {
            emptyCartText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            buyButton.setVisibility(View.GONE);
        } else {
            emptyCartText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            buyButton.setVisibility(View.VISIBLE);
            cartAdapter.setCartItems(cartItems);
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        double total = cartAdapter.getTotalPrice();
        totalPriceText.setText(String.format("$%.2f", total));
    }

    @Override
    public void onQuantityChanged(int cartItemId, int newQuantity) {
        cartManager.updateCartItemQuantity(cartItemId, newQuantity);
        updateTotalPrice();
    }

    @Override
    public void onItemRemoved(int cartItemId) {
        cartManager.removeCartItem(cartItemId);
        loadCartItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartManager.close();
    }
}
