package com.example.sellpicture.activity.Admin;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sellpicture.R;
import com.example.sellpicture.context.CreateDatabase;

import java.util.Random;

public class AddUserActivity extends AppCompatActivity {
    private EditText etUserName, etUserEmail, etUserPhone, etUseracc;
    private Button btnSaveUser;
    private CreateDatabase createDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        etUserName = findViewById(R.id.etUserName);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserPhone = findViewById(R.id.etUserPhone);
        etUseracc = findViewById(R.id.etUseracc);
        btnSaveUser = findViewById(R.id.btnSaveUser);

        createDatabase = new CreateDatabase(this);

        btnSaveUser.setOnClickListener(v -> saveUserToDatabase());
    }


//    private String generateRandomPassword() {
//        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        StringBuilder password = new StringBuilder();
//        Random rnd = new Random();
//        while (password.length() < 8) { // Độ dài mật khẩu là 8 ký tự
//            int index = (int) (rnd.nextFloat() * chars.length());
//            password.append(chars.charAt(index));
//        }
//        return password.toString();
//    }


    private void saveUserToDatabase() {
        String name = etUserName.getText().toString().trim();
        String email = etUserEmail.getText().toString().trim();
        String phone = etUserPhone.getText().toString().trim();
        String acc = etUseracc.getText().toString().trim();


        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || acc.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

//        String randomPassword = generateRandomPassword();
        String defaultPassword = "88888888";

        SQLiteDatabase db = createDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("username",acc);
        values.put("password",defaultPassword);

        long newRowId = db.insert(CreateDatabase.TB_users, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại ManageUsersActivity
        } else {
            Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
