package core;

import java.util.ArrayList;
import tool.ConsoleInputter;
import java.util.Date;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.text.SimpleDateFormat;

public class OrderList extends ArrayList<Order> {

    private transient FeastMenuList fmList;  // menu list phải có rồi
    private transient CustomerList cList;    // phải có trước ds cust

    // ctor
    public OrderList(FeastMenuList fmList, CustomerList cList) {
        this.fmList = fmList;
        this.cList = cList;
    }

    // bổ sung sau khi đọc file nhị phân
    public void setRefs(FeastMenuList fmList, CustomerList cList) {
        this.fmList = fmList;
        this.cList = cList;
    }

    // đặt tiệc
    public void placeOrder() {
        if (fmList == null || fmList.isEmpty()) {
            System.out.println("Chưa có menu");
            return;
        }

        if (this.cList == null || this.cList.isEmpty()) {
            System.out.println("Chưa có customer");
            return;
        }

        String orderID = ConsoleInputter.dataKeyGen(); // mã đơn hàng
        String custCode = ConsoleInputter.getStr("Cust code", CustomerList.custCodePattern, "Cust code: CGK + 4 digits").toUpperCase();

        int pos = this.cList.indexOf(new Customer(custCode));
        if (pos < 0) {
            System.out.println("Cust. does not exist.");
            return;
        }

        String feastMenuCode = ConsoleInputter.getStr("Enter menu code (e.g., PW001):").toUpperCase();
        fmList.printAll();  // hiển thị menu trước

        FeastMenu selectedFm = fmList.findByCode(feastMenuCode);
        if (selectedFm == null) {
            System.out.println("This menu does not exist.");
            return;
        }

        int numTable = ConsoleInputter.getInt("Num of tables", 1, 1000);

        Date preferredDate;
        do {
            preferredDate = ConsoleInputter.getDate("Date (dd-MM-yyyy)", "dd-MM-yyyy");
            if (!preferredDate.after(new Date())) {
                System.out.println("Preferred date must be after the current date.");
            }
        } while (!preferredDate.after(new Date()));

        Order order = new Order(orderID, custCode, feastMenuCode, numTable, preferredDate);
        order.setPrice(selectedFm.getPrice());
        this.add(order);

        System.out.println("\nCustomer order information [Order ID: " + orderID + "]");
        Customer cust = cList.findByCode(custCode);
        if (cust != null) {
            System.out.println("Customer code : " + cust.getCusCode());
            System.out.println("Customer name : " + cust.getCusName());
            System.out.println("Phone number   : " + cust.getPhone());
            System.out.println("Email          : " + cust.getEmail());
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Code of Set Menu: " + selectedFm.getFeastCode());
        System.out.println("Set menu name   : " + selectedFm.getFeastName());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Preferred date  : " + df.format(preferredDate));
        System.out.println("Number of tables: " + numTable);
        System.out.printf("Price           : %,d Vnd\n", selectedFm.getPrice());

        System.out.println("Ingredients:");
        for (String part : selectedFm.getIngredients().split("#")) {
            if (!part.trim().isEmpty()) {
                System.out.println("+ " + part.trim());
            }
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("Total cost      : %,.0f Vnd\n", order.getTotalCost());
    }

    // update tiệc
    public void updateOrder() {
        String orderID = ConsoleInputter.getStr("Enter Order ID to update:");
        int pos = this.indexOf(new Order(orderID));
        if (pos < 0) {
            System.out.println("This Order does not exist.");
            return;
        }

        Order order = this.get(pos);

        // Không cho sửa nếu ngày đã qua
        if (!order.getPreferredDate().after(new Date())) {
            System.out.println("Cannot update order. Event date already passed.");
            return;
        }

        // Sửa mã menu
        fmList.printAll();
        String newMenuCode = ConsoleInputter.getStr("Enter new Set Menu code (blank to keep old):");
        if (!newMenuCode.isEmpty()) {
            FeastMenu newMenu = fmList.findByCode(newMenuCode);
            if (newMenu != null) {
                order.setFeastMenuCode(newMenuCode);
                order.setPrice(newMenu.getPrice());
            } else {
                System.out.println("Invalid menu code. Keeping old.");
            }
        }

        // Sửa số bàn
        String newTableStr = ConsoleInputter.getStr("Enter new number of tables (blank to keep old):");
        if (!newTableStr.isEmpty()) {
            int newTables = Integer.parseInt(newTableStr);
            if (newTables > 0) {
                order.setNumTable(newTables);
            }
        }

        // Sửa ngày tổ chức
        String newDateStr = ConsoleInputter.getStr("Enter new event date (dd-MM-yyyy) (blank to keep old):");
        if (!newDateStr.isEmpty()) {
            Date newDate = ConsoleInputter.getDate(newDateStr, "dd-MM-yyyy");
            if (newDate.after(new Date())) {
                order.setPreferredDate(newDate);
            } else {
                System.out.println("Invalid future date. Keeping old.");
            }
        }

        System.out.println("Order updated successfully.");
    }

    public void displayOrdersTable() {
        if (this.isEmpty()) {
            System.out.println("No data in the system.");
            return;
        }
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-15s | %-12s | %-10s | %-8s | %-10s | %-6s | %-12s\n",
                "Order ID", "Event Date", "Customer", "Menu", "Price", "Table", "Total Cost");
        System.out.println("-------------------------------------------------------------------------");
        Collections.sort(this, Comparator.comparing(Order::getPreferredDate));
        for (Order o : this) {
            System.out.printf("%-15s | %-12s | %-10s | %-8s | %-10.0f | %-6d | %-12.0f\n",
                    o.getOrderID(), o.getPreferredDate(), o.getCustCode(), o.getFeastMenuCode(),
                    o.getPrice(), o.getNumTable(), o.getTotalCost());
        }
        System.out.println("-------------------------------------------------------------------------");
    }

    // Xuất ds order
    public void printAll() {
        if (this.isEmpty()) {
            System.out.println("Order list is empty");
            return;
        }
        for (Order o : this) {
            System.out.println(o);
        }
    }

    // lưu file nhị phân - tự làm
    public void saveToFile(String fName) {
        try {
            FileOutputStream fos = new FileOutputStream(fName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
            System.out.println("Order data has been successfully saved to " + fName);
        } catch (Exception e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    // đọc file nhị phân - tự làm
    public void readFromFile(String fName) {
        try {
            FileInputStream fis = new FileInputStream(fName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            OrderList tmp = (OrderList) ois.readObject();
            this.clear();
            this.addAll(tmp);
            ois.close();
            fis.close();
            System.out.println("Order data loaded from " + fName);
        } catch (Exception e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }
}
