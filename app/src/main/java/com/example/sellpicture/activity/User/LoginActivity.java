
package com.example.sellpicture.activity.User;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.sellpicture.R;
import com.example.sellpicture.activity.Admin.AddProductActivity;
import com.example.sellpicture.context.CreateDatabase;

public class LoginActivity extends AppCompatActivity {

    // Khai báo các thành phần giao diện
    private EditText etUsername, etPassword;
    private TextView tvRegisterLink,tvForgotPassword;
    private CreateDatabase createDatabase;
    private SharedPreferences sharedPreferences;
    private int userId;

    private CheckBox cbRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo thông báo
        createNotificationChannel();

        // Ánh xạ các thành phần giao diện
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);
        cbRememberMe = findViewById(R.id.cbSave);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        // Kiểm tra và tải thông tin đăng nhập từ SharedPreferences
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");
            etUsername.setText(savedUsername);
            etPassword.setText(savedPassword);
            cbRememberMe.setChecked(true);
        }

        checkNotificationPermission();

        createDatabase = new CreateDatabase(this);

        // Khởi tạo SharedPreferences
        //sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // Kiểm tra xem người dùng đã đăng nhập chưa
//        if (isLoggedIn()) {
//            redirectToMainActivity();
//        }

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
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CartChannel";
            String description = "Channel for Cart Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("CartChannelID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Hiển thị AlertDialog yêu cầu người dùng mở thông báo
                new AlertDialog.Builder(this)
                        .setTitle("Cần bật thông báo")
                        .setMessage("Để nhận thông báo về giỏ hàng, vui lòng bật thông báo trong cài đặt.")
                        .setPositiveButton("Mở cài đặt", (dialog, which) -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                            startActivity(intent);
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        }
    }


    private void login(String username, String password) {
        SQLiteDatabase db = createDatabase.open();

        String sql = "SELECT * FROM " + CreateDatabase.TB_users + " WHERE " + CreateDatabase.TB_users_username + " = ? AND " + CreateDatabase.TB_users_password + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});

        // Kiểm tra kết quả truy vấn
        if (cursor != null && cursor.moveToFirst()) {
            // Lưu thông tin đăng nhập nếu checkbox "Remember Me" được chọn
            saveLoginInfo(username, password, cbRememberMe.isChecked());
            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_users_user_id));
            String role = checkUserRole(userId);
//            Intent intent = new Intent(LoginActivity.this, ProductDetail.class);

            // Kiểm tra giỏ hàng và hiển thị thông báo nếu có sản phẩm
            checkCartAndNotify(userId);

            Intent intent2 = new Intent(LoginActivity.this, ProductList.class);
            // Lưu trạng thái đăng nhập vào SharedPreferences
            saveLoginState(userId, username, role);

            // Chuyển hướng dựa trên vai trò của người dùng
            Intent intent;
            Intent intent1;
            if ("Admin".equals(role)) {
                intent = new Intent(LoginActivity.this, AddProductActivity.class); // Thay thế bằng activity dành cho admin
            } else {
                intent1 = new Intent(LoginActivity.this, ProductList.class);
            }
            startActivity(intent2);
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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberMe) {
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putBoolean("rememberMe", true);
        } else {
            // Xóa thông tin đăng nhập nếu checkbox không được chọn
            editor.remove("username");
            editor.remove("password");
            editor.putBoolean("rememberMe", false);
        }
        editor.apply();
    }

    private void checkCartAndNotify(int userID) {
        int cartProductCount = getCartProductCount(userID); // Lấy số lượng sản phẩm trong giỏ hàng
        Log.d("CartNotification", "Số lượng sản phẩm trong giỏ hàng: " + userID + "," + cartProductCount); // Ghi log số lượng sản phẩm

        if (cartProductCount > 0) {
            // Tạo đối tượng Notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CartChannelID")
                    .setSmallIcon(R.drawable.ic_cart) // Đảm bảo bạn có icon phù hợp
                    .setContentTitle("Giỏ hàng của bạn")
                    .setContentText("Bạn có " + cartProductCount + " sản phẩm trong giỏ hàng.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH) // Đặt mức độ ưu tiên
                    .setAutoCancel(true);

            // Tạo Intent để mở CartActivity
            Intent intent = new Intent(this, CartActivity.class);
            intent.putExtra("cart_count", cartProductCount); // Nếu cần gửi thêm dữ liệu
            intent.putExtra("user_id", userId); // Truyền user_id vào intent

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);

            // Hiển thị thông báo
            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, notification); // 1 là ID thông báo

            Log.d("CartNotification", "Thông báo đã được gửi."); // Ghi log khi thông báo được gửi
        } else {
            Log.d("CartNotification", "Không có sản phẩm trong giỏ hàng để hiển thị thông báo.");
        }
    }



    // Phương thức để lấy số lượng sản phẩm trong giỏ hàng
    private int getCartProductCount(int userId) {
        int quantity = 0;
        SQLiteDatabase db = createDatabase.open();
        Cursor cursor = null;

        try {
            // Truy vấn để lấy tổng số lượng sản phẩm từ bảng cart_items
            String query = "SELECT SUM(ci.quantity) " +
                    "FROM cart_items ci " +
                    "JOIN cart c ON ci.cart_id = c.cart_id " +
                    "WHERE c.user_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                quantity = cursor.getInt(0); // Lấy tổng số lượng sản phẩm
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi kiểm tra giỏ hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return quantity;
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
//            intent = new Intent(LoginActivity.this, AddProductActivity.class);
//        } else {
//            intent = new Intent(LoginActivity.this, ProductList.class);
//        }
//        startActivity(intent);
//        finish();
//    }
}
