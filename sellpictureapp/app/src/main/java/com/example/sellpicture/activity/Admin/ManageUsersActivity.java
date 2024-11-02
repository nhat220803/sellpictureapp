package com.example.sellpicture.activity.Admin;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellpicture.R;
import com.example.sellpicture.adapter.UserAdapter;
import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.model.User;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private CreateDatabase database;
    private ImageButton btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        // Initialize database
        database = new CreateDatabase(this);

        // Initialize views
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        btnAddUser = findViewById(R.id.btnAddUser);

        // Setup RecyclerView
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(userAdapter);

        // Load users
        loadUsers();

        // Add user button click listener
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddUserActivity
                // Intent intent = new Intent(UserManagementActivity.this, AddUserActivity.class);
                // startActivity(intent);
                Toast.makeText(ManageUsersActivity.this, "Add User clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUsers() {
        // TODO: Implement database query to load users
        // Example dummy data
        userList.add(new User(1, "John Doe", "john@example.com", "1234567890", "john_doe", "password123", 1, null));
        userList.add(new User(2, "Jane Smith", "jane@example.com", "0987654321", "jane_smith", "password456", 2, null));
        userAdapter.notifyDataSetChanged();
    }
}
