package interfaces;

import java.util.List;

public interface Reportable {
    void generateReport(List<?> items);
    void exportToFile(String filename, List<?> items);
}
