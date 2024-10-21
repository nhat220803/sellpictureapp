//package com.example.sellpicture.activity.User;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.sellpicture.Context.Connection;
//import com.example.sellpicture.R;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class LoginActivity extends AppCompatActivity {
//    private EditText etUsername, etPassword;
//    private TextView tvRegisterLink;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        etUsername = findViewById(R.id.etUsername);
//        etPassword = findViewById(R.id.etPassword);
//        Button btnLogin = findViewById(R.id.btnLogin);
//        tvRegisterLink = findViewById(R.id.tvRegisterLink);
//
//        btnLogin.setOnClickListener(v -> {
//            String username = etUsername.getText().toString().trim();
//            String password = etPassword.getText().toString().trim();
//
//            if (username.isEmpty() || password.isEmpty()) {
//                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(LoginActivity.this, "Đang đăng nhập...", Toast.LENGTH_SHORT).show();
//                login(username, password);
//            }
//        });
//
//        tvRegisterLink.setOnClickListener(v -> {
//            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
//            startActivity(intent);
//        });
//    }
//
//    public void login(String username, String password) {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(() -> {
//            Connection connectionClass = new Connection();
//            java.sql.Connection conn = connectionClass.CONN();
//            if (conn == null) {
//                showToastMessage("Không thể kết nối tới database");
//            }
//
//            String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
//
//            try {
//                PreparedStatement stmt = conn.prepareStatement(sql);
//                stmt.setString(1, username);
//                stmt.setString(2, password);
//                ResultSet rs = stmt.executeQuery();
//
//                if (rs.next()) {
//                    showToastMessage("Đăng nhập thành công!");
//                    Intent intent = new Intent(LoginActivity.this, ProductList.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    showToastMessage("Tên người dùng hoặc mật khẩu không đúng.");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                Log.e("TAG123", "login: ", e);
//            } finally {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    Log.e("TAG123", "login: ", e);
//                }
//            }
//        });
//    }
//
//    private void showToastMessage(String message) {
//        runOnUiThread(() -> Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show());
//    }
//}

package com.example.sellpicture.activity.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private TextView tvRegisterLink;
    private CreateDatabase createDatabase;
    private CheckBox cbRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        


        createDatabase = new CreateDatabase(this);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
//        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false); // Kiểm tra xem đã lưu thông tin đăng nhập hay chưa

        if (rememberMe) {
            // Nếu tùy chọn lưu thông tin đăng nhập được chọn, hiển thị lại tên người dùng và mật khẩu
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");
            etUsername.setText(savedUsername);
            etPassword.setText(savedPassword);
            cbRememberMe.setChecked(true); // Đặt checkbox ở trạng thái được chọn
        }


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
            Intent intent = new Intent(LoginActivity.this, ProductList.class);
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
    private void saveLoginInfo(String username, String password, boolean rememberMe) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (rememberMe) {
            // Lưu tên người dùng và mật khẩu nếu checkbox được chọn
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putBoolean("rememberMe", true);
        } else {
            // Xóa thông tin đăng nhập nếu checkbox không được chọn
            editor.remove("username");
            editor.remove("password");
            editor.putBoolean("rememberMe", false);
        }

        editor.putBoolean("isLoggedIn", true);   // Đặt trạng thái đăng nhập là true
        editor.apply(); // Lưu lại thay đổi
        editor.commit();
    }
}
