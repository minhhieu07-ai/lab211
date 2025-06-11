package core;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    String orderID;
    String custCode;
    String feastMenuCode; // mã menu
    int numTable;
    Date preferredDate;// ngày tổ chức
    private double price;
    private double totalCost;

    public Order(String orderID, String custCode, String feastMenuCode, int numTable, Date preferredDate) {
        this.orderID = orderID;
        this.custCode = custCode;
        this.feastMenuCode = feastMenuCode;
        this.numTable = numTable;
        this.preferredDate = preferredDate;
        this.price = 0;
        this.totalCost = 0;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        this.totalCost = this.price * this.numTable; // cập nhật lại total
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    //code phuc vu tim kiem
    public Order(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public boolean equals(Object obj) {
        Order o = (Order) obj;
        return this.getOrderID().equals(o.getOrderID());
    }

    //getters , setters  tự làm
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getFeastMenuCode() {
        return feastMenuCode;
    }

    public void setFeastMenuCode(String feastMenuCode) {
        this.feastMenuCode = feastMenuCode;
    }

    public int getNumTable() {
        return numTable;
    }

    public void setNumTable(int numTable) {
        this.numTable = numTable;
        this.totalCost = this.price * this.numTable; // cập nhật lại total nếu sửa số bàn
    }

    public Date getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(Date preferredDate) {
        this.preferredDate = preferredDate;
    }

    @Override
    public String toString() {
        return String.format("%-15s | %-8s | %-8s | %10.0f | %-2d | %12.0f",
                orderID, custCode, feastMenuCode, price, numTable, totalCost);
    }
} 