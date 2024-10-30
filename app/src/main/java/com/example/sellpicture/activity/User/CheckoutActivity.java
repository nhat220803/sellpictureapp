package com.example.sellpicture.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellpicture.R;
import com.example.sellpicture.adapter.CheckoutAdapter;
import com.example.sellpicture.model.CartItem;
import com.example.sellpicture.model.Order;
import com.example.sellpicture.util.CartManager;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCheckout;
    private TextView totalAmountText;
    private RadioGroup paymentMethodRadioGroup;
    private Button confirmPaymentButton;

    private CheckoutAdapter checkoutAdapter;
    private CartManager cartManager;

    // Lấy userId từ SharedPreferences
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        recyclerViewCheckout = findViewById(R.id.recyclerViewCheckout);
        totalAmountText = findViewById(R.id.totalAmount);
        paymentMethodRadioGroup = findViewById(R.id.paymentMethodRadioGroup);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        cartManager = new CartManager(this);

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            // Chuyển hướng về LoginActivity nếu chưa đăng nhập
            Intent loginIntent = new Intent(CheckoutActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Lấy tổng giá trị đơn hàng từ intent
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Hiển thị danh sách sản phẩm và tổng tiền
        setupRecyclerView();
        loadCartItems(userId); // Sử dụng userId để tải sản phẩm trong giỏ hàng của người dùng
        totalAmountText.setText(String.format("Total: $%.2f", totalPrice));

        // Xử lý sự kiện khi người dùng bấm "Confirm Payment"
        confirmPaymentButton.setOnClickListener(v -> {
            int selectedPaymentMethodId = paymentMethodRadioGroup.getCheckedRadioButtonId();
            if (selectedPaymentMethodId == -1) {
                Toast.makeText(CheckoutActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }
            String paymentMethod = getPaymentMethod(selectedPaymentMethodId);
            processPayment(userId, totalPrice, paymentMethod); // Thêm userId vào hàm xử lý thanh toán
        });
    }

    private void setupRecyclerView() {
        recyclerViewCheckout.setLayoutManager(new LinearLayoutManager(this));
        checkoutAdapter = new CheckoutAdapter(this, cartManager.getCartItemsByUserId(userId)); // Sử dụng userId
        recyclerViewCheckout.setAdapter(checkoutAdapter);
    }

    private void loadCartItems(int userId) {
        List<CartItem> cartItems = cartManager.getCartItemsByUserId(userId); // Sử dụng userId
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        checkoutAdapter.setCartItems(cartItems);
    }

    private String getPaymentMethod(int radioButtonId) {
        if (radioButtonId == R.id.paymentZalo) {
            return "Payment with ZaloPay";
        } else if (radioButtonId == R.id.paymentCashOnDelivery) {
            return "cash_on_delivery";
        }
        return "";
    }

    private void processPayment(int userId, double totalPrice, String paymentMethod) {
        // Lấy danh sách sản phẩm trong giỏ hàng của người dùng
        List<CartItem> cartItems = cartManager.getCartItemsByUserId(userId);

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống, không thể thực hiện thanh toán!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đơn hàng
        Order order = new Order(cartItems, totalPrice, paymentMethod, "Completed");

        // Lưu đơn hàng vào cơ sở dữ liệu
        cartManager.saveOrder(order);

        // Thông báo cho người dùng
        Toast.makeText(this, "Payment processed successfully!", Toast.LENGTH_SHORT).show();

        // Xóa giỏ hàng của người dùng sau khi thanh toán thành công
        cartManager.clearCartByUserId(userId);
        finish();
    }
}