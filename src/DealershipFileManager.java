import java.io.*;
import java.util.*;

public class DealershipFileManager {
    public Dealership getDealership() {
        Dealership dealership = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("inventory.csv"))) {
            String[] dealershipData = reader.readLine().split("\\|");
            dealership = new Dealership(dealershipData[0], dealershipData[1], dealershipData[2]);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] v = line.split("\\|");
                Vehicle vehicle = new Vehicle(
                        Integer.parseInt(v[0]),
                        Integer.parseInt(v[1]),
                        v[2], v[3], v[4], v[5],
                        Integer.parseInt(v[6]),
                        Double.parseDouble(v[7])
                );
                dealership.addVehicle(vehicle);
            }
        } catch (IOException e) {
            System.out.println("File read error: " + e.getMessage());
        }
        return dealership;
    }

    public void saveDealership(Dealership dealership) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("inventory.csv"))) {
            writer.printf("%s|%s|%s\n", dealership.getName(), dealership.getAddress(), dealership.getPhone());
            for (Vehicle v : dealership.getAllVehicles()) {
                writer.printf("%d|%d|%s|%s|%s|%s|%d|%.2f\n",
                        v.getVin(), v.getYear(), v.getMake(), v.getModel(),
                        v.getType(), v.getColor(), v.getOdometer(), v.getPrice());
            }
        } catch (IOException e) {
            System.out.println("File write error: " + e.getMessage());
        }
    }
}
