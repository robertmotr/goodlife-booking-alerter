package com.robert.main;

import com.robert.src.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Controller main = new Controller();

        this.controller = main;

    }

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void stop() {

        controller.closeDriver();

        System.exit(0);

    }
}
