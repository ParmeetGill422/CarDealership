import java.io.*;
import java.util.*;

public class UserInterface {
    private Dealership dealership;
    private Scanner scanner = new Scanner(System.in);

    public void display() {
        init();
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": displayVehicles(dealership.getVehiclesByPrice(promptDouble("Min price"), promptDouble("Max price"))); break;
                case "2": displayVehicles(dealership.getVehiclesByMakeModel(prompt("Make"), prompt("Model"))); break;
                case "3": displayVehicles(dealership.getVehiclesByYear(promptInt("Min year"), promptInt("Max year"))); break;
                case "4": displayVehicles(dealership.getVehiclesByColor(prompt("Color"))); break;
                case "5": displayVehicles(dealership.getVehiclesByMileage(promptInt("Min mileage"), promptInt("Max mileage"))); break;
                case "6": displayVehicles(dealership.getVehiclesByType(prompt("Type"))); break;
                case "7": displayVehicles(dealership.getAllVehicles()); break;
                case "8":
                    if (authenticateEmployee()) addVehicle();
                    break;
                case "9":
                    if (authenticateEmployee()) removeVehicle();
                    break;
                case "99":
                    new DealershipFileManager().saveDealership(dealership);
                    running = false;
                    break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void init() {
        DealershipFileManager dfm = new DealershipFileManager();
        dealership = dfm.getDealership();
    }

    private void displayMenu() {
        System.out.println("\n===============================");
        System.out.println("  Welcome to " + dealership.getName());
        System.out.println("  Address: " + dealership.getAddress());
        System.out.println("  Phone:   " + dealership.getPhone());
        System.out.println("===============================\n");
        System.out.println("\n--- Dealership Menu ---");
        System.out.println("1 - Find vehicles within a price range");
        System.out.println("2 - Find vehicles by make / model");
        System.out.println("3 - Find vehicles by year range");
        System.out.println("4 - Find vehicles by color");
        System.out.println("5 - Find vehicles by mileage range");
        System.out.println("6 - Find vehicles by type");
        System.out.println("7 - List ALL vehicles");
        System.out.println("8 - Add a vehicle (employee only)");
        System.out.println("9 - Remove a vehicle (employee only)");
        System.out.println("99 - Quit");
        System.out.print("Enter your choice: ");
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.printf("%-10s | %-4s | %-10s | %-10s | %-7s | %-10s | %-10s | %-10s\n",
                "VIN", "Year", "Make", "Model", "Type", "Color", "Mileage", "Price");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Vehicle v : vehicles) {
            System.out.printf("%-10d | %-4d | %-10s | %-15s | %-7s | %-10s | %,10d | $%,9.2f\n",
                    v.getVin(), v.getYear(), v.getMake(), v.getModel(), v.getType(),
                    v.getColor(), v.getOdometer(), v.getPrice());
        }
    }


    private void addVehicle() {
        int vin = promptInt("VIN");
        int year = promptInt("Year");
        String make = prompt("Make");
        String model = prompt("Model");
        String type = prompt("Type");
        String color = prompt("Color");
        int odometer = promptInt("Odometer");
        double price = promptDouble("Price");
        dealership.addVehicle(new Vehicle(vin, year, make, model, type, color, odometer, price));
        System.out.println("Vehicle added.");
        logAction("Added vehicle: VIN " + vin);
    }

    private void removeVehicle() {
        int vin = promptInt("Enter VIN to remove");
        dealership.removeVehicle(vin);
        System.out.println("Vehicle removed if it existed.");
        logAction("Removed vehicle: VIN " + vin);
    }

    private boolean authenticateEmployee() {
        System.out.print("Enter employee PIN: ");
        String input = scanner.nextLine();
        if (input.equals("6969")) {
            return true;
        } else {
            System.out.println("Access denied. Invalid PIN.");
            return false;
        }
    }

    private void logAction(String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter("log.txt", true))) {
            out.println(new Date() + " - " + message);
        } catch (IOException e) {
            System.out.println("Logging failed: " + e.getMessage());
        }
    }

    private String prompt(String msg) {
        System.out.print(msg + ": ");
        return scanner.nextLine();
    }

    private int promptInt(String msg) {
        System.out.print(msg + ": ");
        return Integer.parseInt(scanner.nextLine());
    }

    private double promptDouble(String msg) {
        System.out.print(msg + ": ");
        return Double.parseDouble(scanner.nextLine());
    }
}
