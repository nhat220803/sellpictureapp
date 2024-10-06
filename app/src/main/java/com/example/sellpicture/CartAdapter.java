package com.example.sellpicture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> cartItems;
    private Context context;
    private OnCartItemClickListener listener;

    public interface OnCartItemClickListener {
        void onIncreaseClick(int position);
        void onDecreaseClick(int position);
        void onRemoveClick(int position);
    }

    public CartAdapter(List<Product> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    public void setOnCartItemClickListener(OnCartItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(String.format("$%.2f", product.getPrice()));
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productQuantity;
        ImageButton buttonDecrease;
        ImageButton buttonIncrease;
        ImageButton buttonRemove;

        CartViewHolder(@NonNull View itemView, final OnCartItemClickListener listener) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);

            buttonIncrease.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onIncreaseClick(position);
                    }
                }
            });

            buttonDecrease.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDecreaseClick(position);
                    }
                }
            });

            buttonRemove.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRemoveClick(position);
                    }
                }
            });
        }
    }
}
