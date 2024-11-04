package com.example.sellpicture.activity.User;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.DAO.CartManager;
import com.example.sellpicture.R;
import com.example.sellpicture.context.CreateDatabase;

public class PaymentActivity extends AppCompatActivity {

    private TextView totalAmountText;
    private EditText recipientName, address, district, city, province, zipCode, phoneNumber;
    private Button confirmPaymentButton;
    private CreateDatabase createDatabase;
    private CartManager cartManager;
    private int userId;
    private double shippingFee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        totalAmountText = findViewById(R.id.totalAmount);
        recipientName = findViewById(R.id.recipientName);
        address = findViewById(R.id.address);
        district = findViewById(R.id.district);
        city = findViewById(R.id.city);
        province = findViewById(R.id.province);
        zipCode = findViewById(R.id.zipCode);
        phoneNumber = findViewById(R.id.phoneNumber);
        confirmPaymentButton = findViewById(R.id.confirmPayment);
        cartManager = new CartManager(this);


        createDatabase = new CreateDatabase(this);
        shippingFee = 30000; // Default shipping fee in VND

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            // Chuyển hướng về LoginActivity nếu chưa đăng nhập
            Intent loginIntent = new Intent(PaymentActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Nhận tổng số tiền từ intent
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        totalAmountText.setText(String.format("Total: $%.2f", totalPrice));

        // Xử lý sự kiện khi nhấn nút "Confirm Payment"
        confirmPaymentButton.setOnClickListener(v -> {
            if (validateFields()) {
                saveShippingAddressAndProcessPayment();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            finishAffinity();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return true;
    }

    private void saveShippingAddressAndProcessPayment() {
        SQLiteDatabase db = createDatabase.open();
        db.beginTransaction();
        try {
            // Prepare shipping address data
            ContentValues addressValues = new ContentValues();
            addressValues.put(CreateDatabase.TB_shipping_addresses_user_id, userId);
            addressValues.put(CreateDatabase.TB_shipping_addresses_recipient_name, recipientName.getText().toString().trim());
            addressValues.put(CreateDatabase.TB_shipping_addresses_address, address.getText().toString().trim());
            addressValues.put(CreateDatabase.TB_shipping_addresses_district, district.getText().toString().trim());
            addressValues.put(CreateDatabase.TB_shipping_addresses_city, city.getText().toString().trim());
            addressValues.put(CreateDatabase.TB_shipping_addresses_province, province.getText().toString().trim());
            addressValues.put(CreateDatabase.TB_shipping_addresses_zip_code, zipCode.getText().toString().trim());
            addressValues.put(CreateDatabase.TB_shipping_addresses_phone, phoneNumber.getText().toString().trim());

            // Insert shipping address and get the generated ID
            long shippingAddressId = db.insert(CreateDatabase.TB_shipping_addresses, null, addressValues);

            if (shippingAddressId == -1) {
                throw new Exception("Failed to save shipping address");
            }

            // Create order record
            double totalAmount = getIntent().getDoubleExtra("totalPrice", 0.0);
            ContentValues orderValues = new ContentValues();
            orderValues.put(CreateDatabase.TB_orders_user_id, userId);
            orderValues.put(CreateDatabase.TB_orders_total_amount, totalAmount);
            orderValues.put(CreateDatabase.TB_orders_shipping_fee, shippingFee);
            orderValues.put(CreateDatabase.TB_orders_payment_method, "cod");
            orderValues.put(CreateDatabase.TB_orders_shipping_address_id, shippingAddressId);
            orderValues.put(CreateDatabase.TB_orders_status, "approved");

            long orderId = db.insert(CreateDatabase.TB_orders, null, orderValues);

            if (orderId == -1) {
                throw new Exception("Failed to create order");
            }

            // If everything is successful, commit the transaction
            db.setTransactionSuccessful();
            Toast.makeText(this, "Shipping address saved and order created successfully!", Toast.LENGTH_SHORT).show();

            // Navigate back to ProductListActivity
            Intent productListIntent = new Intent(PaymentActivity.this, ProductList.class);
            startActivity(productListIntent);
            finish(); // Close PaymentActivity


        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
            db.close();
        }

        cartManager.clearCartByUserId(userId);

    }

    // Phương thức kiểm tra tính hợp lệ của các trường thông tin giao hàng
    private boolean validateFields() {
        if (recipientName.getText().toString().isEmpty() ||
                address.getText().toString().isEmpty() ||
                district.getText().toString().isEmpty() ||
                city.getText().toString().isEmpty() ||
                province.getText().toString().isEmpty() ||
                phoneNumber.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

