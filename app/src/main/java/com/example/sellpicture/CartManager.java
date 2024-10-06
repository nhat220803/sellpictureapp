package com.example.sellpicture;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Product> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();

    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }



    public List<Product> getCartItems() {
        return cartItems;
    }

    public void addToCart(Product product) {
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        for (Product item : cartItems) {
            if (item.getProductName().equals(product.getProductName())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
        cartItems.add(product);
    }

    public void updateCart(Product product) {
        for (Product item : cartItems) {
            if (item.getProductName().equals(product.getProductName())) {
                item.setQuantity(product.getQuantity());
                return;
            }
        }
    }

    public void removeFromCart(Product product) {
        // Xóa sản phẩm chỉ nếu sản phẩm đó có trong giỏ hàng
        cartItems.removeIf(item -> item.getProductName().equals(product.getProductName()));
    }


    public double getTotalPrice() {
        double total = 0;
        for (Product item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}
