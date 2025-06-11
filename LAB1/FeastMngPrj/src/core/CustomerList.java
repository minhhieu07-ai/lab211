package core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import tool.ConsoleInputter;

public class CustomerList extends ArrayList<Customer> {

    static final String custCodePattern = "^[CGKcgk]\\d{4}$";
    private static final String custNamePattern = "^[a-zA-Z ]{2,25}$";
    private static final String phonePattern = "^(\\d{9}|\\d{11})$";
    private static final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private ArrayList<Customer> customerList = new ArrayList<Customer>();

    //Thêm cust data lấy từ bàn phím
    public void addNew() {
        // Nhập 4 field cho cust mới
        String custCode;
        int pos;
        do {
            custCode = ConsoleInputter.getStr("New cust code", custCodePattern, "Pattern: CGK + 4 digits");
            custCode = custCode.toUpperCase();
            pos = this.indexOf(new Customer(custCode));
            if (pos >= 0) {
                System.out.println("This code ís duplicated!");
            }
        } while (pos >= 0);

        String custName = ConsoleInputter.getStr("New cust name", custNamePattern, "2-25 letters or spaces");
        String phone = ConsoleInputter.getStr("Phone number", phonePattern, " Must bes 9 or 11 digitis");
        String email = ConsoleInputter.getStr("Emai", emailPattern, "Must be a valid email address");

        Customer newCust = new Customer(custCode, custName, phone, email);
        this.add(newCust);
        System.out.println("New Customer has been added");
    }

    //tìm cust theo tên
    //xuất ds cust với tên nhập từ bàn phím
    public void printByName() {
        if (this.isEmpty()) {
            System.out.println("Empty list");
            return;
        }
        //USER nhập vào tên cần tìm, không cần kiểm tra -> String name
        String name = ConsoleInputter.getStr("Searched customer name").trim().toUpperCase();
        // Duyệt danh sách
        //nếu phát hiện có cust có tên chứa đoạn name thì xuất cust này 
        boolean existed = false;
        // xuất tiêu đề bảng %-12s : xuất chuỗi độ dài 12 , dòng hàng trái
        ArrayList<Customer> matched = new ArrayList<>();
        for (Customer cust : this) {
            if (cust.getCusName().toUpperCase().contains(name)) {
                matched.add(cust);
                existed = true;
            }
        }
        if (!existed) {
            System.out.println("No one matches the search criteria!");
        } else {
            matched.sort(Comparator.comparing(Customer::getCusName));

            System.out.println("+--------+----------------------+------------+------------------------------+");
            System.out.printf("| %-6s | %-20s | %-10s | %-28s |\n", "Code", "Customer Name", "Phone", "Email");
            System.out.println("+--------+----------------------+------------+------------------------------+");
            for (Customer c : matched) {
                System.out.printf("| %-6s | %-20s | %-10s | %-28s |\n",
                        c.getCusCode(), c.getCusName(), c.getPhone(), c.getEmail());
            }
            System.out.println("+--------+----------------------+------------+------------------------------+");

        }

    }

    //update cust với data nhập từu bàn phím
    public void updateCust() {
        if (this.isEmpty()) {
            System.out.println("Empty list");
            return;
        }
        //nhập custCode đổi ra chữ hoa không ktra
        //tìm vị trí có mặt custCode trong danh sách -> int pos
        //if(pos<0){
        //Báo không có cust này
        // return;
        //}
        /*else {
            Customer cust= this.get(pos);// lấy ra cust sẽ cập nhật
            Nhập lại có ktra custName,phone,email
            cust.setCustName(custName);// sửa tên
            sửa phone,sửa email
            Báo "Đã update"
        
         */

        String code = ConsoleInputter.getStr("Enter customer code to update: ").toUpperCase();
        int pos = this.indexOf(new Customer(code));
        if (pos < 0) {
            System.out.println("This customer does not exist.");
            return;
        }

        Customer cust = this.get(pos);

        String newName = ConsoleInputter.getStr("Enter new name (Enter to skip): ");
        if (!newName.trim().isEmpty()) {
            if (newName.matches(custNamePattern)) {
                cust.setCusName(newName);
            } else {
                System.out.println("Invalid name. Keeping old name.");
            }
        }

        String newPhone = ConsoleInputter.getStr("Enter new phone (Enter to skip): ");
        if (!newPhone.trim().isEmpty()) {
            if (newPhone.matches(phonePattern)) {
                cust.setPhone(newPhone);
            } else {
                System.out.println("Invalid phone. Keeping old phone.");
            }
        }

        String newEmail = ConsoleInputter.getStr("Enter new email (Enter to skip): ");
        if (!newEmail.trim().isEmpty()) {
            if (newEmail.matches(emailPattern)) {
                cust.setEmail(newEmail);
            } else {
                System.out.println("Invalid email. Keeping old email.");
            }
        }

        System.out.println("Customer has been updated.");
    }

    // Lưu ds cust lên file nhi phân     
    public void saveToFile(String fName) {
        try {
            FileOutputStream fos = new FileOutputStream(fName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
            System.out.println("Customer data has been successfully saved to " + fName);
        } catch (Exception e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void readFromFile(String fName) {
        try {
            FileInputStream fis = new FileInputStream(fName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            CustomerList tmp = (CustomerList) ois.readObject();
            this.clear();
            this.addAll(tmp);
            ois.close();
            fis.close();
            System.out.println("Customer data loaded from " + fName);
        } catch (Exception e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    public void displayAll() {
        if (customerList.isEmpty()) {
            System.out.println("No data in the system.");
            return;
        }
        System.out.println("Customers information:");
        System.out.println("---------------------------------------------------------------");
        System.out.printf("%-6s | %-20s | %-10s | %-20s\n", "Code", "Customer Name", "Phone", "Email");
        System.out.println("---------------------------------------------------------------");
        Collections.sort(customerList, new Comparator<Customer>() {
            public int compare(Customer o1, Customer o2) {
                return o1.getCusName().compareTo(o2.getCusName());
            }
        });
        for (Customer c : customerList) {
            System.out.println(c);
        }
        System.out.println("---------------------------------------------------------------");
    }

    //xuất ds cust
    public void printAll() {
        if (this.size() == 0) {
            System.out.println("Empty List");
        } else {
            for (Customer cust : this) {
                System.out.println(cust);
            }
        }
    }

    public Customer findByCode(String code) {
        for (Customer c : this) {
            if (c.getCusCode().equalsIgnoreCase(code)) {
                return c;
            }
        }
        return null;
    }

}
