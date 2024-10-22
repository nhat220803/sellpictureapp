
package com.example.sellpicture.activity.User;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
        createNotificationChannel();
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);
        checkNotificationPermission();

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

        if (cursor != null && cursor.moveToFirst()) {
            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            // Kiểm tra giỏ hàng và hiển thị thông báo nếu có sản phẩm
            checkCartAndNotify();

            Intent intent = new Intent(LoginActivity.this, ProductList.class);
            String role = checkUserRole(username); // Kiểm tra vai trò

            Intent intent1;
            if ("Admin".equals(role)) {
                intent1 = new Intent(LoginActivity.this, AddProductActivity.class); // Thay thế bằng activity dành cho admin
            } else {
                intent1 = new Intent(LoginActivity.this, ProductList.class);
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

    private void checkCartAndNotify() {
        int cartProductCount = getCartProductCount(); // Lấy số lượng sản phẩm trong giỏ hàng
        Log.d("CartNotification", "Số lượng sản phẩm trong giỏ hàng: " + cartProductCount); // Ghi log số lượng sản phẩm

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
    private int getCartProductCount() {
        int quantity = 0;
        SQLiteDatabase db = createDatabase.open();
        Cursor cursor = null;

        try {
            // Truy vấn để lấy tổng số lượng sản phẩm từ bảng cart_items
            cursor = db.rawQuery("SELECT SUM(quantity) FROM cart_items WHERE cart_item_id IS NOT NULL", null);

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
