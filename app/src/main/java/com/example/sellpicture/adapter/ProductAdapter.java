
package com.example.sellpicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sellpicture.R;
import com.example.sellpicture.activity.User.ProductDetail;
import com.example.sellpicture.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context,List<Product> productList) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("Giá: " + product.getPrice() + " VND");

        // Tải hình ảnh bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetail.class);
            intent.putExtra("product_id", product.getId()); // Truyền id sản phẩm
            intent.putExtra("product_name", product.getName()); // Truyền tên sản phẩm
            intent.putExtra("product_description", product.getDescription()); // Truyền mô tả sản phẩm
            intent.putExtra("product_price", product.getPrice()); // Truyền giá sản phẩm
            intent.putExtra("product_image", product.getImage()); // Truyền đường dẫn hình ảnh sản phẩm
            context.startActivity(intent); // Khởi động activity ProductDetail
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}

