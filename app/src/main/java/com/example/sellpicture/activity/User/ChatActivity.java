package com.example.sellpicture.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sellpicture.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {
    private EditText etMessage;
    private TextView tvMessages;
    private Button btnSend, btnClear;

    private String serverIP = "10.0.2.16"; // Đặt IP mặc định
    private static final int serverPort = 8080; // Đặt port mặc định
    private ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        etMessage = findViewById(R.id.etMessage);
        tvMessages = findViewById(R.id.tvMessages);
        btnSend = findViewById(R.id.btnSend);
        btnClear = findViewById(R.id.btnClear);
        executorService = Executors.newFixedThreadPool(2);

        // connect with server
        connectWithServer();

        btnClear.setOnClickListener(v -> tvMessages.setText(""));


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, ProductList.class)); // Chuyển về màn hình danh sách sản phẩm
                return true;

            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class)); // Chuyển về CartActivity
                return true;
            } else if (itemId == R.id.nav_profile) {
                 startActivity(new Intent(this, UserProfileActivity.class)); // Chuyển về UserProfileActivity
                return true;
            } else if (itemId == R.id.nav_more) {
                showMoreOptions();
                return true;
            }

            return false;
        });
    }


    private void showMoreOptions() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.nav_more));
        popup.getMenuInflater().inflate(R.menu.more_options_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.shop_location) {

                startActivity(new Intent(this, MapActivity.class));
            } else if (item.getItemId() == R.id.chat_with_shop){
                startActivity(new Intent(this, ChatActivity.class));

            }else if (item.getItemId() == R.id.support_chat ) {
                startActivity(new Intent(this,SupportChatActivity.class));
            }
            else if (item.getItemId() == R.id.call ) {
                startActivity(new Intent(this, CallActivity.class));
            }
            return false;
        });

        popup.show();
    }

    public void connectWithServer() {
        executorService.execute(() -> {
            try {
                // Tạo kết nối với server
                Socket socket = new Socket(serverIP, serverPort);

                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                // get message from server
                getMessages(socket);

                // event
                btnSend.setOnClickListener(v -> {
                    String message = etMessage.getText().toString().trim();
                    if (!message.isEmpty()) {
                        sendMessage(message, output);
                        etMessage.setText("");
                    } else {
                        Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                // If connect fail
                String errorMessage = "Connection failed: " + e.getMessage();
                runOnUiThread(() -> {
                    Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    tvMessages.append(errorMessage + "\n");
                });
                e.printStackTrace();
            }
        });
    }

    public void sendMessage(String message, PrintWriter output) {
        executorService.execute(() -> {
            if (output != null) {
                output.println(message);
                output.flush();
                runOnUiThread(() -> tvMessages.append("Me: " + message + "\n"));
            } else {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Not connected to server", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void getMessages(Socket socket) {
        executorService.execute(() -> {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = input.readLine()) != null) {
                    final String serverMessage = message;
                    // Cập nhật giao diện
                    runOnUiThread(() -> {
                        tvMessages.append("Admin: " + serverMessage + "\n");
                        Log.d("Client", "Received message: " + serverMessage); // Thêm log để kiểm tra
                    });
                }

                // Kiểm tra nếu client ngắt kết nối
                runOnUiThread(() -> tvMessages.append("Disconnected from server\n"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
