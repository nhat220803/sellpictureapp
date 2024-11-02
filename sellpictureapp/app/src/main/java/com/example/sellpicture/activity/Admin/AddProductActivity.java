package com.example.sellpicture.activity.Admin;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.R;

public class AddProductActivity extends AppCompatActivity {
    private EditText etProductName, etDescription, etPrice, etStockQuantity, etCategoryId, etImage;
    private Button btnAddProduct;
    private CreateDatabase createDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Khởi tạo các view
        etProductName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etStockQuantity = findViewById(R.id.etStockQuantity);
        etCategoryId = findViewById(R.id.etCategoryId);
        etImage = findViewById(R.id.etImage);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        createDatabase = new CreateDatabase(this);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewProduct();
            }

        });
    }

    private void addnewProduct() {
        String productName = etProductName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String stockQuantityStr = etStockQuantity.getText().toString().trim();
        String categoryIdStr = etCategoryId.getText().toString().trim();
        String image = etImage.getText().toString().trim();

        // Kiểm tra xem các trường có bị trống không
        if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(description) || TextUtils.isEmpty(priceStr)
                || TextUtils.isEmpty(stockQuantityStr) || TextUtils.isEmpty(categoryIdStr) || TextUtils.isEmpty(image)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        SQLiteDatabase db = createDatabase.open();
        ContentValues value = new ContentValues();
        value.put(CreateDatabase.TB_products_product_name,productName);
        value.put(CreateDatabase.TB_products_description,description);
        value.put(CreateDatabase.TB_products_price,priceStr);
        value.put(CreateDatabase.TB_products_stock_quantity,stockQuantityStr);
        value.put(CreateDatabase.TB_products_category_id,categoryIdStr);
        value.put(CreateDatabase.TB_products_image,image);

        long newRowId = db.insert(CreateDatabase.TB_products, null, value);
        if (newRowId != -1) {
            Toast.makeText(this, "add Successful", Toast.LENGTH_SHORT).show();
            // Chuyển hướng đến trang đăng nhập hoặc trang chính
//            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//            finish();
        } else {
            Toast.makeText(this, "add Failed", Toast.LENGTH_SHORT).show();
        }

    }
}
