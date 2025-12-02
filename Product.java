package models;

public class Product {
    private static int totalProducts = 0;

    private final String productID;
    private final String name;
    private final String category;
    private final double price;
    private int quantity;

    public Product(String productID, String name, String category, double price, int quantity) {
        this.productID = productID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        totalProducts++;
    }

    public static int getTotalProducts() { return totalProducts; }

    public String getProductID() { return productID; }
    public String getName() { return name; }
    public String getCategory() { return category; } // Fixed
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void updateStock(int amount) {
        this.quantity += amount;
        System.out.println("Stock updated. Current quantity: " + this.quantity);
    }

    public void displayInfo() {
        System.out.println(productID + " | " + name + " | " + category + " | " + price + " | " + quantity);
    }

    @Override
    public String toString() {
        return name + " (" + productID + ")";
    }
}
