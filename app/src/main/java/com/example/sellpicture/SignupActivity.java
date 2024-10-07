package com.example.sellpicture;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.R;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {
    private EditText etFullName, etEmail, etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLoginLink;
    private String TAG = "TAG123";
    private Connection connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        connectionClass = new Connection();

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Đang đăng ký...", Toast.LENGTH_SHORT).show();
                    signup(username, password, email, fullName);
                }
            }
        });

        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void signup(String username, String password, String email, String fullName) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            java.sql.Connection conn = connectionClass.CONN();
            if (conn == null) {
                showToastMessage("Không thể kết nối tới database");
                return;
            }

            String checkEmailUserNameSQL = "SELECT * FROM Users WHERE username = ? OR email =?";
            try {
                PreparedStatement check = conn.prepareStatement(checkEmailUserNameSQL);
                check.setString(1,username);
                check.setString(2,email);
                ResultSet resultSet = check.executeQuery();

                if(resultSet.next()){
                    Log.e(TAG, "Tên người dùng hoặc email đã được đăng ký!" );
                    showToastMessage("Tên người dùng hoặc email đã được đăng ký!");
                    return;
                }
            }catch (SQLException e){
                Log.e(TAG, "signup: ",e );
            }

            String sql = "INSERT INTO Users (username, password, email, full_name) VALUES (?, ?, ?, ?)";

            try {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, email);
                stmt.setString(4, fullName);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    showToastMessage("Đăng ký thành công");
                } else {
                    showToastMessage("Đăng kys thất bại");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showToastMessage("Đăng kys thất bại" + e);
                Log.e(TAG, "signup: ", e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    showToastMessage("Đăng kys thất bại" + e);
                    Log.e(TAG, "signup: ", e);
                    e.printStackTrace();
                }
            }
        });
    }

    private void showToastMessage(String message) {
        runOnUiThread(() -> Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}