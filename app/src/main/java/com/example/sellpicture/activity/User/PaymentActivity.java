package com.example.sellpicture.activity.User;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellpicture.R;
import com.example.sellpicture.adapter.CartAdapter;
import com.example.sellpicture.model.CartItem;
import com.example.sellpicture.util.CartManager;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCheckout;
    private TextView totalAmountText;
    private Button btnconfirm;
    private RadioGroup paymentmethod;
    private CartManager cartManager;
    private List<CartItem> cartItemsList;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        recyclerViewCheckout = findViewById(R.id.recyclerViewCheckout);
        totalAmountText = findViewById(R.id.totalAmount);
        btnconfirm = findViewById(R.id.confirmPaymentButton);
        paymentmethod = findViewById(R.id.paymentMethodRadioGroup);
        cartManager = new CartManager(this);
        cartItemsList = cartManager.getCartItems();

        double totalPrice = getIntent().getDoubleExtra("totalPrice",0.0);
        totalAmountText.setText("Total: $" + String.format("%.2f", totalPrice));
        calculateTotalAmount();

        setupRecycleView();

        btnconfirm.setOnClickListener(view ->  processPayment());
    }

    private void calculateTotalAmount() {
        totalAmount = 0.0;
        for (CartItem item : cartItemsList) {
            totalAmount += item.getQuantity() * item.getPrice();
        }
        totalAmountText.setText("Total: $" + String.format("%.2f", totalAmount));
    }

    private void setupRecycleView() {
        CartAdapter adapter = new CartAdapter(this, (CartAdapter.CartItemListener) cartItemsList);
        recyclerViewCheckout.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCheckout.setAdapter(adapter);
    }

    private void processPayment() {
        int selectedPaymentMethodId = paymentmethod.getCheckedRadioButtonId();
        if (selectedPaymentMethodId == R.id.paymentCreditCard) {
            // Xử lý thanh toán bằng thẻ tín dụng
            Toast.makeText(this, "Credit Card payment selected", Toast.LENGTH_SHORT).show();
        } else if (selectedPaymentMethodId == R.id.paymentCashOnDelivery) {
            // Xử lý thanh toán khi nhận hàng
            Toast.makeText(this, "Cash on Delivery payment selected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
        }
    }
}

