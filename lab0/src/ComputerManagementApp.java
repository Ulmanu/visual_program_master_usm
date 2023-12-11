import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class ComputerManagementApp {
    private static PersonalComputer computer = null;
    private static final String FILE_NAME = "computer.txt";

    public static void main(String[] args) {
        loadFromFile();

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add a computer");
            System.out.println("2. Edit a computer");
            System.out.println("3. Save to file");
            System.out.println("4. Display computer");
            System.out.println("5. Delete computer");
            System.out.println("6. Exit");

            int choice = getUserChoice();

            switch (choice) {
                case 1 -> addComputer();
                case 2 -> editComputer();
                case 3 -> saveToFile();
                case 4 -> displayComputers();
                case 5 -> deleteComputer();
                case 6 -> {
                    saveToFile();
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private static void addComputer() {
        Scanner scanner = new Scanner(System.in);
        if (computer == null) {
            try {
                PersonalComputer computerTemp = new PersonalComputer();
                setComputerFields(scanner, computerTemp);
                computer = computerTemp;
                computer.setId(new Random().nextInt());
                System.out.println("Computer added successfully!");
            } catch (Exception e) {
                System.out.println("There is an exception in data you provided: " + e.getMessage());
                computer = null;
            }
        } else {
            System.out.println("Computer is already added you can edit it!");
        }
    }

    private static void editComputer() {
        Scanner scanner = new Scanner(System.in);
        if (computer != null) {
            try {
                setComputerFields(scanner, computer);
                System.out.println("Computer was edited successfully!");
            } catch (Exception e) {
                System.out.println("There is an exception in data you provided: " + e.getMessage());
            }
        } else {
            System.out.println("Computer is not added you cannot edit it!");
        }

    }

    private static void setComputerFields(Scanner scanner, PersonalComputer computer) {
        System.out.print("Enter CPU Brand: ");
        computer.setCpuBrand(scanner.next());
        System.out.print("Enter GPU Brand: ");
        computer.setGraphicCard(scanner.next());
        System.out.print("Enter CPU Speed GHz: ");
        computer.setCpuSpeedGHz(scanner.nextDouble());
        System.out.print("Enter has wifi: ");
        computer.setHasWifi(scanner.nextBoolean());
        System.out.print("Enter storage type: ");
        computer.setStorageType(scanner.next());
        System.out.print("Enter RAM type: ");
        computer.setRamType(scanner.next());
        System.out.print("Enter power consumption: ");
        computer.setPowerConsumption(scanner.nextInt());
        System.out.print("Enter is laptop: ");
        computer.setLaptop(scanner.nextBoolean());
        System.out.print("Enter price: ");
        computer.setPrice(scanner.nextDouble());
        System.out.print("Enter RAM size GB: ");
        computer.setRamSizeGB(scanner.nextInt());
        System.out.print("Enter storage size GB: ");
        computer.setStorageGB(scanner.nextInt());
    }


    private static void saveToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            outputStream.writeObject(computer);
            System.out.println("Data saved to file successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            computer = (PersonalComputer) inputStream.readObject();
            System.out.println("Data loaded from file successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data found. Starting with an empty list.");
        }
    }

    private static void displayComputers() {
        if (computer == null) {
            System.out.println("No computer to display.");
        } else {
            System.out.println(computer);
        }
    }

    private static void deleteComputer() {
        computer = null;
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            outputStream.writeObject(computer);
            System.out.println("Data saved to file successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }
}