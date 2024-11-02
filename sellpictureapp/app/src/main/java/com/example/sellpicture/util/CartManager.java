package com.example.sellpicture.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.sellpicture.context.CreateDatabase;
import com.example.sellpicture.model.CartItem;
import com.example.sellpicture.model.Order;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private CreateDatabase dbHelper;
    private SQLiteDatabase db;

    public CartManager(Context context) {
        dbHelper = new CreateDatabase(context);
        db = dbHelper.open();
    }

    // Hàm thêm sản phẩm vào giỏ hàng
    public void addToCart(int userId, int productId, int quantity) {
        // Kiểm tra xem giỏ hàng của người dùng đã tồn tại chưa
        Cursor cartCursor = db.rawQuery("SELECT " + CreateDatabase.TB_cart_cart_id + " FROM " + CreateDatabase.TB_cart +
                " WHERE " + CreateDatabase.TB_cart_user_id + " = ?", new String[]{String.valueOf(userId)});

        int cartId;
        if (cartCursor.moveToFirst()) {
            // Giỏ hàng đã tồn tại
            cartId = cartCursor.getInt(cartCursor.getColumnIndexOrThrow(CreateDatabase.TB_cart_cart_id));
        } else {
            // Tạo giỏ hàng mới
            ContentValues cartValues = new ContentValues();
            cartValues.put(CreateDatabase.TB_cart_user_id, userId);
            cartId = (int) db.insert(CreateDatabase.TB_cart, null, cartValues);
        }
        cartCursor.close();

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Cursor cursor = db.rawQuery("SELECT * FROM " + CreateDatabase.TB_cart_items +
                        " WHERE " + CreateDatabase.TB_cart_items_cart_id + " = ? AND " +
                        CreateDatabase.TB_cart_items_product_id + " = ?",
                new String[]{String.valueOf(cartId), String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            // Sản phẩm đã có trong giỏ hàng, cập nhật số lượng
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(CreateDatabase.TB_cart_items_quantity));
            int newQuantity = currentQuantity + quantity;
            db.execSQL("UPDATE " + CreateDatabase.TB_cart_items +
                            " SET " + CreateDatabase.TB_cart_items_quantity + " = ? WHERE " +
                            CreateDatabase.TB_cart_items_cart_id + " = ? AND " +
                            CreateDatabase.TB_cart_items_product_id + " = ?",
                    new String[]{String.valueOf(newQuantity), String.valueOf(cartId), String.valueOf(productId)});
        } else {
            // Sản phẩm chưa có trong giỏ hàng, thêm mới
            ContentValues itemValues = new ContentValues();
            itemValues.put(CreateDatabase.TB_cart_items_cart_id, cartId);
            itemValues.put(CreateDatabase.TB_cart_items_product_id, productId);
            itemValues.put(CreateDatabase.TB_cart_items_quantity, quantity);
            db.insert(CreateDatabase.TB_cart_items, null, itemValues);
        }
        cursor.close();
    }

    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> cartItems = new ArrayList<>();

        // Lấy cart_id của người dùng
        Cursor cartCursor = db.rawQuery(
                "SELECT " + CreateDatabase.TB_cart_cart_id + " FROM " + CreateDatabase.TB_cart +
                        " WHERE " + CreateDatabase.TB_cart_user_id + " = ?", new String[]{String.valueOf(userId)});

        if (cartCursor.moveToFirst()) {
            int cartId = cartCursor.getInt(cartCursor.getColumnIndexOrThrow(CreateDatabase.TB_cart_cart_id));

            // Lấy các sản phẩm trong giỏ hàng của userId cụ thể
            Cursor cursor = db.rawQuery(
                    "SELECT ci." + CreateDatabase.TB_cart_items_cart_item_id + ", ci." + CreateDatabase.TB_cart_items_product_id +
                            ", p." + CreateDatabase.TB_products_product_name + ", p." + CreateDatabase.TB_products_price +
                            ", ci." + CreateDatabase.TB_cart_items_quantity + ", p." + CreateDatabase.TB_products_image +
                            " FROM " + CreateDatabase.TB_cart_items + " ci JOIN " + CreateDatabase.TB_products + " p ON ci." +
                            CreateDatabase.TB_cart_items_product_id + " = p." + CreateDatabase.TB_products_product_id +
                            " WHERE ci." + CreateDatabase.TB_cart_items_cart_id + " = ?", new String[]{String.valueOf(cartId)});

            while (cursor.moveToNext()) {
                CartItem item = new CartItem(
                        cursor.getInt(0), // cart_item_id
                        cursor.getInt(1), // product_id
                        cursor.getString(2), // product_name
                        cursor.getDouble(3), // price
                        cursor.getInt(4), // quantity
                        cursor.getString(5) // image
                );
                cartItems.add(item);
            }
            cursor.close();
        }
        cartCursor.close();
        return cartItems;
    }

    // Hàm cập nhật số lượng sản phẩm trong giỏ hàng
    public void updateCartItemQuantity(int cartItemId, int newQuantity) {
        db.execSQL("UPDATE " + CreateDatabase.TB_cart_items +
                        " SET " + CreateDatabase.TB_cart_items_quantity + " = ? " +
                        " WHERE " + CreateDatabase.TB_cart_items_cart_item_id + " = ?",
                new String[]{String.valueOf(newQuantity), String.valueOf(cartItemId)});
    }

    // Hàm xóa sản phẩm khỏi giỏ hàng
    public void removeCartItem(int cartItemId) {
        db.execSQL("DELETE FROM " + CreateDatabase.TB_cart_items +
                        " WHERE " + CreateDatabase.TB_cart_items_cart_item_id + " = ?",
                new String[]{String.valueOf(cartItemId)});
    }

    // Hàm xóa giỏ hàng của người dùng cụ thể khi thanh toán thành công
    public void clearCartByUserId(int userId) {
        // Lấy cart_id của người dùng
        Cursor cartCursor = db.rawQuery("SELECT " + CreateDatabase.TB_cart_cart_id + " FROM " + CreateDatabase.TB_cart +
                " WHERE " + CreateDatabase.TB_cart_user_id + " = ?", new String[]{String.valueOf(userId)});

        if (cartCursor.moveToFirst()) {
            int cartId = cartCursor.getInt(cartCursor.getColumnIndexOrThrow(CreateDatabase.TB_cart_cart_id));

            // Xóa các sản phẩm trong giỏ hàng
            db.execSQL("DELETE FROM " + CreateDatabase.TB_cart_items +
                    " WHERE " + CreateDatabase.TB_cart_items_cart_id + " = ?", new String[]{String.valueOf(cartId)});

            // Xóa giỏ hàng của người dùng
            db.execSQL("DELETE FROM " + CreateDatabase.TB_cart +
                    " WHERE " + CreateDatabase.TB_cart_cart_id + " = ?", new String[]{String.valueOf(cartId)});
        }
        cartCursor.close();
    }

    // Hàm lưu đơn hàng
    public void saveOrder(Order order) {
        ContentValues orderValues = new ContentValues();
        orderValues.put("total_price", order.getTotalPrice());
        orderValues.put("payment_method", order.getPaymentMethod());
        orderValues.put("order_status", order.getOrderStatus());

        // Lưu đơn hàng vào bảng orders
        long orderId = db.insert("orders", null, orderValues);

        // Lưu các sản phẩm trong đơn hàng vào bảng order_items
        for (CartItem item : order.getCartItems()) {
            ContentValues itemValues = new ContentValues();
            itemValues.put("order_id", orderId); // ID đơn hàng đã lưu
            itemValues.put("product_id", item.getProductId());
            itemValues.put("quantity", item.getQuantity());
            itemValues.put("price", item.getPrice());
            db.insert("order_items", null, itemValues);
        }
    }

    // Đóng kết nối cơ sở dữ liệu
    public void close() {
        db.close();
    }
}
