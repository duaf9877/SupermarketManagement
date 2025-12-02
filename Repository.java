package repository;

import java.util.ArrayList;
import java.util.List;

public class Repository<T> {
    private List<T> items;

    public Repository() {
        items = new ArrayList<>();
    }

    public void add(T item) {
        items.add(item);
        System.out.println(item + " added to repository.");
    }

    public void remove(T item) {
        items.remove(item);
        System.out.println(item + " removed from repository.");
    }

    public List<T> getAll() {
        return items;
    }

    // NEW: Set all items at once
    public void setAll(List<T> newItems) {
        if(newItems != null) {
            this.items = newItems;
            System.out.println("Repository data loaded. Total items: " + items.size());
        }
    }
}
