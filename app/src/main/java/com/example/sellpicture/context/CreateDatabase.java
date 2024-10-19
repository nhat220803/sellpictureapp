package com.example.sellpicture.context;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateDatabase extends SQLiteOpenHelper {

    public static String  TB_users = "users";
    public static String  TB_products = "products";
    public static String  TB_product_categories = "product_categories";
    public static String TB_orders = "orders";
    public static String  TB_payments = "payments";
    public static String  TB_cart = "cart";
    public static String  TB_cart_items = "cart_items";
    public static String  TB_order_items = "order_items";
    public static String  TB_roles = "roles";
    public static String  TB_shipping = "shipping";
    public static String  TB_shipping_addresses = "shipping_addresses";

    public static String TB_users_user_id = "user_id";
    public static String TB_users_username = "username";
    public static String TB_users_password = "password";
    public static String TB_users_email = "email";
    public static String TB_users_full_name = "full_name";
    public static String TB_users_phone = "phone";
    public static String TB_users_role_id = "role_id";
    public static String TB_users_avatar = "avatar";

    public static String TB_shipping_shipping_id = "shipping_id";
    public static String TB_shipping_order_id = "order_id";
    public static String TB_shipping_shipping_provider = "shipping_provider";
    public static String TB_shipping_tracking_number = "tracking_number";
    public static String TB_shipping_status = "status";
    public static String TB_shipped_at = "shipped_at";
    public static String TB_shipping_delivered_at = "delivered_at";

    public static String TB_shipping_addresses_address_id = "address_id";
    public static String TB_shipping_addresses_user_id = "user_id";
    public static String TB_shipping_addresses_recipient_name = "recipient_name";
    public static String TB_shipping_addresses_address = "address";
    public static String TB_shipping_addresses_city = "city";
    public static String TB_shipping_addresses_district = "district";
    public static String TB_shipping_addresses_zip_code = "zip_code";
    public static String TB_shipping_addresses_province = "province";
    public static String TB_shipping_addresses_phone = "phone";

    public static String  TB_roles_role_id = "role_id";
    public static String  TB_roles_role_name = "role_name";

    public static String  TB_products_product_id = "product_id";
    public static String  TB_products_product_name = "product_name";
    public static String  TB_products_description = "description";
    public static String  TB_products_price = "price";
    public static String  TB_products_stock_quantity = "stock_quantity";
    public static String  TB_products_category_id = "category_id";
    public static String  TB_products_image = "image";

    public static String  TB_product_categories_category_id = "category_id";
    public static String  TB_product_categories_category_name = "category_name";

    public static String  TB_payments_payment_id = "payment_id";
    public static String  TB_payments_order_id = "order_id";
    public static String  TB_payments_payment_method = "payment_method";
    public static String  TB_payments_amount = "amount";
    public static String  TB_payments_payment_date = "payment_date";
    public static String  TB_payments_status = "status";

    public static String TB_orders_order_id = "order_id";
    public static String TB_orders_user_id = "user_id";
    public static String TB_orders_total_amount = "total_amount";
    public static String TB_orders_shipping_fee = "shipping_fee";
    public static String TB_orders_payment_method = "payment_method";
    public static String TB_orders_shipping_address_id = "shipping_address_id";
    public static String TB_orders_status = "status";
    public static String TB_orders_created_at = "created_at";

    public static String  TB_order_items_order_item_id = "order_item_id";
    public static String  TB_order_items_order_id = "order_id";
    public static String  TB_order_items_product_id = "product_id";
    public static String  TB_order_items_quantity = "quantity";
    public static String  TB_order_items_price = "price";

    public static String  TB_cart_items_cart_item_id = "cart_item_id";
    public static String  TB_cart_items_cart_id = "cart_id";
    public static String  TB_cart_items_product_id = "product_id";
    public static String  TB_cart_items_quantity = "quantity";

    public static String  TB_cart_cart_id = "cart_id";
    public static String  TB_cart_user_id = "user_id";
    public static String  TB_cart_created_at = "created_at";

    public CreateDatabase(@Nullable Context context) {
        super(context, "pictureshop",null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbUsers = "CREATE TABLE " + TB_users + " ("
                + TB_users_user_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_users_username + " TEXT UNIQUE, "
                + TB_users_password + " TEXT, "
                + TB_users_email + " TEXT UNIQUE, "
                + TB_users_full_name + " TEXT, "
                + TB_users_phone + " TEXT, "
                + TB_users_role_id + " INTEGER, "
                + TB_users_avatar + " TEXT);";

        // Tạo bảng Roles
        String tbRoles = "CREATE TABLE " + TB_roles + " ("
                + TB_roles_role_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_roles_role_name + " TEXT UNIQUE);";

        // Tạo bảng Product_Categories
        String tbProductCategories = "CREATE TABLE " + TB_product_categories + " ("
                + TB_product_categories_category_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_product_categories_category_name + " TEXT UNIQUE);";

        // Tạo bảng Products
        String tbProducts = "CREATE TABLE " + TB_products + " ("
                + TB_products_product_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_products_product_name + " TEXT, "
                + TB_products_description + " TEXT, "
                + TB_products_price + " DECIMAL(10,2) NOT NULL, "
                + TB_products_stock_quantity + " INTEGER NOT NULL, "
                + TB_products_category_id + " INTEGER, "
                + TB_products_image + " TEXT, "
                + "FOREIGN KEY(" + TB_products_category_id + ") REFERENCES " + TB_product_categories + "(" + TB_product_categories_category_id + ") ON DELETE SET NULL);";

        // Tạo bảng CartItem
        String tbCart = "CREATE TABLE " + TB_cart + " ("
                + TB_cart_cart_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_cart_user_id + " INTEGER, "
                + TB_cart_created_at + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(" + TB_cart_user_id + ") REFERENCES " + TB_users + "(" + TB_users_user_id + ") ON DELETE SET NULL);";

        // Tạo bảng Cart_Items
        String tbCartItems = "CREATE TABLE " + TB_cart_items + " ("
                + TB_cart_items_cart_item_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_cart_items_cart_id + " INTEGER, "
                + TB_cart_items_product_id + " INTEGER, "
                + TB_cart_items_quantity + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + TB_cart_items_cart_id + ") REFERENCES " + TB_cart + "(" + TB_cart_cart_id + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + TB_cart_items_product_id + ") REFERENCES " + TB_products + "(" + TB_products_product_id + ") ON DELETE CASCADE);";

        // Tạo bảng Orders
        String tbOrders = "CREATE TABLE " + TB_orders + " ("
                + TB_orders_order_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_orders_user_id + " INTEGER, "
                + TB_orders_total_amount + " DECIMAL(10,2) NOT NULL, "
                + TB_orders_shipping_fee + " DECIMAL(10,2), "
                + TB_orders_payment_method + " TEXT CHECK(" + TB_orders_payment_method + " IN ('cod','bank_transfer','online_payment')) NOT NULL, "
                + TB_orders_shipping_address_id + " INTEGER, "
                + TB_orders_status + " TEXT CHECK(" + TB_orders_status + " IN ('pending','approved','shipped','delivered','cancelled')) NOT NULL, "
                + TB_orders_created_at + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(" + TB_orders_user_id + ") REFERENCES " + TB_users + "(" + TB_users_user_id + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + TB_orders_shipping_address_id + ") REFERENCES " + TB_shipping_addresses + "(address_id) ON DELETE SET NULL);";

        // Tạo bảng Order_Items
        String tbOrderItems = "CREATE TABLE " + TB_order_items + " ("
                + TB_order_items_order_item_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_order_items_order_id + " INTEGER, "
                + TB_order_items_product_id + " INTEGER, "
                + TB_order_items_quantity + " INTEGER NOT NULL, "
                + TB_order_items_price + " DECIMAL(10,2) NOT NULL, "
                + "FOREIGN KEY(" + TB_order_items_order_id + ") REFERENCES " + TB_orders + "(" + TB_orders_order_id + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + TB_order_items_product_id + ") REFERENCES " + TB_products + "(" + TB_products_product_id + ") ON DELETE CASCADE);";

        // Tạo bảng Payments
        String tbPayments = "CREATE TABLE " + TB_payments + " ("
                + TB_payments_payment_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_payments_order_id + " INTEGER, "
                + TB_payments_payment_method + " TEXT CHECK(" + TB_payments_payment_method + " IN ('cod','bank_transfer','online_payment')) NOT NULL, "
                + TB_payments_amount + " DECIMAL(10,2) NOT NULL, "
                + TB_payments_payment_date + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + TB_payments_status + " TEXT CHECK(" + TB_payments_status + " IN ('pending','completed','failed')) NOT NULL, "
                + "FOREIGN KEY(" + TB_payments_order_id + ") REFERENCES " + TB_orders + "(" + TB_orders_order_id + ") ON DELETE CASCADE);";

        // Tạo bảng Shipping
        String tbShipping = "CREATE TABLE " + TB_shipping + " ("
                + TB_shipping_shipping_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_shipping_order_id + " INTEGER, "
                + TB_shipping_shipping_provider + " TEXT NOT NULL, "
                + TB_shipping_tracking_number + " TEXT, "
                + TB_shipping_status + " TEXT CHECK(" + TB_shipping_status + " IN ('pending','shipped','delivered')) NOT NULL, "
                + TB_shipped_at + " TIMESTAMP, "
                + TB_shipping_delivered_at + " TIMESTAMP, "
                + "FOREIGN KEY(" + TB_shipping_order_id + ") REFERENCES " + TB_orders + "(" + TB_orders_order_id + ") ON DELETE CASCADE);";

        // Tạo bảng Shipping_Addresses
        String tbShippingAddresses = "CREATE TABLE " + TB_shipping_addresses + " ("
                + TB_shipping_addresses_address_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_shipping_addresses_user_id + " INTEGER, "
                + TB_shipping_addresses_recipient_name + " TEXT NOT NULL, "
                + TB_shipping_addresses_address + " TEXT NOT NULL, "
                + TB_shipping_addresses_city + " TEXT NOT NULL, "
                + TB_shipping_addresses_district + " TEXT, "
                + TB_shipping_addresses_zip_code + " TEXT, "
                + TB_shipping_addresses_province + " TEXT NOT NULL, "
                + TB_shipping_addresses_phone + " TEXT, "
                + "FOREIGN KEY(" + TB_shipping_addresses_user_id + ") REFERENCES " + TB_users + "(" + TB_users_user_id + ") ON DELETE CASCADE);";

        db.execSQL(tbUsers);
        db.execSQL(tbRoles);
        db.execSQL(tbProductCategories);
        db.execSQL(tbProducts);
        db.execSQL(tbCart);
        db.execSQL(tbCartItems);
        db.execSQL(tbOrders);
        db.execSQL(tbOrderItems);
        db.execSQL(tbPayments);
        db.execSQL(tbShipping);
        db.execSQL(tbShippingAddresses);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_users);
        db.execSQL("DROP TABLE IF EXISTS " + TB_roles);
        db.execSQL("DROP TABLE IF EXISTS " + TB_product_categories);
        db.execSQL("DROP TABLE IF EXISTS " + TB_products);
        db.execSQL("DROP TABLE IF EXISTS " + TB_cart);
        db.execSQL("DROP TABLE IF EXISTS " + TB_cart_items);
        db.execSQL("DROP TABLE IF EXISTS " + TB_orders);
        db.execSQL("DROP TABLE IF EXISTS " + TB_order_items);
        db.execSQL("DROP TABLE IF EXISTS " + TB_payments);
        db.execSQL("DROP TABLE IF EXISTS " + TB_shipping);
        db.execSQL("DROP TABLE IF EXISTS " + TB_shipping_addresses);

        // Gọi lại onCreate để tạo lại các bảng với cấu trúc mới
        onCreate(db);
    }
    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }
}

