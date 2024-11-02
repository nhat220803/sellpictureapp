package com.example.sellpicture.activity.Admin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
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
    private CreateDatabase createDatabase;
    private ImageButton btnAddUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        btnAddUser = findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(v -> {
            Intent intent = new Intent(ManageUsersActivity.this, AddUserActivity.class);
            startActivity(intent);
        });


        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList, (userId, position) -> {
            deleteUserById(userId, position);
        });
        recyclerViewUsers.setAdapter(userAdapter);

        createDatabase = new CreateDatabase(this);

        loadUsersListFromDatabase();
    }



    private void deleteUserById(int userId, int position) {
        SQLiteDatabase db = createDatabase.getWritableDatabase();

        // Xóa người dùng bằng user_id
        int rowsDeleted = db.delete(CreateDatabase.TB_users, "user_id = ?", new String[]{String.valueOf(userId)});

        if (rowsDeleted > 0) {
            userList.remove(position);
            userAdapter.notifyItemRemoved(position);
            Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User deletion failed", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadUsersListFromDatabase() {
        SQLiteDatabase db = createDatabase.open();
        String query = "SELECT " + CreateDatabase.TB_users_full_name + ", " + CreateDatabase.TB_users_email + ", " + CreateDatabase.TB_users_phone + ", " +CreateDatabase.TB_users_user_id + " FROM " + CreateDatabase.TB_users;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));

                User user = new User(id,name,email,phone);
                userList.add(user);
            }while(cursor.moveToNext());

            userAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(ManageUsersActivity.this,"No users found",Toast.LENGTH_SHORT);
        }

        if(cursor != null){
            cursor.close();
        }
        db.close();
    }
    @Override
    protected void onResume() {
        super.onResume();
        userList.clear();
        loadUsersListFromDatabase();
    }

}
