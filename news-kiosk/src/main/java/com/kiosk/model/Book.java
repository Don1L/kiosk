package com.kiosk.model;

public class Book extends Publication {
    private String author;
    private String isbn;

    public Book(long id, String title, double price, int quantity,
                String author, String isbn) {
        super(id, title, price, quantity);
        this.author = author;
        this.isbn = isbn;
    }

    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    @Override
    public String toString() {
        return "Book " + super.toString() + ", author=" + author + ", isbn=" + isbn;
    }
}