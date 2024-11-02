package com.example.sellpicture.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.R;

public class PaymentActivity extends AppCompatActivity {

    private EditText recipientNameEditText;
    private EditText addressEditText;
    private EditText phoneNumberEditText;
    private Button confirmPaymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        recipientNameEditText = findViewById(R.id.recipientName);
        addressEditText = findViewById(R.id.address);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        confirmPaymentButton = findViewById(R.id.confirmPayment);

        confirmPaymentButton.setOnClickListener(v -> {
            // Get the entered details
            String recipientName = recipientNameEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();

            // Validate input fields
            if (recipientName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(PaymentActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Handle order processing for cash on delivery here if needed

            // Show confirmation
            Toast.makeText(PaymentActivity.this, "Order confirmed! Cash on delivery selected.", Toast.LENGTH_SHORT).show();

            // Finish activity and go back to CheckoutActivity
            finish();
        });
    }
}

