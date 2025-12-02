package models;

import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {
    private static int totalCustomers = 0;
    private final List<CartItem> cart;

    public Customer(String id, String name) {
        super(id, name);
        cart = new ArrayList<>();
        totalCustomers++;
    }

    public static int getTotalCustomers() { return totalCustomers; }

    public void addToCart(Product product, int quantity) {
        cart.add(new CartItem(product, quantity));
        System.out.println(quantity + " units of " + product.getName() + " added to cart for " + name);
    }

    public double checkout() {
        double total = 0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
            item.getProduct().updateStock(-item.getQuantity());
        }
        cart.clear();
        return total;
    }

    public void displayCart() {
        System.out.println("Cart for " + name + ":");
        for (CartItem item : cart) {
            System.out.println(item.getProduct().getName() + " x" + item.getQuantity() + " = " + item.getTotalPrice());
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("Customer: " + id + " | " + name);
    }

    public List<CartItem> getCartItems() {
        return cart;
    }
    
    public double getCartTotal() {
        double total = 0;
        for(CartItem item : cart) {
            total += item.getTotalPrice();
        }
        return total;
    }
    
}
