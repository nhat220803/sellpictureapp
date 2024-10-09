package com.example.sellpicture;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String image; // Thêm thuộc tính image

    public Product(int id, String name, String description, double price, int stockQuantity, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.image = image; // Gán giá trị cho thuộc tính image
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getImage() {
        return image; // Thêm getter cho thuộc tính image
    }
}