package com.example.sellpicture.activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.R;

public class AdminDashboardActivity extends AppCompatActivity {
    private Button btnManageUsers, btnManageProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageProducts = findViewById(R.id.btnManageProducts);

        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageUsersActivity.class);
            startActivity(intent);
        });

        btnManageProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageProductActivity.class);
            startActivity(intent);
        });

    }
}
