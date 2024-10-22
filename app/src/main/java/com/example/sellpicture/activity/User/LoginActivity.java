
package com.example.sellpicture.activity.User;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.activity.Admin.AddProductActivity;
import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private TextView tvRegisterLink;
    private CreateDatabase createDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        createDatabase = new CreateDatabase(this);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Đang đăng nhập...", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            String role = checkUserRole(username); // Kiểm tra vai trò

            Intent intent;
            if ("Admin".equals(role)) {
                intent = new Intent(LoginActivity.this, AddProductActivity.class); // Thay thế bằng activity dành cho admin
            } else {
                intent = new Intent(LoginActivity.this, ProductList.class);
            }
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Tên người dùng hoặc mật khẩu không đúng.", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    private String checkUserRole(String username) {
        SQLiteDatabase db = createDatabase.open();
        String role = null;

        // Truy vấn để lấy vai trò của người dùng theo tên người dùng
        String sql = "SELECT " + CreateDatabase.TB_users_role_id + " FROM " + CreateDatabase.TB_users + " WHERE " + CreateDatabase.TB_users_username + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            int roleId = cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_users_role_id));
            role = getRoleName(roleId); // Lấy tên vai trò từ ID vai trò
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return role;
    }

    // Hàm này sẽ trả về tên vai trò dựa trên ID vai trò
    private String getRoleName(int roleId) {
        switch (roleId) {
            case 1:
                return "User";
            case 2:
                return "Admin";
            // Thêm các vai trò khác nếu cần
            default:
                return "Unknown Role";
        }
    }
}
