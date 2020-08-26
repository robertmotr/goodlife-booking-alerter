package com.robert.check;

import com.robert.src.Booking;
import com.robert.src.Request;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CheckThreadable extends Thread {

    public CheckThreadable(Request requestInstance, int refreshRate, Booking booking, String section) {

        SystemTray tray = SystemTray.getSystemTray();
        CheckThreadable.tray = tray;

        request = requestInstance;

        this.refreshRate = refreshRate;

        this.booking = booking;

        this.section = section.strip();

    }

    private final String section;

    private static Request request;

    private static SystemTray tray;

    private Boolean loopFlag = true;

    private final int refreshRate;

    private final Booking booking;

    @Override
    public void run() {

        try {

            checkStatus();

        } catch (InterruptedException | IOException | AWTException e) {

            e.printStackTrace();

        }

    }

    public void disableCheck() {

        this.loopFlag = false;

    }

    private void checkStatus() throws InterruptedException, IOException, AWTException {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Goodlife Booking Alerter is now tracking for changes.");

        alert.setHeaderText("GBA is now looking for free spots.");

        alert.setContentText("GBA is now looking for free spots for: \n" +
                this.section + "\n" +
                this.booking.getTotalTime());

        alert.showAndWait();

        while(loopFlag) {

            TimeUnit.SECONDS.sleep(refreshRate);

            request.getSpecificJsonObject(booking.getArrayIndex(), section);

            Booking booking = request.getSpecificBooking();

            if(booking.getSpotsAvailable() > 0) {

                disableCheck();

                // notification

                Image image = Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/com/robert/resources/goodlife-logo.png"));

                TrayIcon trayIcon = new TrayIcon(image,"Goodlife Booking Alerter has found something.");

                tray.add(trayIcon);

                trayIcon.displayMessage("Goodlife Booking Alerter has found something.", "A spot(s) is now available at your desired booking!", TrayIcon.MessageType.INFO);

                System.exit(0);

            }
        }
    }
}
