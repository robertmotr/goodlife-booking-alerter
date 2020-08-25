package com.robert.main;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class StartMain {

    static File chromeDriverFile;

    static File chromeFile;

    public static void main(String[] args) throws IOException {

        setChromeDriver();
        setChrome();

        System.setProperty("webdriver.chrome.driver", chromeDriverFile.getAbsolutePath());
        System.setProperty("webdriver.chrome.bin", chromeFile.getAbsolutePath());


        Main.main(args);

    }

    public static void setChromeDriver() throws IOException {

        ClassLoader classLoader = StartMain.class.getClassLoader();

        URL resource = classLoader.getResource("com/robert/lib/chromedriver/chromedriver.exe");

        File f = new File("Driver");

        if (!f.exists()) {

            f.mkdirs();

        }

        File chromeDriver = new File("Driver" + File.separator + "chromedriver.exe");

        if (!chromeDriver.exists()) {

            chromeDriver.createNewFile();

            org.apache.commons.io.FileUtils.copyURLToFile(resource, chromeDriver);

        }

        chromeDriverFile = chromeDriver;

    }

    public static void setChrome() throws IOException {

        ClassLoader classLoader = StartMain.class.getClassLoader();

        URL resource = classLoader.getResource("com/robert/lib/chrome/chrome.exe");

        File f = new File("Chrome");

        if (!f.exists()) {

            f.mkdirs();

        }

        File chrome = new File("Chrome" + File.separator + "chrome.exe");

        if (!chrome.exists()) {

            chrome.createNewFile();

            org.apache.commons.io.FileUtils.copyURLToFile(resource, chrome);

        }

        chromeFile = chrome;



    }


}
