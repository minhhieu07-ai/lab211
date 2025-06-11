
import core.CustomerList;
import core.FeastMenuList;
import core.OrderList;
import tool.ConsoleInputter;

public class OrderManager {

    public static void main(String[] args) {

        String fmFname = "src/data/FeastMenu.csv";
        String custFname = "src/data/Customers.dat";
        String orderFname = "src/data/feast_order_service.dat";

        CustomerList cList;     // danh sách khách hàng
        OrderList oList;        // danh sách order

        // chuẩn bị dữ liệu ban đầu về menu list
        FeastMenuList fmList = new FeastMenuList(); // Tự động đọc từ CSV trong constructor

        // chuẩn bị ds khách hàng
        cList = new CustomerList();
        cList.readFromFile(custFname);  // đọc file để lấy ds khách hàng đã đăng ký rõ

        // chuẩn bị ds order đã có
        oList = new OrderList(fmList, cList);
        oList.readFromFile(orderFname);

        // code quản lý chức năng của chương trình
        int choice; // lựa chọn chức năng của chương trình
        boolean changed = false;
        do {
            choice = ConsoleInputter.intMenu(
                    "Register customers.",
                    "Update customer information.",
                    "Search for customer information by name.",
                    "Display feast menus.",
                    "Place a feast order.",
                    "Update order information.",
                    "Save data to file.",
                    "Display Customer.",
                    "Display Order list.",
                    "Quit"
            );

            switch (choice) {
                case 1:
                    cList.addNew();
                    changed = true;
                    break;
                case 2:
                    cList.updateCust();
                    changed = true;
                    break;
                case 3:
                    cList.printByName();
                    break;
                case 4:
                    fmList.printAll();
                    break;
                case 5:
                    oList.placeOrder();
                    changed = true;
                    break;
                case 6:
                    oList.updateOrder();
                    changed = true;
                    break;
                case 7:
                    if (changed == true) {
                        if (!cList.isEmpty()) {
                            cList.saveToFile(custFname);
                        }
                        if (!oList.isEmpty()) {
                            oList.saveToFile(orderFname);
                        }

                        changed = false;
                    }
                    break;
                case 8:
                    cList.printAll();
                    break;
                case 9:
                    oList.printAll();
                    break;
                case 10:
                    if (changed) {
                        boolean resp = ConsoleInputter.getBoolean("Data changed. Save or not?");
                        if (resp == true) {
                            if (!cList.isEmpty()) {
                                cList.saveToFile(custFname);
                            }
                            if (!cList.isEmpty()) {
                                oList.saveToFile(orderFname);
                            }
                        }
                        System.out.println("Good luck.");
                    }
            }
        } while (choice >= 0 && choice < 10);

    }
}
