

package com.example.sellpicture.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.R;
import com.example.sellpicture.adapter.ProductAdapter;
import com.example.sellpicture.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class ProductList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private CreateDatabase createDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this,productList);
        recyclerView.setAdapter(productAdapter);

        createDatabase = new CreateDatabase(this);


        loadProductsFromDatabase();
        // Thêm xử lý cho BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, ProductList.class)); // Chuyển về màn hình danh sách sản phẩm
                return true;

            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class)); // Chuyển về CartActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_profile) {
                 startActivity(new Intent(this, UserProfileActivity.class)); // Chuyển về UserProfileActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_more) {
                showMoreOptions(); // Hiển thị thêm tùy chọn
                return true;
            }

            return false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            finishAffinity();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return true;
    }

    private void showMoreOptions() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.nav_more));
        popup.getMenuInflater().inflate(R.menu.more_options_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.phone_shop) {
                // Xử lý khi chọn Phone Shop
                Toast.makeText(this, "Phone Shop được chọn", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.shop_location) {
                // Xử lý khi chọn Shop Location
                startActivity(new Intent(this, MapActivity.class));
            } else if (item.getItemId() == R.id.chat_with_shop){
                startActivity(new Intent(this, ChatActivity.class));

            }else if (item.getItemId() == R.id.support_chat ) {
                   startActivity(new Intent(this,SupportChatActivity.class));
            }
            return false;
        });

        popup.show();
    }



    private void loadProductsFromDatabase() {
        SQLiteDatabase db = createDatabase.open();
        String query = "SELECT product_id, product_name, description, price, stock_quantity, image FROM " + CreateDatabase.TB_products;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id")); // Dùng getColumnIndexOrThrow
                String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock_quantity"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));

                Product product = new Product(id, name, description, price, stock, image);
                productList.add(product);
            } while (cursor.moveToNext());

            productAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(ProductList.this, "Không có sản phẩm nào!", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();



    }

}

