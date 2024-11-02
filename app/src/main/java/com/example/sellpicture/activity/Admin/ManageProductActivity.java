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
import com.example.sellpicture.adapter.ManageProAdapter;
import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ManageProductActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private ManageProAdapter productAdapter;
    private List<Product> productList;
    private CreateDatabase createDatabase;
    private ImageButton btnAddProduct, btnRefresh ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        productAdapter = new ManageProAdapter(this, productList, ((productId, position) -> {
            deleteProductById(productId, position);
        }));

        recyclerViewProducts.setAdapter(productAdapter);

        createDatabase = new CreateDatabase(this);

        DisplayListProduct();

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(view -> {
            Intent intent = new Intent(ManageProductActivity.this, AddProductActivity.class);
            startActivity(intent);

        });
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(view -> {
            productList.clear(); // Clear current list to refresh with new data
            DisplayListProduct();
        });

    }

    private void deleteProductById(int productId, int position) {
        SQLiteDatabase db = createDatabase.open();

        int rowsDeleted = db.delete(CreateDatabase.TB_products, "product_id = ?", new String[]{String.valueOf(productId)});

        if (rowsDeleted > 0) {
            productList.remove(position);
            productAdapter.notifyItemRemoved(position);
            Toast.makeText(ManageProductActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ManageProductActivity.this, "Product not deleted", Toast.LENGTH_SHORT).show();
        }
    }

    private void DisplayListProduct() {
        SQLiteDatabase db = createDatabase.open();
        String query = "SELECT product_id, product_name, price, stock_quantity, image FROM " + CreateDatabase.TB_products;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock_quantity"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));

                Product product = new Product(id, name, price, stock, image);
                productList.add(product);
            } while (cursor.moveToNext());

            productAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(ManageProductActivity.this, "No products found", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        productList.clear();
        DisplayListProduct();
    }
}
