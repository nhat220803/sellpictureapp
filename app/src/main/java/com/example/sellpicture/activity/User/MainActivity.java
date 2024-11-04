//package com.example.sellpicture.activity.User;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.sellpicture.context.Connection;
//import com.example.sellpicture.R;
//
//public class MainActivity extends AppCompatActivity {
//
//    private Connection connectionClass;
//    private TextView txtResult;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Tạo kênh thông báo khi ứng dụng khởi động
//
//
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);
//
//        setContentView(R.layout.activity_main);
//        txtResult = findViewById(R.id.textView);
//
//        connectionClass = new Connection();
//        fetchDataFromMySQL();
//    }
//
//
//
//    private void fetchDataFromMySQL() {
//        // Kết nối và lấy dữ liệu từ MySQL
//    }
//}
