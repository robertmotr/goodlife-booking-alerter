package com.robert.driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;


@Deprecated
public class DriverThread extends Thread {

    public DriverThread(File chromeBinary) {

        this.chromeBinary = chromeBinary;

    }

    private File chromeBinary;

    public static ChromeDriver chromeDriver;

    @Override
    public void run() {

        ChromeOptions options = new ChromeOptions();

        options.setBinary(chromeBinary);

        options.addArguments("--headless");

        chromeDriver = new ChromeDriver(options);

        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
