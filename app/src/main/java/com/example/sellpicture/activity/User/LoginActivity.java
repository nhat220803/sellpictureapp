package com.example.sellpicture.activity.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.activity.Admin.AdminDashboardActivity;
import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private TextView tvRegisterLink;
    private CreateDatabase createDatabase;
    private SharedPreferences sharedPreferences;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        createDatabase = new CreateDatabase(this);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // Kiểm tra xem người dùng đã đăng nhập chưa
//        if (isLoggedIn()) {
//            redirectToMainActivity();
//        }

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter complete information", Toast.LENGTH_SHORT).show();
            } else {
                login(username, password);
            }
        });

        tvRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void login(String username, String password) {
        SQLiteDatabase db = createDatabase.open();

        String sql = "SELECT * FROM " + CreateDatabase.TB_users + " WHERE " + CreateDatabase.TB_users_username + " = ? AND " + CreateDatabase.TB_users_password + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});

        if (cursor != null && cursor.moveToFirst()) {
            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_users_user_id));
            String role = checkUserRole(userId);

            // Lưu trạng thái đăng nhập vào SharedPreferences
            saveLoginState(userId, username, role);

            // Chuyển hướng dựa trên vai trò của người dùng
            Intent intent;
            if ("Admin".equals(role)) {
                intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
            } else {
                intent = new Intent(LoginActivity.this, ProductList.class);
            }
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "username or password is incorrect", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    private String checkUserRole(int userId) {
        SQLiteDatabase db = createDatabase.open();
        String role = null;

        String sql = "SELECT " + CreateDatabase.TB_users_role_id + " FROM " + CreateDatabase.TB_users + " WHERE " + CreateDatabase.TB_users_user_id + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            int roleId = cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_users_role_id));
            role = getRoleName(roleId);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return role;
    }

    private String getRoleName(int roleId) {
        switch (roleId) {
            case 1:
                return "User";
            case 2:
                return "Admin";
            default:
                return "Unknown Role";
        }
    }

    // Hàm lưu trạng thái đăng nhập vào SharedPreferences
    private void saveLoginState(int userId, String username, String role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.putString("username", username);
        editor.putString("role", role);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    // Kiểm tra xem người dùng đã đăng nhập chưa
    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    // Chuyển hướng đến MainActivity nếu đã đăng nhập
//    private void redirectToMainActivity() {
//        String role = sharedPreferences.getString("role", "User");
//
//        Intent intent;
//        if ("Admin".equals(role)) {
//            intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
//        } else {
//            intent = new Intent(LoginActivity.this, ProductList.class);
//        }
//        startActivity(intent);
//        finish();
//    }
}
