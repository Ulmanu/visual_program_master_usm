package com.example.lab2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Random;

public class SDIApp extends Application {
    private Stage primaryStage;
    private BorderPane root;
    private static PersonalComputer computer = null;
    private static final String FILE_NAME = "computer.txt";

    public static void main(String[] args) {
        loadFromFile();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SDI App");

        root = new BorderPane();

        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // Initialize your content area (e.g., a TextArea)
        Label label = new Label("Computer Management app");
        root.setCenter(label);
        Image image = new Image("img.png"); // Replace with the path to your image file
        // Create an ImageView to display the image
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        root.setRight(imageView);


        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void loadFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            computer = (PersonalComputer) inputStream.readObject();
            System.out.println("Data loaded from file successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data found. Starting with an empty computer.");
        }
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu computer = new Menu("Computer management");
        MenuItem fileMenuItem = new MenuItem("SaveFile");
        MenuItem newMenuItem = new MenuItem("New");
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");
        MenuItem display = new MenuItem("Display");
        MenuItem exit = new MenuItem("Exit");

        // Set actions for menu items
        fileMenuItem.setOnAction(e -> handleSave());
        newMenuItem.setOnAction(e -> handleNew());
        edit.setOnAction(e -> handleEdit());
        delete.setOnAction(e -> handleDelete());
        exit.setOnAction(e -> handleExit());
        display.setOnAction(e -> handleDisplay());

        computer.getItems().addAll(fileMenuItem, newMenuItem, edit, delete, display, exit);
        menuBar.getMenus().add(computer);

        return menuBar;
    }

    private void handleNew() {
        primaryStage.setTitle("New Computer");
        Label label = new Label();
        root.setCenter(null);
        root.setRight(label);
        GridPane gridPane = new GridPane();
        Result result = getResult(gridPane);

        if (computer == null) {
            root.setCenter(gridPane);
        } else {
            label.setText("Computer is already added; you can edit it!");
        }


        result.submit().setOnAction(actionEvent -> {
            try {
                PersonalComputer computerTemp = new PersonalComputer();
                setFields(result.cpuBrandTextField(), result.gpuBrandTextField(), result.cpuSpeedTextField(), result.hasWifiCheckBox(), result.storageTypeComboBox(), result.ramTypeComboBox(), result.powerConsumptionSpinner(), result.isLaptopCheckBox(), result.priceTextField(), result.storageSizeSpinner(), result.ramSizeSpinner(), computerTemp);
                computerTemp.setId(new Random().nextInt());
                computer = computerTemp;
                label.setText("Computer added successfully!");
            } catch (Exception e) {
                label.setText("There is an exception in data you provided: " + e.getMessage());
                computer = null;
            }
        });
    }

    private static Result getResult(GridPane gridPane) {
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
        Button submit = new Button("Submit");
        gridPane.addRow(12, submit);
        Result result = new Result(cpuBrandTextField, gpuBrandTextField, cpuSpeedTextField, hasWifiCheckBox, storageTypeComboBox, ramTypeComboBox, powerConsumptionSpinner, isLaptopCheckBox, priceTextField, storageSizeSpinner, ramSizeSpinner, submit);
        return result;
    }

    private record Result(ComboBox<String> cpuBrandTextField, ComboBox<String> gpuBrandTextField,
                          Spinner<Integer> cpuSpeedTextField, CheckBox hasWifiCheckBox,
                          ComboBox<String> storageTypeComboBox, ComboBox<String> ramTypeComboBox,
                          TextField powerConsumptionSpinner, CheckBox isLaptopCheckBox, TextField priceTextField,
                          TextField storageSizeSpinner, Spinner<Integer> ramSizeSpinner, Button submit) {
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

    private void handleEdit() {
        primaryStage.setTitle("Edit Computer");
        Label label = new Label();
        root.setCenter(null);
        root.setRight(label);
        if (computer != null) {

            GridPane gridPane = new GridPane();
            Result result = getResult(gridPane);
            root.setCenter(gridPane);


            result.submit().setOnAction(actionEvent -> {
                try {
                    PersonalComputer computerTemp = new PersonalComputer();
                    setFields(result.cpuBrandTextField(), result.gpuBrandTextField(), result.cpuSpeedTextField(), result.hasWifiCheckBox(), result.storageTypeComboBox(), result.ramTypeComboBox(), result.powerConsumptionSpinner(), result.isLaptopCheckBox(), result.priceTextField(), result.storageSizeSpinner(), result.ramSizeSpinner(), computerTemp);
                    computerTemp.setId(new Random().nextInt());
                    computer = computerTemp;
                    label.setText("Computer edited successfully!");
                } catch (Exception e) {
                    label.setText("There is an exception in data you provided: " + e.getMessage());
                    computer = null;
                }
            });
        } else {
            label.setText("Computer is not added; you cannot edit it!");
        }
    }

    private void handleSave() {
        primaryStage.setTitle("Save computer");
        Label label = new Label();
        root.setCenter(null);
        root.setRight(label);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            outputStream.writeObject(computer);
            label.setText("Data saved to file successfully!");
        } catch (IOException e) {
            label.setText("Error saving data to file: " + e.getMessage());
        }
    }

    private void handleExit() {
        primaryStage.close();
    }

    private void handleDisplay() {
        primaryStage.setTitle("Display Computer");
        Label label = new Label();
        root.setCenter(null);
        root.setRight(label);
        if (computer == null) {
            label.setText("No computer to display.");
        } else {
            label.setText(String.valueOf(computer));
        }
    }

    private void handleDelete() {
        computer = null;
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            outputStream.writeObject(computer);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Data deleted from file successfully!");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Error deleting data from file: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
