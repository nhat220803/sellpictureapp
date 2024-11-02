package com.example.sellpicture.activity.User;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
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

        // Ánh xạ các thành phần giao diện
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        // Khởi tạo CreateDatabase
        createDatabase = new CreateDatabase(this);

        // Xử lý sự kiện khi người dùng nhấn nút đăng ký
        btnRegister.setOnClickListener(v -> registerUser());

        // Xử lý sự kiện khi người dùng nhấn liên kết đăng nhập
        tvLoginLink.setOnClickListener(v -> finish());

    }

    private void registerUser() {
        // Lấy dữ liệu từ các trường nhập liệu
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Kiểm tra các trường nhập liệu
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        // Kiểm tra định dạng email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        // Kiểm tra độ dài mật khẩu
        if (password.length() < 8) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra độ dài mật khẩu
        if (password.length() < 8) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu và xác nhận mật khẩu có trùng khớp
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thực hiện đăng ký người dùng
        SQLiteDatabase db = createDatabase.open();

// Tạo một đối tượng ContentValues để lưu trữ các giá trị cột cho bảng users
        ContentValues values = new ContentValues();

// Đưa username vào ContentValues với khóa là tên cột TB_users_username
        values.put(CreateDatabase.TB_users_username, username);

// Đưa password vào ContentValues với khóa là tên cột TB_users_password
        values.put(CreateDatabase.TB_users_password, password);

// Đưa email vào ContentValues với khóa là tên cột TB_users_email
        values.put(CreateDatabase.TB_users_email, email);

// Đưa tên đầy đủ của người dùng vào ContentValues với khóa là tên cột TB_users_full_name
        values.put(CreateDatabase.TB_users_full_name, fullName);

// Đặt giá trị role_id là 1, giả sử 1 đại diện cho quyền của người dùng bình thường
        values.put(CreateDatabase.TB_users_role_id, 2); // role_id = 2 cho người dùng bình thường


        // Thực hiện truy vấn INSERT để thêm người dùng mới vào bảng users
// Hàm insert trả về ID của hàng vừa được chèn hoặc -1 nếu chèn thất bại.
        long newRowId = db.insert(CreateDatabase.TB_users, null, values);

// Kiểm tra xem việc chèn có thành công không
        if (newRowId != -1) {
            // Nếu newRowId khác -1, tức là chèn thành công
            // Hiển thị thông báo "Đăng ký thành công"
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

            // Chuyển hướng người dùng đến LoginActivity
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));

            // Kết thúc SignupActivity để không quay lại được màn hình đăng ký
            finish();
        } else {
            // Nếu newRowId là -1, tức là chèn thất bại
            // Hiển thị thông báo "Đăng ký thất bại"
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        }

    }
}