package com.example.MongoSpring.Enity.users;

import com.example.MongoSpring.Enity.ImageUtils;

import javafx.stage.Stage;
import javafx.application.Application;



public class WebCamapp extends Application {

    @Override
    public void start(Stage primaryStage) {
        ImageUtils.startVideoCapture(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
