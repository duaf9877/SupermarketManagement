package models;

public class Cashier extends Person {
    public Cashier(String id, String name) {
        super(id, name);
    }

    public void processCheckout(Customer customer) {
        System.out.println("Processing checkout for " + customer.getName());
        customer.displayCart();
        double total = customer.checkout();
        System.out.println("Total Bill: " + total);
    }

    @Override
    public void displayInfo() {
        System.out.println("Cashier: " + id + " | " + name);
    }
}
