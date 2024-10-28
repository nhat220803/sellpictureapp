package com.example.sellpicture.activity.User;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sellpicture.R;
import com.example.sellpicture.context.CreateDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfileActivity extends AppCompatActivity {
    private TextView tvFullName, tvUsername, tvEmail, tvPhone, tvRole;
    private CreateDatabase createDatabase;
    private SharedPreferences sharedPreferences;
    private Button buttonEdit;

    private boolean isEditting = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvRole = findViewById(R.id.tvRole);
        buttonEdit = findViewById(R.id.btnEdit);
        createDatabase = new CreateDatabase(this);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        getUserInfo(sharedPreferences.getString("username", ""));

        // Thêm xử lý cho BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, ProductList.class)); // Chuyển về màn hình danh sách sản phẩm
                return true;

            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class)); // Chuyển về CartActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, UserProfileActivity.class)); // Chuyển về UserProfileActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_more) {
                showMoreOptions(); // Hiển thị thêm tùy chọn
                return true;
            }

            return false;
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditting) {
                    String username = tvUsername.getText().toString().trim();
                    String fullname = tvFullName.getText().toString().trim();
                    String email = tvEmail.getText().toString().trim();
                    String phone = tvPhone.getText().toString().trim();

                    if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                        Toast.makeText(UserProfileActivity.this, "Chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                    }else {
                        updateUserInfo(username, fullname, email, phone);
                    }

                } else {
                    tvFullName.setEnabled(true);
                    tvEmail.setEnabled(true);
                    tvPhone.setEnabled(true);
                    isEditting = true;
                    buttonEdit.setText("Lưu thay đổi");
                }
            }
        });


    }
    private void showMoreOptions() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.nav_more));
        popup.getMenuInflater().inflate(R.menu.more_options_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.phone_shop) {
                // Xử lý khi chọn Phone Shop
                Toast.makeText(this, "Phone Shop được chọn", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.shop_location) {
                // Xử lý khi chọn Shop Location
                Toast.makeText(this, "Shop Location được chọn", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.chat_with_shop){
                startActivity(new Intent(this, ChatActivity.class));

            }
            return false;
        });

        popup.show();
    }
    private void getUserInfo(String username) {
        SQLiteDatabase db = createDatabase.open();

        // Truy vấn dữ liệu từ bảng Users dựa trên tên đăng nhập (username)
        String[] columns = {
                CreateDatabase.TB_users_full_name,
                CreateDatabase.TB_users_username,
                CreateDatabase.TB_users_password,
                CreateDatabase.TB_users_email,
                CreateDatabase.TB_users_phone,
                CreateDatabase.TB_users_role_id
        };

        String selection = CreateDatabase.TB_users_username + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(CreateDatabase.TB_users, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Lấy các thông tin người dùng từ Cursor
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_users_full_name));
            String userUsername = cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_users_username));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_users_email));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_users_phone));
            int roleId = cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_users_role_id));

            // Cập nhật giao diện với thông tin người dùng
            tvFullName.setText(fullName);
            tvUsername.setText(userUsername);
            tvEmail.setText(email);
            tvPhone.setText(phone);
            tvRole.setText(getRoleNameById(roleId)); // Hiển thị vai trò người dùng

        } else {
            Toast.makeText(this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    private String getRoleNameById(int roleId) {
        // Bạn có thể trả về tên của vai trò dựa trên role_id
        switch (roleId) {
            case 1:
                return "Admin";
            case 2:
                return "User";
            default:
                return "Unknown";
        }
    }

    private void updateUserInfo(String username, String newFullName, String newEmail, String newPhone) {
        SQLiteDatabase db = createDatabase.open();

        // Tạo đối tượng ContentValues để lưu trữ các cặp key-value
        ContentValues values = new ContentValues();
        values.put(CreateDatabase.TB_users_full_name, newFullName);
        values.put(CreateDatabase.TB_users_email, newEmail);
        values.put(CreateDatabase.TB_users_phone, newPhone);

        // Điều kiện WHERE để xác định người dùng cần cập nhật
        String selection = CreateDatabase.TB_users_username + " = ?";
        String[] selectionArgs = {username};

        // Cập nhật dữ liệu
        int count = db.update(
                CreateDatabase.TB_users,   // Tên bảng
                values,                    // Dữ liệu cần cập nhật
                selection,                 // Điều kiện WHERE
                selectionArgs              // Điều kiện WHERE dưới dạng mảng
        );

        if (count > 0) {
            tvEmail.setEnabled(false);
            tvFullName.setEnabled(false);
            tvPhone.setEnabled(false);
            isEditting = false;
            buttonEdit.setText("Sửa thong tin");
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cập nhật không thành công!", Toast.LENGTH_SHORT).show();
        }

        db.close(); // Đóng kết nối với cơ sở dữ liệu sau khi cập nhật
    }
}