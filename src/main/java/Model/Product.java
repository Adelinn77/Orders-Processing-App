package Model;

public class Product {
    private int id;
    private String name;
    private int price;
    private int currentStock;

    public Product(String name, int price, int currentStock) {
        this.name = name;
        this.price = price;
        this.currentStock = currentStock;
    }

    public Product() {}

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
