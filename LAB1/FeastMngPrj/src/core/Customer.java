package core;

import java.io.Serializable;

public class Customer implements Serializable {

    private String cusCode;
    private String cusName;
    private String phone;
    private String email;

    public Customer(String cusCode, String cusName, String phone, String email) {
        this.cusCode = cusCode;
        this.cusName = cusName;
        this.phone = phone;
        this.email = email;
    }

    public Customer(String cusCode) {
        this.cusCode = cusCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Customer)) return false;
        return this.cusCode.equalsIgnoreCase(((Customer) obj).cusCode);
    }

    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-20s | %-12s | %-25s",
                cusCode, cusName, phone, email);
    }
} 