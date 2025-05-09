import java.io.*;
import java.util.*;

public class UserInterface {
    private Dealership dealership;
    private Scanner scanner = new Scanner(System.in);

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

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
        clearScreen();

        // ANSI color codes
        final String RESET  = "\u001B[0m";
        final String CYAN   = "\u001B[36m";
        final String YELLOW = "\u001B[33m";
        final String GREEN  = "\u001B[32m";
        final String PURPLE = "\u001B[35m";
        final String BOLD   = "\u001B[1m";

        System.out.println(PURPLE + BOLD + "╔══ ✰ ══ ✮ :: ✮ ══ ✰ ══╗" + RESET);
        System.out.println(PURPLE + BOLD + "     D & B USED CARS     " + RESET);
        System.out.println(PURPLE + BOLD + "╚══ ✰ ══ ✮ :: ✮ ══ ✰ ══╝" + RESET);

        System.out.println(CYAN + "\n→ DEALERSHIP : " + RESET + dealership.getName());
        System.out.println(CYAN + "→ ADDRESS    : " + RESET + dealership.getAddress());
        System.out.println(CYAN + "→ PHONE      : " + RESET + dealership.getPhone());

        System.out.println(YELLOW + "\n╔════════════════════════════════════════════════════╗");
        System.out.println("║                    MAIN MENU                       ║");
        System.out.println("╠════╦═══════════════════════════════════════════════╣" + RESET);
        System.out.printf("%s║ %-2s ║ %-45s ║%s\n", YELLOW, "1",  "Search vehicles by price range", RESET);
        System.out.printf("%s║ %-2s ║ %-45s ║%s\n", YELLOW, "2",  "Search by make and model", RESET);
        System.out.printf("%s║ %-2s ║ %-45s ║%s\n", YELLOW, "3",  "Search by year range", RESET);
        System.out.printf("%s║ %-2s ║ %-45s ║%s\n", YELLOW, "4",  "Search by color", RESET);
        System.out.printf("%s║ %-2s ║ %-45s ║%s\n", YELLOW, "5",  "Search by mileage range", RESET);
        System.out.printf("%s║ %-2s ║ %-45s ║%s\n", YELLOW, "6",  "Search by vehicle type", RESET);
        System.out.printf("%s║ %-2s ║ %-45s ║%s\n", YELLOW, "7",  "List all vehicles", RESET);
        System.out.printf("%s║ %-2s ║ %-45s ║%s\n", YELLOW, "8",  "Add a vehicle (EMPLOYEE ONLY)", RESET);
        System.out.printf("%s║ %-2s ║ %-45s ║%s\n", YELLOW, "9",  "Remove a vehicle (EMPLOYEE ONLY)", RESET);
        System.out.printf("%s║ %-2s ║ %-45s ║%s\n", YELLOW, "99", "Exit program", RESET);
        System.out.println(YELLOW + "╚════╩═══════════════════════════════════════════════╝" + RESET);

        System.out.print(GREEN + "\nSelect an option ➤ " + RESET);
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
