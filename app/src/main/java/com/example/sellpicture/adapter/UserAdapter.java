package com.example.sellpicture.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellpicture.R;
import com.example.sellpicture.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;
    private OnUserDeleteListener deleteListener;

    public interface OnUserDeleteListener {
        void onUserDelete(int userId,int position);
    }

    public UserAdapter(Context context, List<User> userList,OnUserDeleteListener deleteListener) {
        this.context = context;
        this.userList = userList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewEmail.setText(user.getEmail());
        holder.textViewPhone.setText(user.getPhone());
        holder.btnDeleteUser.setOnClickListener(v -> deleteListener.onUserDelete(user.getUser_id(), position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewEmail, textViewPhone;
        ImageButton btnDeleteUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}


