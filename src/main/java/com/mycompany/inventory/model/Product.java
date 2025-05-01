package com.mycompany.inventory.model;

public class Product {
    private int id;
    private String name;
    private String brand;
    private double price;
    private int quantity;
    private int categoryId;

    public Product() { }

    public Product(int id, String name, String brand, double price, int quantity, int categoryId) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }

    public Product(String name, String brand, double price, int quantity, int categoryId) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    @Override
    public String toString() {
        return name + " (" + brand + ")";
    }
}

