package core;

public class FeastMenu {

    String feastCode;
    String feastName;
    int price;
    String ingredients;
    //

    public FeastMenu(String feastCode, String feastName, int price, String ingredients) {
        this.feastCode = feastCode;
        this.feastName = feastName;
        this.price = price;               // thêm để fix lỗi price = 0
        this.ingredients = ingredients;   // thêm để fix lỗi ingredients = null
    }

    public String getFeastName() {
        return feastName;
    }

    public void setFeastName(String feastName) {
        this.feastName = feastName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return this.feastCode + ", " + feastName + ", " + price; //To change body of generated methods, choose Tools | Templates.
    }

    public String getFeastCode() {
        return feastCode;
    }

    public void setFeastCode(String feastCode) {
        this.feastCode = feastCode;
    }

}
