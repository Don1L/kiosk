package com.kiosk.model;

public class Magazine extends Publication {
    private int issueNumber;
    private String monthYear; // формат: MM/YYYY

    public Magazine(long id, String title, double price, int quantity,
                    int issueNumber, String monthYear) {
        super(id, title, price, quantity);
        this.issueNumber = issueNumber;
        this.monthYear = monthYear;
    }

    public int getIssueNumber() { return issueNumber; }
    public String getMonthYear() { return monthYear; }
    public void setIssueNumber(int issueNumber) { this.issueNumber = issueNumber; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }

    @Override
    public String toString() {
        return "Magazine " + super.toString() + ", issue=" + issueNumber + ", month=" + monthYear;
    }
}