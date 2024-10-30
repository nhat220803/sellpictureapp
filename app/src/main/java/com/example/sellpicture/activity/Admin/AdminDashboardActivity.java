package com.example.sellpicture.activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.R;

public class AdminDashboardActivity extends AppCompatActivity {
    private Button btnManageUsers, btnManageProducts, btnViewOrders, btnViewPayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageProducts = findViewById(R.id.btnManageProducts);
        btnViewOrders = findViewById(R.id.btnViewOrders);
        btnViewPayments = findViewById(R.id.btnViewPayments);

        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageUsersActivity.class);
            startActivity(intent);
        });

        btnManageProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

//        btnViewOrders.setOnClickListener(v -> {
//            Intent intent = new Intent(AdminDashboardActivity.this, ViewOrdersActivity.class);
//            startActivity(intent);
//        });
//
//        btnViewPayments.setOnClickListener(v -> {
//            Intent intent = new Intent(AdminDashboardActivity.this, ViewPaymentsActivity.class);
//            startActivity(intent);
//        });
    }
}
