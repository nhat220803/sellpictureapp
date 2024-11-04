package com.example.sellpicture.activity.Admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.activity.User.LoginActivity;
import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.R;

public class AddProductActivity extends AppCompatActivity {
    private EditText etProductName, etDescription, etPrice, etStockQuantity, etImage;
    private Spinner spCategoryId;
    private Button btnAddProduct;
    private CreateDatabase createDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize views
        etProductName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etStockQuantity = findViewById(R.id.etStockQuantity);
        spCategoryId = findViewById(R.id.etCategoryId);
        etImage = findViewById(R.id.etImage);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        createDatabase = new CreateDatabase(this);

        // Populate Spinner with category options
        String[] categories = {"1: Phong cảnh", "2: Trừu tượng", "3: Hiện đại"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoryId.setAdapter(adapter);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewProduct();
            }
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

    private void addnewProduct() {
        String productName = etProductName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String stockQuantityStr = etStockQuantity.getText().toString().trim();
        String image = etImage.getText().toString().trim();
        String selectedCategory = spCategoryId.getSelectedItem().toString().split(":")[0].trim();

        // Check if fields are empty
        if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(description) || TextUtils.isEmpty(priceStr)
                || TextUtils.isEmpty(stockQuantityStr) || TextUtils.isEmpty(image)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = createDatabase.open();
        ContentValues value = new ContentValues();
        value.put(CreateDatabase.TB_products_product_name, productName);
        value.put(CreateDatabase.TB_products_description, description);
        value.put(CreateDatabase.TB_products_price, priceStr);
        value.put(CreateDatabase.TB_products_stock_quantity, stockQuantityStr);
        value.put(CreateDatabase.TB_products_category_id, selectedCategory);
        value.put(CreateDatabase.TB_products_image, image);

        long newRowId = db.insert(CreateDatabase.TB_products, null, value);
        if (newRowId != -1) {
            Toast.makeText(this, "Add successful", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show();
        }
    }
}
