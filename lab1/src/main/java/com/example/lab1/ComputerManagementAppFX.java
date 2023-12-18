package com.example.lab1;

import com.example.lab1.PersonalComputer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Random;

public class ComputerManagementAppFX extends Application {
    private static PersonalComputer computer = null;
    private static final String FILE_NAME = "computer.txt";

    public static void main(String[] args) {
        loadFromFile();
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Computer Management App");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));

        // Add UI components (TextFields, CheckBoxes, etc.) to the grid for user input

        Button addComputerButton = new Button("Add a computer");
        addComputerButton.setOnAction(e -> addComputer(primaryStage, grid));

        Button editComputerButton = new Button("Edit a computer");
        editComputerButton.setOnAction(e -> editComputer(primaryStage, grid));

        Button saveToFileButton = new Button("Save to file");
        saveToFileButton.setOnAction(e -> saveToFile());

        Button displayComputersButton = new Button("Display computer");
        displayComputersButton.setOnAction(e -> displayComputers());

        Button deleteComputerButton = new Button("Delete computer");
        deleteComputerButton.setOnAction(e -> deleteComputer());

        grid.addRow(0, addComputerButton, editComputerButton, saveToFileButton);
        grid.addRow(1, displayComputersButton, deleteComputerButton);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void addComputer(Stage primaryStage, GridPane grid) {
        Dialog dialog = createDialog("Add Computer");

        // Add UI components to the dialog for user input
        ComboBox<String> cpuBrandTextField = new ComboBox<>();
        cpuBrandTextField.getItems().addAll(CPU.AMD.name(), CPU.APPLE.name(), CPU.INTEL.name());
        ComboBox<String> gpuBrandTextField = new ComboBox<>();
        gpuBrandTextField.getItems().addAll(GPU.AMD.name(), GPU.INTEL.name(), GPU.NVIDIA.name());
        Spinner<Integer> cpuSpeedTextField = new Spinner<>(0, Integer.MAX_VALUE, 0);
        CheckBox hasWifiCheckBox = new CheckBox("Has Wifi");
        ComboBox<String> storageTypeComboBox = new ComboBox<>();
        storageTypeComboBox.getItems().addAll(STORAGE_TYPE.HDD.name(), STORAGE_TYPE.SSD.name(), STORAGE_TYPE.COMBINED.name());
        ComboBox<String> ramTypeComboBox = new ComboBox<>();
        ramTypeComboBox.getItems().addAll(RAM_TYPE.DDR3.name(), RAM_TYPE.DDR4.name(), RAM_TYPE.DDR5.name());
        TextField powerConsumptionSpinner = new TextField();
        CheckBox isLaptopCheckBox = new CheckBox("Is Laptop");
        TextField priceTextField = new TextField();
        TextField storageSizeSpinner = new TextField();
        Spinner<Integer> ramSizeSpinner = new Spinner<>(0, Integer.MAX_VALUE, 0);


        // Add other fields as needed

        GridPane gridPane = new GridPane();
        gridPane.addRow(1, new Label("CPU Brand:"), cpuBrandTextField);
        gridPane.addRow(2, new Label("GPU Brand:"), gpuBrandTextField);
        gridPane.addRow(3, new Label("CPU Speed GHz:"), cpuSpeedTextField);
        gridPane.addRow(4, hasWifiCheckBox);
        gridPane.addRow(5, new Label("Storage Type:"), storageTypeComboBox);
        gridPane.addRow(6, new Label("RAM Type:"), ramTypeComboBox);
        gridPane.addRow(7, new Label("Power Consumption:"), powerConsumptionSpinner);
        gridPane.addRow(8, isLaptopCheckBox);
        gridPane.addRow(9, new Label("Price:"), priceTextField);
        gridPane.addRow(10, new Label("RAM Size GB:"), ramSizeSpinner);
        gridPane.addRow(11, new Label("Storage Size GB:"), storageSizeSpinner);

        dialog.getDialogPane().setContent(gridPane);
        Dialog message = createDialog("Add result");
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        dialog.getDialogPane().lookupButton(okButton).setDisable(false);
        if (computer == null) {
            dialog.showAndWait().ifPresent(result -> {
                if (result.equals(okButton)) {
                    try {
                        PersonalComputer computerTemp = new PersonalComputer();
                        setFields(cpuBrandTextField, gpuBrandTextField, cpuSpeedTextField, hasWifiCheckBox, storageTypeComboBox, ramTypeComboBox, powerConsumptionSpinner, isLaptopCheckBox, priceTextField, storageSizeSpinner, ramSizeSpinner, computerTemp);

                        computerTemp.setId(new Random().nextInt());
                        computer = computerTemp;
                        createMessageDialog(message, "Computer added successfully!");
                    } catch (Exception e) {
                        createMessageDialog(message, "There is an exception in data you provided: " + e.getMessage());
                        computer = null;
                    }
                }

            });
        } else {
            createMessageDialog(message, "Computer is already added; you can edit it!");
        }
    }

    private static void setFields(ComboBox<String> cpuBrandTextField, ComboBox<String> gpuBrandTextField, Spinner<Integer> cpuSpeedTextField, CheckBox hasWifiCheckBox, ComboBox<String> storageTypeComboBox, ComboBox<String> ramTypeComboBox, TextField powerConsumptionSpinner, CheckBox isLaptopCheckBox, TextField priceTextField, TextField storageSizeSpinner, Spinner<Integer> ramSizeSpinner, PersonalComputer computerTemp) {
        computerTemp.setCpuBrand(cpuBrandTextField.getValue());
        computerTemp.setGraphicCard(gpuBrandTextField.getValue());
        computerTemp.setCpuSpeedGHz(Double.valueOf(cpuSpeedTextField.getValue()));
        computerTemp.setHasWifi(hasWifiCheckBox.isSelected());
        computerTemp.setStorageType(storageTypeComboBox.getValue());
        computerTemp.setRamType(ramTypeComboBox.getValue());
        computerTemp.setPowerConsumption(Integer.valueOf(powerConsumptionSpinner.getText().isEmpty() ? "0" : powerConsumptionSpinner.getText()));
        computerTemp.setLaptop(isLaptopCheckBox.isSelected());
        computerTemp.setPrice(Double.parseDouble(priceTextField.getText()));
        computerTemp.setRamSizeGB(ramSizeSpinner.getValue());
        computerTemp.setStorageGB(Integer.valueOf(storageSizeSpinner.getText().isEmpty() ? "0" : storageSizeSpinner.getText()));
    }

    private static void createMessageDialog(Dialog message, String s) {
        message.setContentText(s);
        message.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        message.showAndWait().ifPresent(resultM -> {
            if (resultM.equals(ButtonType.CANCEL)) {
                message.close();
            }
        });
    }

    private void editComputer(Stage primaryStage, GridPane grid) {
        Dialog message = createDialog("Edit result");
        if (computer != null) {
            Dialog dialog = createDialog("Edit Computer");

            ComboBox<String> cpuBrandTextField = new ComboBox<>();
            cpuBrandTextField.getItems().addAll(CPU.AMD.name(), CPU.APPLE.name(), CPU.INTEL.name());
            ComboBox<String> gpuBrandTextField = new ComboBox<>();
            gpuBrandTextField.getItems().addAll(GPU.AMD.name(), GPU.INTEL.name(), GPU.NVIDIA.name());
            Spinner<Integer> cpuSpeedTextField = new Spinner<>(0, Integer.MAX_VALUE, 0);
            CheckBox hasWifiCheckBox = new CheckBox("Has Wifi");
            ComboBox<String> storageTypeComboBox = new ComboBox<>();
            storageTypeComboBox.getItems().addAll(STORAGE_TYPE.HDD.name(), STORAGE_TYPE.SSD.name(), STORAGE_TYPE.COMBINED.name());
            ComboBox<String> ramTypeComboBox = new ComboBox<>();
            ramTypeComboBox.getItems().addAll(RAM_TYPE.DDR3.name(), RAM_TYPE.DDR4.name(), RAM_TYPE.DDR5.name());
            TextField powerConsumptionSpinner = new TextField();
            CheckBox isLaptopCheckBox = new CheckBox("Is Laptop");
            TextField priceTextField = new TextField();
            TextField storageSizeSpinner = new TextField();
            Spinner<Integer> ramSizeSpinner = new Spinner<>(0, Integer.MAX_VALUE, 0);


            // Add other fields as needed

            GridPane gridPane = new GridPane();
            gridPane.addRow(1, new Label("CPU Brand:"), cpuBrandTextField);
            gridPane.addRow(2, new Label("GPU Brand:"), gpuBrandTextField);
            gridPane.addRow(3, new Label("CPU Speed GHz:"), cpuSpeedTextField);
            gridPane.addRow(4, hasWifiCheckBox);
            gridPane.addRow(5, new Label("Storage Type:"), storageTypeComboBox);
            gridPane.addRow(6, new Label("RAM Type:"), ramTypeComboBox);
            gridPane.addRow(7, new Label("Power Consumption:"), powerConsumptionSpinner);
            gridPane.addRow(8, isLaptopCheckBox);
            gridPane.addRow(9, new Label("Price:"), priceTextField);
            gridPane.addRow(10, new Label("RAM Size GB:"), ramSizeSpinner);
            gridPane.addRow(11, new Label("Storage Size GB:"), storageSizeSpinner);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
            dialog.getDialogPane().lookupButton(okButton).setDisable(false);

            dialog.showAndWait().ifPresent(result -> {
                if (result.equals(okButton)) {
                    try {
                        setFields(cpuBrandTextField, gpuBrandTextField,
                                cpuSpeedTextField, hasWifiCheckBox,
                                storageTypeComboBox, ramTypeComboBox,
                                powerConsumptionSpinner, isLaptopCheckBox, priceTextField,
                                storageSizeSpinner, ramSizeSpinner, computer);

                        createMessageDialog(message, "Computer edited successfully!");
                    } catch (Exception e) {
                        createMessageDialog(message, "There is an exception in data you provided: " + e.getMessage());
                    }
                }

            });
        } else {
            createMessageDialog(message, "Computer is not added; you cannot edit it!");
        }
    }

    private Dialog<String> createDialog(String title) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);

        return dialog;
    }

    private void saveToFile() {
        Dialog message = createDialog("Save Result");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            outputStream.writeObject(computer);
            createMessageDialog(message, "Data saved to file successfully!");
        } catch (IOException e) {
            createMessageDialog(message, "Error saving data to file: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            computer = (PersonalComputer) inputStream.readObject();
            System.out.println("Data loaded from file successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data found. Starting with an empty computer.");
        }
    }

    private void displayComputers() {
        Dialog dialog = createDialog("Display computer");
        if (computer == null) {
            createMessageDialog(dialog, "No computer to display.");
        } else {
            createMessageDialog(dialog, String.valueOf(computer));
        }
    }

    private void deleteComputer() {
        computer = null;
        Dialog message = createDialog("Delete dialog");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            outputStream.writeObject(computer);
            createMessageDialog(message, "Data deleted from file successfully!");
        } catch (IOException e) {
            createMessageDialog(message, "Error deleting data from file: " + e.getMessage());
        }
    }
}
