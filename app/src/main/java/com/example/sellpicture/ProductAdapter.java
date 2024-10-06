package com.example.sellpicture;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onAddToCartClick(Product product);
    }

    public ProductAdapter(List<Product> productList, Context context, OnProductClickListener listener) {
        this.productList = productList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewProduct.setText(product.getProductName());
        holder.textViewPrice.setText(String.format("$%.2f", product.getPrice()));

        holder.buttonAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProduct;
        TextView textViewPrice;
        Button buttonAddToCart;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewProduct = itemView.findViewById(R.id.textViewProduct);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
        }
    }
}