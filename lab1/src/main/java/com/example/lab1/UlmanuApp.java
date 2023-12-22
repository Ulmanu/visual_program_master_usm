package com.example.lab1;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UlmanuApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        UlmanuDlg timerApp = new UlmanuDlg();
        timerApp.start(primaryStage);
    }

    class UlmanuDlg {
        private Timeline timeline;
        private Label UlmanuText;
        private ProgressBar UlmanuEvolut;
        private Button UlmanuButon;
        private Date startTime = new Date();
        long elapsedSeconds;


        public void start(Stage stage) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String currentTime = sdf.format(new Date());
            UlmanuText = new Label(currentTime);
            UlmanuEvolut = new ProgressBar();
            UlmanuButon = new Button("Start timer");

            VBox root = new VBox(10);
            root.setPadding(new Insets(10));
            root.getChildren().addAll(UlmanuText, UlmanuEvolut, UlmanuButon);

            UlmanuButon.setOnAction(event -> toggleTimer());

            Scene scene = new Scene(root, 300, 200);
            stage.setTitle("Timer App");
            stage.setScene(scene);
            stage.show();

            initializeTimer();
        }

        private void initializeTimer() {
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateUI()));
            timeline.setCycleCount(Animation.INDEFINITE);
        }

        private void toggleTimer() {

            if (timeline.getStatus() == Animation.Status.RUNNING) {
                timeline.pause();
                UlmanuButon.setText("Start timer");
            } else {
                timeline.play();
                UlmanuButon.setText("Stop timer");
            }
        }

        long lTime = System.currentTimeMillis();

        private void updateUI() {
            updateTextLabel();
            lTime += 1000;
            updateProgressBar(lTime);
        }

        private void updateTextLabel() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String currentTime = sdf.format(new Date());
            UlmanuText.setText("Current Time: " + currentTime);
        }

        private void updateProgressBar(long launchTime) {
            elapsedSeconds = (launchTime - startTime.getTime()) / 100;
            UlmanuEvolut.setProgress((double) elapsedSeconds / (77 * 3));
        }

    }
}
