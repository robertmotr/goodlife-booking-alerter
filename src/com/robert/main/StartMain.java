package com.robert.main;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class StartMain {

    static File chromeDriverFile;

    public static void main(String[] args) throws IOException {

        setChromeDriver();

        System.setProperty("webdriver.chrome.driver", chromeDriverFile.getAbsolutePath());

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
}
