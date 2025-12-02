package utils;

import java.io.*;
import java.util.List;

public class FileManager {

    // Generic save method
    public static <T> void save(List<T> list, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(list);
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Generic load method
    public static <T> List<T> load(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) in.readObject();
        } catch (Exception e) {
            System.out.println("Error loading data from " + filename + ": " + e.getMessage());
            return null;
        }
    }
}
