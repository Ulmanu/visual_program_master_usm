package com.example.lab3;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MDIApp extends Application {
    private Stage primaryStage;
    private BorderPane root;
    private static List<PersonalComputer> computers = new ArrayList<>();
    private static final String FILE_NAME = "computers.txt";
    VBox internalWindowsContainer = new VBox();

    public static void main(String[] args) {
        loadFromFile();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MDI App");

        root = new BorderPane();

        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        internalWindowsContainer.setAlignment(Pos.CENTER);
        Label label = new Label("Computer Management app");
        root.setCenter(label);
        Image image = new Image("img.png"); // Replace with the path to your image file
        // Create an ImageView to display the image
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        root.setRight(imageView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        // Display existing computers
//        for (PersonalComputer computer : computers) {
//            createInternalWindow(computer);
//        }

        primaryStage.show();
    }

    private static void loadFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            List<PersonalComputer> loadedComputers = (List<PersonalComputer>) inputStream.readObject();
            computers.addAll(loadedComputers);
            System.out.println("Data loaded from file successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data found. Starting with an empty list of computers.");
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            outputStream.writeObject(computers);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save result");
            alert.setHeaderText(null);
            alert.setContentText("Data saved to file successfully!");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save result");
            alert.setHeaderText(null);
            alert.setContentText("Error saving data to file: " + e.getMessage());
            alert.showAndWait();
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
        fileMenuItem.setOnAction(e -> saveToFile());
        newMenuItem.setOnAction(e -> createInternalAddWindow());
        edit.setOnAction(e -> createInternalEditWindow());
        delete.setOnAction(e -> createInternalDeleteWindow());
        exit.setOnAction(e -> handleExit());
        display.setOnAction(e -> createInternalDisplayWindow());

        computer.getItems().addAll(fileMenuItem, newMenuItem, edit, delete, display, exit);
        menuBar.getMenus().add(computer);
        return menuBar;
    }

    private void createInternalAddWindow() {
        Stage internalStage = new Stage();
        internalStage.setTitle("Add Computer");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Result result = getResult(gridPane);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add result");
        alert.setHeaderText(null);


        result.submit().setOnAction(actionEvent -> {
            try {
                PersonalComputer computerTemp = new PersonalComputer();
                setFields(result.cpuBrandTextField(), result.gpuBrandTextField(), result.cpuSpeedTextField(), result.hasWifiCheckBox(), result.storageTypeComboBox(), result.ramTypeComboBox(), result.powerConsumptionSpinner(), result.isLaptopCheckBox(), result.priceTextField(), result.storageSizeSpinner(), result.ramSizeSpinner(), computerTemp);
                computerTemp.setId(new Random().nextInt());
                computers.add(computerTemp);
                alert.setContentText("Computer added successfully!");
                alert.showAndWait();
            } catch (Exception e) {
                alert.setContentText("There is an exception in data you provided: " + e.getMessage());
                alert.showAndWait();
            }
        });

        Scene internalScene = new Scene(gridPane, 600, 500);
        internalStage.setScene(internalScene);

        // Add the internal window to the internal windows container
        StackPane internalWindow = new StackPane();
        internalWindowsContainer.getChildren().clear();
        internalWindowsContainer.getChildren().add(internalWindow);

        // Show the internal window
        internalStage.show();
    }

    private void createInternalEditWindow() {
        Stage internalStage = new Stage();
        internalStage.setTitle("Edit computer");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Result result = getResult(gridPane);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit result");
        alert.setHeaderText(null);
        if (computers.isEmpty()) {
            alert.setContentText("Computer is not added; you cannot edit it!");
            alert.showAndWait();
        } else {
            ComboBox<Integer> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(computers.stream().map(PersonalComputer::getId).toList());
            gridPane.addRow(0, comboBox);
            result.submit().setOnAction(actionEvent -> {
                try {
                    PersonalComputer computerTemp = computers.stream().filter(c -> c.getId() == comboBox.getValue()).findFirst()
                            .orElseThrow();
                    setFields(result.cpuBrandTextField(), result.gpuBrandTextField(), result.cpuSpeedTextField(), result.hasWifiCheckBox(), result.storageTypeComboBox(), result.ramTypeComboBox(), result.powerConsumptionSpinner(), result.isLaptopCheckBox(), result.priceTextField(), result.storageSizeSpinner(), result.ramSizeSpinner(), computerTemp);
                    alert.setContentText("Computer edited successfully!");
                    alert.showAndWait();
                } catch (Exception e) {
                    alert.setContentText("There is an exception in data you provided: " + e.getMessage());
                    alert.showAndWait();
                }
            });

            Scene internalScene = new Scene(gridPane, 600, 500);
            internalStage.setScene(internalScene);

            // Add the internal window to the internal windows container
            StackPane internalWindow = new StackPane();
            internalWindowsContainer.getChildren().clear();
            internalWindowsContainer.getChildren().add(internalWindow);

            // Show the internal window
            internalStage.show();
        }

    }

    private void createInternalDisplayWindow() {
        Stage internalStage = new Stage();
        internalStage.setTitle("Display computer");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit result");
        alert.setHeaderText(null);
        if (computers.isEmpty()) {
            alert.setContentText("Computer is not added; you cannot display it!");
            alert.showAndWait();
        } else {
            Label label = new Label();
            ComboBox<Integer> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(computers.stream().map(PersonalComputer::getId).toList());
            Button submit = new Button("Submit");
            gridPane.addRow(2, submit);
            gridPane.addRow(0, comboBox);

            gridPane.addRow(1, label);

            submit.setOnAction(e -> {
                PersonalComputer computerTemp = computers.stream().filter(c -> c.getId() == comboBox.getValue()).findFirst()
                        .orElseThrow();
                label.setText(String.valueOf(computerTemp));
            });

            Scene internalScene = new Scene(gridPane, 600, 500);
            internalStage.setScene(internalScene);

            // Add the internal window to the internal windows container
            StackPane internalWindow = new StackPane();
            internalWindowsContainer.getChildren().clear();
            internalWindowsContainer.getChildren().add(internalWindow);

            // Show the internal window
            internalStage.show();
        }

    }

    private void createInternalDeleteWindow() {
        Stage internalStage = new Stage();
        internalStage.setTitle("Delete computer");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Delete result");
        alert.setHeaderText(null);
        if (computers.isEmpty()) {
            alert.setContentText("Computer is not added; you cannot delete it!");
            alert.showAndWait();
        } else {
            ComboBox<Integer> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(computers.stream().map(PersonalComputer::getId).toList());
            Button submit = new Button("Submit");
            gridPane.addRow(2, submit);
            gridPane.addRow(0, comboBox);


            submit.setOnAction(e -> {
                PersonalComputer computerTemp = computers.stream().filter(c -> c.getId() == comboBox.getValue()).findFirst()
                        .orElseThrow();
                computers.remove(computerTemp);
                alert.setContentText("Data deleted from file successfully!");
                alert.showAndWait();

            });

            Scene internalScene = new Scene(gridPane, 600, 500);
            internalStage.setScene(internalScene);

            // Add the internal window to the internal windows container
            StackPane internalWindow = new StackPane();
            internalWindowsContainer.getChildren().clear();
            internalWindowsContainer.getChildren().add(internalWindow);

            // Show the internal window
            internalStage.show();
        }

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

    private void handleExit() {
        primaryStage.close();
    }
}

