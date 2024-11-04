package com.example.sellpicture.activity.User;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellpicture.R;
import com.example.sellpicture.adapter.MessageAdapter;
import com.example.sellpicture.context.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class SupportChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_chat);

        recyclerView = findViewById(R.id.recyclerView);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

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

        Button btnQuestion1 = findViewById(R.id.btn_question1);
        Button btnQuestion2 = findViewById(R.id.btn_question2);
        Button btnQuestion3 = findViewById(R.id.btn_question3);
        Button btnQuestion4 = findViewById(R.id.btn_question4);

        btnQuestion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage("Tôi muốn hỏi địa chỉ mua hàng", true);
                addMessage("Bạn có thể mua hàng tại trang web: www.shop.com", false);
            }
        });

        btnQuestion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage("Tôi muốn tham gia cộng đồng Tranh", true);
                addMessage("Tham gia tại: www.facebook.com/tranh", false);
            }
        });

        btnQuestion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage("Địa chỉ trung tâm bảo hành", true);
                addMessage("Trung tâm bảo hành laptop tại: 123 Đường ABC, Thành phố XYZ.", false);
            }
        });

        btnQuestion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage("Địa chỉ trung tâm văn phòng", true);
                addMessage("Trung tâm văn phòng tại: 456 Đường DEF, Thành phố UVW.", false);
            }
        });


    }
    private void showMoreOptions() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.nav_more));
        popup.getMenuInflater().inflate(R.menu.more_options_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.shop_location) {
                // Xử lý khi chọn Shop Location
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


    private void addMessage(String content, boolean isUserMessage) {
        messageList.add(new Message(content, isUserMessage));
        messageAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageList.size() - 1); // Tự động cuộn đến tin nhắn mới nhất
    }
}
