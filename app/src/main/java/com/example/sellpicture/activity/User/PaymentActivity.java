package com.example.sellpicture.activity.User;

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

// PaymentActivity.java
public class PaymentActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCheckout;
    private TextView totalAmountText;
    private RadioGroup paymentMethodRadioGroup;
    private Button confirmPaymentButton;

    private CheckoutAdapter checkoutAdapter;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        recyclerViewCheckout = findViewById(R.id.recyclerViewCheckout);
        totalAmountText = findViewById(R.id.totalAmount);
        paymentMethodRadioGroup = findViewById(R.id.paymentMethodRadioGroup);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        cartManager = new CartManager(this);

        // Lấy tổng giá trị đơn hàng từ intent
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Hiển thị danh sách sản phẩm và tổng tiền
        setupRecyclerView();
        loadCartItems();
        totalAmountText.setText(String.format("Total: $%.2f", totalPrice));

        // Xử lý sự kiện khi người dùng bấm "Confirm Payment"
        confirmPaymentButton.setOnClickListener(v -> {
            int selectedPaymentMethodId = paymentMethodRadioGroup.getCheckedRadioButtonId();
            if (selectedPaymentMethodId == -1) {
                Toast.makeText(PaymentActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }
            String paymentMethod = getPaymentMethod(selectedPaymentMethodId);
            processPayment(totalPrice, paymentMethod);
        });

    }

    private void setupRecyclerView() {
        recyclerViewCheckout.setLayoutManager(new LinearLayoutManager(this));
        checkoutAdapter = new CheckoutAdapter(this, cartManager.getCartItems());
        recyclerViewCheckout.setAdapter(checkoutAdapter);
    }

    private void loadCartItems() {
        List<CartItem> cartItems = cartManager.getCartItems();
        checkoutAdapter.setCartItems(cartItems);
    }

    private String getPaymentMethod(int radioButtonId) {
        if (radioButtonId == R.id.paymentCreditCard) {
            return "credit_card";
        } else if (radioButtonId == R.id.paymentCashOnDelivery) {
            return "cash_on_delivery";
        }
        return "";
    }

    private void processPayment(double totalPrice, String paymentMethod) {
        // Lấy danh sách sản phẩm trong giỏ hàng
        List<CartItem> cartItems = cartManager.getCartItems();

        // Tạo đơn hàng
        Order order = new Order(cartItems, totalPrice, paymentMethod, "Completed");

        // Lưu đơn hàng vào cơ sở dữ liệu
        cartManager.saveOrder(order);

        // Thông báo cho người dùng
        Toast.makeText(this, "Payment processed successfully!", Toast.LENGTH_SHORT).show();

        // Xóa giỏ hàng sau khi thanh toán thành công
        cartManager.clearCart();
        finish();
    }
}