package com.kiosk.model;

public class Newspaper extends Publication {
    private int issueNumber;
    private String date; // формат: DD.MM.YYYY

    public Newspaper(long id, String title, double price, int quantity,
                     int issueNumber, String date) {
        super(id, title, price, quantity);
        this.issueNumber = issueNumber;
        this.date = date;
    }

    @Override
    public String getType() { return "NEWSPAPER"; }

    public int getIssueNumber() { return issueNumber; }
    public String getDate() { return date; }
    public void setIssueNumber(int issueNumber) { this.issueNumber = issueNumber; }
    public void setDate(String date) { this.date = date; }

    @Override
    public String toString() {
        return "Newspaper " + super.toString() + ", issue=" + issueNumber + ", date=" + date;
    }
}