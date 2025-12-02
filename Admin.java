package models;

import interfaces.Reportable;
import java.util.List;

public class Admin extends Person implements Reportable {

    public Admin(String id, String name) {
        super(id, name);
    }

    @Override
    public void generateReport(List<?> items) {
        System.out.println("----- Admin Report -----");
        for (Object obj : items) {
            System.out.println(obj.toString());
        }
        System.out.println("------------------------");
    }

    @Override
    public void exportToFile(String filename, List<?> items) {
        // Can implement file export logic here
        System.out.println("Report exported to " + filename);
    }

    @Override
    public void displayInfo() {
        System.out.println("Admin: " + id + " | " + name);
    }
}
