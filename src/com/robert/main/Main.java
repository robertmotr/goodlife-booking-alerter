package com.robert.main;

import com.robert.src.Controller;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Alert Alert = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION);

        Alert.setTitle("Goodlife Booking Alerter");
        Alert.setHeaderText("Select where Chrome Beta/Chromium's chrome.exe is located.");
        Alert.setContentText("Ensure that the file location is not a shortcut but is inside its installation folder.");
        Alert.showAndWait();

        FileChooser fileChooser = new FileChooser();

        File chromeBinary = fileChooser.showOpenDialog(primaryStage);

        setChromeDriver();

        primaryStage.close();

        this.controller = new Controller(chromeBinary);

    }

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void stop() {

        controller.closeDriver();

        System.exit(0);

    }

    public void setChromeDriver() throws IOException {

        InputStream resource = Main.class.getResourceAsStream("/com/robert/resources/lib/chromedriver/chromedriver.exe");

        byte[] byteArray = resource.readAllBytes();

        File f = new File("GBA");

        if (!f.exists()) {

            f.mkdirs();

        }

        File chromeDriver = new File("GBA" + File.separator + "chromedriver.exe");

        if (!chromeDriver.exists()) {

            chromeDriver.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(chromeDriver);

            fileOutputStream.write(byteArray);

            fileOutputStream.close();
        }

        resource.close();

        System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());

    }
}
