package com.example.cinefast;

public class Snack {
    private int id;
    private String name;
    private double price;
    private int imageResId;
    private int quantity;

    public Snack(int id, String name, double price, int imageResId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
