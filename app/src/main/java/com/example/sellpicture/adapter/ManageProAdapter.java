package com.example.sellpicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sellpicture.R;
import com.example.sellpicture.activity.Admin.AddProductActivity;
import com.example.sellpicture.model.Product;

import java.util.List;

public class ManageProAdapter extends RecyclerView.Adapter<ManageProAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnProductDeleteListener deleteListener;

    public interface OnProductDeleteListener {
        void onDeleteProduct(int productId, int position);
    }

    public ManageProAdapter(Context context, List<Product> productList, OnProductDeleteListener deleteListener) {
        this.context = context;
        this.productList = productList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(String.format("Price: $%.2f", product.getPrice()));
        holder.tvProductQuantity.setText(String.format("Quantity: %d", product.getStockQuantity()));
        holder.btnDeleteProduct.setOnClickListener(v -> deleteListener.onDeleteProduct(product.getId(), position));

        // Tải hình ảnh bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.imgProduct);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddProductActivity.class);
            intent.putExtra("product_id", product.getId());
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_description", product.getDescription());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_image", product.getImage());
            context.startActivity(intent);
        });

    }



    @Override
    public int getItemCount() {
        return productList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductQuantity;
        ImageButton btnDeleteProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            btnDeleteProduct = itemView.findViewById(R.id.btnDelete);
        }
    }
}
