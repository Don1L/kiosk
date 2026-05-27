package com.kiosk.model;

public abstract class Publication {
    private long id;
    private String title;
    private double price;
    private int quantity;

    public Publication(long id, String title, double price, int quantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setTitle(String title) { this.title = title; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "[id=" + id + ", title=" + title + ", price=" + price + ", qty=" + quantity + "]";
    }
}