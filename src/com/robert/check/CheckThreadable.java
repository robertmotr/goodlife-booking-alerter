package com.robert.check;

import com.robert.src.Booking;
import com.robert.src.Request;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CheckThreadable extends Thread {

    public CheckThreadable(Request requestInstance, int refreshRate, Booking booking, String goodlifeApiRequest, String section) {

        SystemTray tray = SystemTray.getSystemTray();
        this.tray = tray;

        request = requestInstance;

        this.refreshRate = refreshRate;

        this.booking = booking;

        this.goodlifeApiRequest = goodlifeApiRequest;

        this.section = section.strip();

    }

    private String section;

    private static Request request;

    private static SystemTray tray;

    private Boolean loopFlag = true;

    private int refreshRate;

    private Booking booking;

    private String goodlifeApiRequest;

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

        while(loopFlag) {

            TimeUnit.SECONDS.sleep(refreshRate);

            request.getSpecificJsonObject(booking.getArrayIndex(), section);

            Booking booking = request.getSpecificBooking();

            if(booking.getSpotsAvailable() > 0) {

                disableCheck();

                // notification

                Image image = Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/goodlife-logo.png"));

                TrayIcon trayIcon = new TrayIcon(image,"Goodlife Booking Alerter has found something.");

                tray.add(trayIcon);

                trayIcon.displayMessage("Goodlife Booking Alerter has found something.", "A spot(s) is now available at your desired booking!", TrayIcon.MessageType.INFO);

            }
        }
    }
}
