package com.example.sellpicture;

public class Product {
    private String productName;
    private String description;
    private double price;
    private int quantity;

    public Product(String productName, String description, double price) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = 1; // Mặc định số lượng là 1 khi thêm vào giỏ hàng
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

