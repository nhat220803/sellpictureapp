package com.example.sellpicture.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.model.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private CreateDatabase dbHelper;
    private SQLiteDatabase db;

    public CartManager(Context context) {
        dbHelper = new CreateDatabase(context);
        db = dbHelper.open();
    }

    public void addToCart(int productId, int quantity) {
        // Check if the product is already in the cart
        Cursor cursor = db.rawQuery("SELECT * FROM " + CreateDatabase.TB_cart_items +
                        " WHERE " + CreateDatabase.TB_cart_items_product_id + " = ?",
                new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            // Product is already in cart, update quantity
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_cart_items_quantity));
            int newQuantity = currentQuantity + quantity;
            db.execSQL("UPDATE " + CreateDatabase.TB_cart_items +
                            " SET " + CreateDatabase.TB_cart_items_quantity + " = ? " +
                            " WHERE " + CreateDatabase.TB_cart_items_product_id + " = ?",
                    new String[]{String.valueOf(newQuantity), String.valueOf(productId)});
        } else {
            // Product is not in cart, insert new item
            db.execSQL("INSERT INTO " + CreateDatabase.TB_cart_items +
                            " (" + CreateDatabase.TB_cart_items_product_id + ", " + CreateDatabase.TB_cart_items_quantity + ") " +
                            " VALUES (?, ?)",
                    new String[]{String.valueOf(productId), String.valueOf(quantity)});
        }
        cursor.close();
    }

    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT ci.*, p.* FROM " + CreateDatabase.TB_cart_items + " ci " +
                " JOIN " + CreateDatabase.TB_products + " p ON ci." + CreateDatabase.TB_cart_items_product_id +
                " = p." + CreateDatabase.TB_products_product_id, null);
        while (cursor.moveToNext()) {
            CartItem item = new CartItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_cart_items_cart_item_id)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_cart_items_product_id)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_product_name)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_price)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_cart_items_quantity)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_products_image))
            );
            cartItems.add(item);
        }
        cursor.close();
        return cartItems;
    }

    public void updateCartItemQuantity(int cartItemId, int newQuantity) {
        db.execSQL("UPDATE " + CreateDatabase.TB_cart_items +
                        " SET " + CreateDatabase.TB_cart_items_quantity + " = ? " +
                        " WHERE " + CreateDatabase.TB_cart_items_cart_item_id + " = ?",
                new String[]{String.valueOf(newQuantity), String.valueOf(cartItemId)});
    }

    public void removeCartItem(int cartItemId) {
        db.execSQL("DELETE FROM " + CreateDatabase.TB_cart_items +
                        " WHERE " + CreateDatabase.TB_cart_items_cart_item_id + " = ?",
                new String[]{String.valueOf(cartItemId)});
    }

    public void close() {
        db.close();
    }
}
