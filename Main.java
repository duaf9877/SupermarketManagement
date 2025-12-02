import models.*;
import repository.Repository;
import utils.FileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final Repository<Product> productRepo = new Repository<>();
    private static final Repository<Customer> customerRepo = new Repository<>();
    private static final String PRODUCT_FILE = "products.dat";
    private static final String CUSTOMER_FILE = "customers.dat";

    public static void main(String[] args) {

        // ------------------- LOAD DATA -------------------
        loadData();

        // ------------------- SAMPLE PRODUCTS (if repo is empty) -------------------
        if (productRepo.getAll().isEmpty()) {
            addSampleProducts();
        }

        JFrame frame = new JFrame("Supermarket Management System");
        frame.setSize(1000, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // ------------------- PRODUCTS TAB -------------------
        JPanel productPanel = new JPanel(new BorderLayout(10,10));
        productPanel.setBorder(new EmptyBorder(10,10,10,10));

        DefaultTableModel productModel = new DefaultTableModel(new String[]{"ID","Name","Category","Price","Qty"},0);
        JTable productTable = new JTable(productModel);
        refreshProductTable(productModel, productRepo.getAll());

        // Product Form Panel
        JPanel productForm = new JPanel(new GridLayout(7,2,5,5));
        productForm.setBorder(new EmptyBorder(10,10,10,10));
        JTextField txtID = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtCategory = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtQty = new JTextField();

        productForm.add(new JLabel("Product ID:")); productForm.add(txtID);
        productForm.add(new JLabel("Name:")); productForm.add(txtName);
        productForm.add(new JLabel("Category:")); productForm.add(txtCategory);
        productForm.add(new JLabel("Price:")); productForm.add(txtPrice);
        productForm.add(new JLabel("Quantity:")); productForm.add(txtQty);

        JButton btnAddProduct = new JButton("Add Product");
        btnAddProduct.addActionListener(e -> {
            try {
                String id = txtID.getText();
                String name = txtName.getText();
                String cat = txtCategory.getText();
                double price = Double.parseDouble(txtPrice.getText());
                int qty = Integer.parseInt(txtQty.getText());

                Product p = new Product(id,name,cat,price,qty);
                productRepo.add(p);
                refreshProductTable(productModel, productRepo.getAll());
                JOptionPane.showMessageDialog(frame, "Product added successfully!");
                saveData();
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,"Invalid input!","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        productForm.add(btnAddProduct);

        // Product Search
        JTextField txtSearchProduct = new JTextField();
        JButton btnSearchProduct = new JButton("Search");
        btnSearchProduct.addActionListener(e -> {
            String query = txtSearchProduct.getText().toLowerCase();
            List<Product> results = productRepo.getAll().stream()
                    .filter(p -> p.getName().toLowerCase().contains(query) || p.getCategory().toLowerCase().contains(query))
                    .collect(Collectors.toList());
            refreshProductTable(productModel, results);
        });
        productForm.add(txtSearchProduct);
        productForm.add(btnSearchProduct);

        productPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        productPanel.add(productForm, BorderLayout.SOUTH);
        tabbedPane.addTab("Products", productPanel);

        // ------------------- CUSTOMERS TAB -------------------
        JPanel customerPanel = new JPanel(new BorderLayout(10,10));
        customerPanel.setBorder(new EmptyBorder(10,10,10,10));

        DefaultTableModel customerModel = new DefaultTableModel(new String[]{"ID","Name"},0);
        JTable customerTable = new JTable(customerModel);
        refreshCustomerTable(customerModel, customerRepo.getAll());

        JComboBox<String> customerCombo = new JComboBox<>();
        refreshCustomerCombo(customerCombo);

        JPanel customerForm = new JPanel(new GridLayout(4,2,5,5));
        JTextField txtCustID = new JTextField();
        JTextField txtCustName = new JTextField();
        JButton btnAddCust = new JButton("Add Customer");

        btnAddCust.addActionListener(e -> {
            String id = txtCustID.getText();
            String name = txtCustName.getText();
            if(id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(frame,"ID or Name cannot be empty!","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            Customer c = new Customer(id,name);
            customerRepo.add(c);
            refreshCustomerTable(customerModel, customerRepo.getAll());
            refreshCustomerCombo(customerCombo);
            JOptionPane.showMessageDialog(frame,"Customer added successfully!");
            saveData();
        });

        // Customer Search
        JTextField txtSearchCust = new JTextField();
        JButton btnSearchCust = new JButton("Search");
        btnSearchCust.addActionListener(e -> {
            String query = txtSearchCust.getText().toLowerCase();
            List<Customer> results = customerRepo.getAll().stream()
                    .filter(c -> c.getName().toLowerCase().contains(query))
                    .collect(Collectors.toList());
            refreshCustomerTable(customerModel, results);
        });

        customerForm.add(new JLabel("Customer ID:")); customerForm.add(txtCustID);
        customerForm.add(new JLabel("Name:")); customerForm.add(txtCustName);
        customerForm.add(btnAddCust);
        customerForm.add(new JLabel());
        customerForm.add(txtSearchCust); customerForm.add(btnSearchCust);

        customerPanel.add(new JScrollPane(customerTable), BorderLayout.CENTER);
        customerPanel.add(customerForm, BorderLayout.SOUTH);
        tabbedPane.addTab("Customers", customerPanel);

        // ------------------- BILLING TAB -------------------
        JPanel billingPanel = new JPanel(new BorderLayout(10,10));
        billingPanel.setBorder(new EmptyBorder(10,10,10,10));

        JPanel billingForm = new JPanel(new GridLayout(5,2,5,5));
        JComboBox<String> productCombo = new JComboBox<>();
        refreshProductCombo(productCombo);

        JTextField txtQtyBill = new JTextField();
        JButton btnAddCart = new JButton("Add to Cart");
        JButton btnViewBill = new JButton("View Bill");
        JButton btnCheckout = new JButton("Checkout");

        btnAddCart.addActionListener(e -> {
            try {
                String custName = (String) customerCombo.getSelectedItem();
                String prodName = (String) productCombo.getSelectedItem();
                int qty = Integer.parseInt(txtQtyBill.getText());

                if(custName == null || prodName == null) {
                    JOptionPane.showMessageDialog(frame,"Please select customer and product!","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Customer c = customerRepo.getAll().stream().filter(cust -> cust.getName().equals(custName)).findFirst().orElse(null);
                Product p = productRepo.getAll().stream().filter(prod -> prod.getName().equals(prodName)).findFirst().orElse(null);

                if(c != null && p != null) {
                    if(qty > p.getQuantity()) {
                        JOptionPane.showMessageDialog(frame,"Insufficient stock! Available: "+p.getQuantity(),"Error",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    c.addToCart(p,qty);
                    JOptionPane.showMessageDialog(frame,"Added to cart!");
                    saveData();
                }
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,"Invalid quantity!","Error",JOptionPane.ERROR_MESSAGE);
            }
        });

        // View Bill Button
        btnViewBill.addActionListener(e -> {
            String custName = (String) customerCombo.getSelectedItem();
            Customer c = customerRepo.getAll().stream().filter(cust -> cust.getName().equals(custName)).findFirst().orElse(null);
            if(c != null) {
                StringBuilder bill = new StringBuilder();
                bill.append("Final Bill for ").append(c.getName()).append("\n\n");
                double total = 0;
                for(CartItem item : c.getCartItems()) {
                    double subtotal = item.getQuantity() * item.getProduct().getPrice();
                    total += subtotal;
                    bill.append(item.getProduct().getName())
                            .append(" x").append(item.getQuantity())
                            .append(" = ").append(subtotal).append("\n");
                }
                // Apply discount if total > 500
                double discount = 0;
                if(total > 500) discount = total * 0.10;
                double finalAmount = total - discount;
                bill.append("\nTotal: ").append(total);
                if(discount > 0) {
                    bill.append("\nDiscount: ").append(discount);
                    bill.append("\nFinal Amount: ").append(finalAmount);
                }
                JOptionPane.showMessageDialog(frame,bill.toString(),"Final Bill",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnCheckout.addActionListener(e -> {
            String custName = (String) customerCombo.getSelectedItem();
            Customer c = customerRepo.getAll().stream().filter(cust -> cust.getName().equals(custName)).findFirst().orElse(null);
            if(c != null) {
                Cashier cashier = new Cashier("CA001","Ahmed");
                cashier.processCheckout(c);
                saveData();
            }
        });

        billingForm.add(new JLabel("Select Customer:")); billingForm.add(customerCombo);
        billingForm.add(new JLabel("Select Product:")); billingForm.add(productCombo);
        billingForm.add(new JLabel("Quantity:")); billingForm.add(txtQtyBill);
        billingForm.add(btnAddCart); billingForm.add(btnViewBill);
        billingForm.add(new JLabel()); billingForm.add(btnCheckout);

        billingPanel.add(billingForm, BorderLayout.CENTER);
        tabbedPane.addTab("Billing", billingPanel);

        // ------------------- REPORTS TAB -------------------
        JPanel reportPanel = new JPanel(new BorderLayout(10,10));
        reportPanel.setBorder(new EmptyBorder(10,10,10,10));
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);

        JButton btnReport = new JButton("Generate Product Report");
        btnReport.addActionListener(e -> {
            reportArea.setText("");
            double totalRevenue = 0;
            for(Product p : productRepo.getAll()) {
                reportArea.append(p.toString() + " | Stock: " + p.getQuantity() + " | Category: " + p.getCategory() + "\n");
            }
            for(Customer c : customerRepo.getAll()) {
                double custTotal = c.getCartItems().stream()
                        .mapToDouble(item -> item.getQuantity()*item.getProduct().getPrice()).sum();
                totalRevenue += custTotal;
            }
            reportArea.append("\nTotal Revenue: "+totalRevenue);
        });

        reportPanel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        reportPanel.add(btnReport, BorderLayout.SOUTH);
        tabbedPane.addTab("Reports", reportPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    // ------------------- Helper Methods -------------------
    private static void refreshProductTable(DefaultTableModel model, List<Product> products) {
        model.setRowCount(0);
        for(Product p : products) {
            model.addRow(new Object[]{p.getProductID(), p.getName(), p.getCategory(), p.getPrice(), p.getQuantity()});
        }
    }

    private static void refreshCustomerTable(DefaultTableModel model, List<Customer> customers) {
        model.setRowCount(0);
        for(Customer c : customers) {
            model.addRow(new Object[]{c.getId(), c.getName()});
        }
    }

    private static void refreshCustomerCombo(JComboBox<String> combo) {
        combo.removeAllItems();
        for(Customer c : customerRepo.getAll()) {
            combo.addItem(c.getName());
        }
    }

    private static void refreshProductCombo(JComboBox<String> combo) {
        combo.removeAllItems();
        for(Product p : productRepo.getAll()) {
            combo.addItem(p.getName());
        }
    }

    private static void addSampleProducts() {
        productRepo.add(new Product("P001","Milk","Dairy",100,50));
        productRepo.add(new Product("P002","Bread","Bakery",50,30));
        productRepo.add(new Product("P003","Eggs","Dairy",200,60));
        productRepo.add(new Product("P004","Butter","Dairy",150,40));
        productRepo.add(new Product("P005","Cheese","Dairy",250,30));
        productRepo.add(new Product("P006","Apple","Fruits",100,80));
        productRepo.add(new Product("P007","Banana","Fruits",60,100));
        productRepo.add(new Product("P008","Orange","Fruits",80,70));
        productRepo.add(new Product("P009","Rice","Grocery",90,200));
        productRepo.add(new Product("P010","Wheat Flour","Grocery",70,150));
        productRepo.add(new Product("P011","Sugar","Grocery",50,180));
        productRepo.add(new Product("P012","Tea","Beverages",120,60));
        saveData();
    }

    private static void saveData() {
        FileManager.save(productRepo.getAll(), PRODUCT_FILE);
        FileManager.save(customerRepo.getAll(), CUSTOMER_FILE);
    }

    private static void loadData() {
        File f1 = new File(PRODUCT_FILE);
        File f2 = new File(CUSTOMER_FILE);
        if(f1.exists()) productRepo.setAll(FileManager.load(PRODUCT_FILE));
        if(f2.exists()) customerRepo.setAll(FileManager.load(CUSTOMER_FILE));
    }

    public static Repository<Customer> getCustomerRepo() {
        return customerRepo;
    }
}
