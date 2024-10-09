package com.example.sellpicture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private Connection connectionClass;
    private TextView txtResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
//        finish();
        setContentView(R.layout.activity_main);

        txtResult = findViewById(R.id.textView);

        // Khởi tạo lớp kết nối và DatabaseHelper
        connectionClass = new Connection();
//        databaseHelper = new DatabaseHelper();

        // Kết nối và lấy dữ liệu
        fetchDataFromMySQL();
        EdgeToEdge.enable(this);
        setContentView(R.layout.product_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Hàm này sẽ thực hiện việc kết nối và gọi hàm để lấy dữ liệu
    private void fetchDataFromMySQL() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                java.sql.Connection conn = connectionClass.CONN(); // Kết nối tới database
                if (conn != null) {
                    showToastMessage("Kết nối thành công");
                } else {
                    showToastMessage("Không thể kết nối tới database");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToastMessage("Đã xảy ra lỗi: " + e.getMessage());
            }
        });
    }

    // Hàm để hiển thị thông báo bằng Toast
    private void showToastMessage(String message) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    // Hàm để cập nhật TextView với dữ liệu từ cơ sở dữ liệu
    private void updateUIWithResult(String result) {
        runOnUiThread(() -> txtResult.setText(result));
    }





}

