package core;

import java.io.*;
import java.util.*;

public class FeastMenuList extends ArrayList<FeastMenu> {

    private final String pathFile = "src/data/FeastMenu.csv";

    public FeastMenuList() {
        readFile(pathFile);
    }

    //mở file văn bản để đọc
    public void readFile(String pathFile) {
        File file = new File(pathFile);
        if (!file.exists()) {
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // BỎ DÒNG GIẢI THÍCH TRONG FILE
            // PW001,Wedding party 01,3750000,"+ Khai vị : Súp bong bóng cá; Nem rán; G...
            while ((line = reader.readLine()) != null) {
                FeastMenu menu = parseLineToMenu(line);
                if (menu != null) {
                    this.add(menu);
                }
            }

            // Sắp xếp theo giá tăng dần
            this.sort(Comparator.comparingInt(FeastMenu::getPrice));

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    //chuyển đổi dòng văn bản thành 1 đối tượng FeastMenu
    private FeastMenu parseLineToMenu(String text) {
        String[] data = text.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if (data.length != 4) {
            return null;
        }

        try {
            String code = data[0].trim();
            String name = data[1].trim();
            String rawPrice = data[2].trim().replace(",", "").replace(".", "").replaceAll("\\s", "");
            int price = Integer.parseInt(rawPrice);
            String ingredients = data[3].trim().replace("\"", "");
            return new FeastMenu(code, name, price, ingredients);
        } catch (Exception e) {
            return null;
        }
    }

    //hiển thị toàn bộ thực đơn
    public void printAll() {
        if (this.isEmpty()) {
            System.out.println("No menus available.");
            return;
        }

        System.out.println("\nList of Set Menus for ordering party:");
        System.out.println("------------------------------------------------------------");

        for (FeastMenu m : this) {
            System.out.printf("Code    : %s\n", m.getFeastCode());
            System.out.printf("Name    : %s\n", m.getFeastName());
            System.out.printf("Price   : %,d Vnd\n", m.getPrice());

            System.out.println("Ingredients:");
            String ingredients = m.getIngredients();
            if (ingredients != null && !ingredients.trim().isEmpty()) {
                for (String part : ingredients.split("#")) {
                    if (!part.trim().isEmpty()) {
                        System.out.println("+ " + part.trim());
                    }
                }
            } else {
                System.out.println("(No ingredients listed)");
            }

            System.out.println("------------------------------------------------------------");
        }
    }

    //tìm kiếm theo mã
    public FeastMenu findByCode(String menuCode) {
        return this.stream()
                .filter(m -> m.getFeastCode().equalsIgnoreCase(menuCode))
                .findFirst()
                .orElse(null);
    }
}
