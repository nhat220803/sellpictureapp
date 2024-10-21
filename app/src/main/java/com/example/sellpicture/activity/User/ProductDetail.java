//package com.example.sellpicture.activity.User;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.sellpicture.Context.CreateDatabase;
//import com.example.sellpicture.R;
//import com.example.sellpicture.model.Product;
//
//public class ProductDetail extends AppCompatActivity {
//    private CreateDatabase createDatabase;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product_detail);
//        createDatabase = new CreateDatabase(this);
//
////        // Nhận productId từ Intent (hoặc bạn có thể lấy từ nguồn khác)
////        Intent intent = getIntent();
////        int productId = intent.getIntExtra("productId", -1);
////
////        // Kiểm tra productId hợp lệ và gọi getProduct() để lấy dữ liệu sản phẩm
////        if (productId != -1) {
////            Product product = getProduct(productId);  // Lấy sản phẩm theo ID
////            if (product != null) {
////                displayProduct(product);  // Hiển thị sản phẩm nếu tồn tại
////            }
////        }
//        int productId = 1; // Bạn có thể thay đổi giá trị này thành ID thực tế
//        Product product = getProduct(productId);
//        displayProduct(product);
//
//    }
//
//    public void displayProduct(Product product) {
//        TextView titleTextView = findViewById(R.id.detail_Title);
//        TextView descriptionTextView = findViewById(R.id.detail_Des);
//        TextView priceTextView = findViewById(R.id.detail_Price);
//        TextView quantityTextView = findViewById(R.id.detail_Quantity);
//
//        titleTextView.setText(product.getName());
//        descriptionTextView.setText(product.getDescription());
//        priceTextView.setText(String.valueOf(product.getPrice()));
//        quantityTextView.setText(String.valueOf(product.getStockQuantity()));
//    }
//
//    public Product getProduct(int id) {
//        SQLiteDatabase db = createDatabase.open();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + CreateDatabase.TB_products + " WHERE product_id = ?", new String[]{String.valueOf(id)});
//        Product product = null;
//        if (cursor.moveToFirst()) {
//            do {
//                // 4. Lấy dữ liệu từ từng cột
//                String name = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
//                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
//                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
//                int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock_quantity"));
//                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
//
//                // 5. Bạn có thể xử lý dữ liệu ở đây, ví dụ hiển thị ra log hoặc hiển thị lên giao diện
//                product = new Product(id, name, description, price, stock, image);
//
//                // 6. Duyệt đến bản ghi tiếp theo
//            } while (cursor.moveToNext());
//        }
//
//        // 7. Đóng Cursor và cơ sở dữ liệu sau khi sử dụng xong
//        cursor.close();
//        db.close();
//
//        // 8. Trả về sản phẩm đã lấy được
//        return product;
//
//    }
//}

package com.example.sellpicture.activity.User;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide; // Import Glide
import com.example.sellpicture.adapter.ViewPager2Adapter;
import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.R;
import com.example.sellpicture.model.Product;
import com.example.sellpicture.util.CartManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductDetail extends AppCompatActivity {

    private TextView productName, productDescription, productPrice, productQuantity;
    private ImageView productImage;
    private Button purchaseButton, viewCartButton;
    private CreateDatabase dbHelper;
    private int productId;
    private CartManager cartManager;
    private ViewPager2 pagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail); // Ensure you're using the correct layout

        // Initialize views
        productName = findViewById(R.id.detail_Title);
        productDescription = findViewById(R.id.detail_Des);
        productPrice = findViewById(R.id.detail_Price);
        productQuantity = findViewById(R.id.detail_Quantity);
        productImage = findViewById(R.id.detail_Image);
        purchaseButton = findViewById(R.id.detail_Purchase);
        pagerFragment = findViewById(R.id.viewpager2_fragment);



        // Thêm xử lý cho BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                pagerFragment.setCurrentItem(0);
//                startActivity(new Intent(this, ProductList.class)); // Chuyển về màn hình danh sách sản phẩm
                return true;
            } else if (itemId == R.id.nav_search) {
                pagerFragment.setCurrentItem(1);
//                showSearchBar(); // Hiển thị thanh tìm kiếm
                return true;
            } else if (itemId == R.id.nav_cart) {
                pagerFragment.setCurrentItem(2);
//                startActivity(new Intent(this, CartActivity.class)); // Chuyển về CartActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_profile) {
                pagerFragment.setCurrentItem(3);
//                 startActivity(new Intent(this, UserProfileActivity.class)); // Chuyển về UserProfileActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_more) {
                pagerFragment.setCurrentItem(4);
//                showMoreOptions(); // Hiển thị thêm tùy chọn
                return true;
            }

            return false;
        });

        pagerFragment.setAdapter(new ViewPager2Adapter(this));
        pagerFragment.setCurrentItem(0);
        pagerFragment.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int itemId ;
                if (position ==0){
                    itemId = R.id.nav_home;
                } else if (position==1) {
                    itemId = R.id.nav_search;
                } else if (position==2) {
                    itemId = R.id.nav_cart;
                } else if (position==3) {
                    itemId = R.id.nav_profile;
                } else if (position==4) {
                    itemId = R.id.nav_more;
                }else {
                    itemId = R.id.nav_home;
                }
                bottomNavigationView.setSelectedItemId(itemId);
            }
        });


//        ImageButton backButton = findViewById(R.id.detail_Back);
//        viewCartButton = findViewById(R.id.detail_ViewCart);

        // Set click listener for the back button
//        backButton.setOnClickListener(v -> {// This will close the current activity and return to the previous one
//            Intent prolistintent = new Intent(ProductDetail.this, ProductList.class); // Điều hướng đến trang CartActivity
//            startActivity(prolistintent);
//        });

        // Initialize database helper
        dbHelper = new CreateDatabase(this);
        cartManager = new CartManager(this);

        // Get product ID from Intent
        Intent intent = getIntent();
        productId = intent.getIntExtra("product_id", -1);
        if (productId != -1) {
            loadProductDetails(productId);
        }

        // Set up purchase button click listener
//        purchaseButton.setOnClickListener(v -> {
//            addToCart();
//        });

        // Set up view cart button click listener
//        viewCartButton.setOnClickListener(v -> {
//            Intent cartIntent = new Intent(ProductDetail.this, CartActivity.class); // Điều hướng đến trang CartActivity
//            startActivity(cartIntent);
//        });
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
                Toast.makeText(this, "Shop Location được chọn", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        popup.show();
    }

    // Hàm để hiển thị thanh tìm kiếm
    private void showSearchBar() {
        // Xử lý hiển thị thanh tìm kiếm ở đây
        Toast.makeText(this, "Thanh tìm kiếm được hiển thị", Toast.LENGTH_SHORT).show();
    }

    //    private void addToCart() {
//        cartManager.addToCart(productId, 1); // Add 1 quantity of the product
//        Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show();
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartManager.close();
    }

    private void loadProductDetails(int productId) {
        SQLiteDatabase db = dbHelper.open();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CreateDatabase.TB_products + " WHERE " + CreateDatabase.TB_products_product_id + "=?", new String[]{String.valueOf(productId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Create a Product object from cursor
            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_product_id)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_product_name)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_description)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_price)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_stock_quantity)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_image)) // Get image from DB
            );

            // Update UI with product details
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(String.format("$%.2f", product.getPrice()));
            productQuantity.setText(String.valueOf(product.getStockQuantity()));

            // Load image into ImageView using Glide
            Glide.with(this)
                    .load(product.getImage())
                    .into(productImage);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }


}





