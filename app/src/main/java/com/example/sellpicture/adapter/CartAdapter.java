package com.example.sellpicture.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sellpicture.R;
import com.example.sellpicture.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems = new ArrayList<>();
    private Context context;
    private CartItemListener listener;

    public CartAdapter(Context context, CartItemListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

//    public double getTotalPrice() {
//        double total = 0;
//        for (CartItem item : cartItems) {
//            total += item.getTotalPrice();
//        }
//        return total;
//    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            // Cập nhật giá tổng dựa trên giá sản phẩm và số lượng
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, minusButton, addButton, deleteButton;
        TextView productName, productPrice, quantityText;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.item_cart_image);
            productName = itemView.findViewById(R.id.item_cart_productname);
            productPrice = itemView.findViewById(R.id.item_cart_price);
            quantityText = itemView.findViewById(R.id.item_cart_quantity);
            minusButton = itemView.findViewById(R.id.item_cart_minus);
            addButton = itemView.findViewById(R.id.item_cart_add);
            deleteButton = itemView.findViewById(R.id.item_cart_delete);
        }

        void bind(CartItem item) {
            productName.setText(item.getProductName());
            productPrice.setText(String.format("$%.2f", item.getPrice()));
            quantityText.setText(String.valueOf(item.getQuantity()));

            Glide.with(context)
                    .load(item.getImage())
                    .into(productImage);

            minusButton.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    listener.onQuantityChanged(item.getId(), item.getQuantity() - 1);
                }
            });

            addButton.setOnClickListener(v -> {
                listener.onQuantityChanged(item.getId(), item.getQuantity() + 1);
            });

            deleteButton.setOnClickListener(v -> {
                listener.onItemRemoved(item.getId());
            });
        }
    }

    public interface CartItemListener {
        void onQuantityChanged(int cartItemId, int newQuantity);
        void onItemRemoved(int cartItemId);
    }
}
