package com.example.sellpicture;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemClickListener {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView textViewTotal;
    private List<Product> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        textViewTotal = findViewById(R.id.textViewTotal);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách sản phẩm trong giỏ hàng
        cartItems = CartManager.getInstance().getCartItems();

        // Tạo và thiết lập adapter cho RecyclerView
        cartAdapter = new CartAdapter(cartItems, this);
        cartAdapter.setOnCartItemClickListener(this);
        recyclerViewCart.setAdapter(cartAdapter);

        updateTotalPrice();
        // Thêm xử lý cho nút Quay lại
        Button buttonBackToProducts = findViewById(R.id.buttonBackToProducts);
        buttonBackToProducts.setOnClickListener(v -> {
            finish(); // Đóng CartActivity và quay lại MainActivity
        });
    }

    private void updateTotalPrice() {
        double totalPrice = CartManager.getInstance().getTotalPrice();
        textViewTotal.setText(String.format("Tổng cộng: $%.2f", totalPrice));
    }

    @Override
    public void onIncreaseClick(int position) {
        Product product = cartItems.get(position);
        product.setQuantity(product.getQuantity() + 1);
        CartManager.getInstance().updateCart(product);
        cartAdapter.notifyItemChanged(position);
        updateTotalPrice();
    }

    @Override
    public void onDecreaseClick(int position) {
        Product product = cartItems.get(position);
        if (product.getQuantity() > 1) {
            product.setQuantity(product.getQuantity() - 1);
            CartManager.getInstance().updateCart(product);
            cartAdapter.notifyItemChanged(position);
        } else {
            CartManager.getInstance().removeFromCart(product);
            cartItems.remove(position);
            cartAdapter.notifyItemRemoved(position);
        }
        updateTotalPrice();
    }

    @Override
    public void onRemoveClick(int position) {
        Product product = cartItems.get(position);
        CartManager.getInstance().removeFromCart(product);
        cartItems.remove(position);
        cartAdapter.notifyItemRemoved(position);
        updateTotalPrice();
    }
}
