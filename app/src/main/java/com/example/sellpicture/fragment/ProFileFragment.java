package com.example.sellpicture.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sellpicture.R;
import com.example.sellpicture.context.CreateDatabase;


public class ProFileFragment extends Fragment {
    private TextView tvFullName, tvUsername, tvEmail, tvPhone, tvRole;
    private CreateDatabase createDatabase;
    private SharedPreferences sharedPreferences;
    private Button buttonEdit;

    private boolean isEditting = false;

    public ProFileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Ánh xạ các view
        View view = inflater.inflate(R.layout.fragment_pro_file, container, false);

        tvFullName = view.findViewById(R.id.tvFullName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvRole = view.findViewById(R.id.tvRole);
        buttonEdit = view.findViewById(R.id.btnEdit);
        createDatabase = new CreateDatabase(requireContext());
        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        getUserInfo(sharedPreferences.getString("username", ""));

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditting) {
                    String username = tvUsername.getText().toString().trim();
                    String fullname = tvFullName.getText().toString().trim();
                    String email = tvEmail.getText().toString().trim();
                    String phone = tvPhone.getText().toString().trim();

                    if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                        Toast.makeText(requireContext(), "Chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
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

        return view;
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
            Toast.makeText(requireContext(), "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Cập nhật không thành công!", Toast.LENGTH_SHORT).show();
        }

        db.close(); // Đóng kết nối với cơ sở dữ liệu sau khi cập nhật
    }

}