package com.example.sellpicture.activity.User;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.R;

public class SignupActivity extends AppCompatActivity {
    private EditText etFullName, etEmail, etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLoginLink;
    private CreateDatabase createDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        createDatabase = new CreateDatabase(this);

        btnRegister.setOnClickListener(v -> registerUser());
        tvLoginLink.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = createDatabase.open();
        ContentValues values = new ContentValues();
        values.put(CreateDatabase.TB_users_username, username);
        values.put(CreateDatabase.TB_users_password, password);
        values.put(CreateDatabase.TB_users_email, email);
        values.put(CreateDatabase.TB_users_full_name, fullName);
        values.put(CreateDatabase.TB_users_role_id, 1);

        long newRowId = db.insert(CreateDatabase.TB_users, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
