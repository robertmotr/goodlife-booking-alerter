package com.robert.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StartMain {

    static File chromeDriverFile;

    public static void main(String[] args) throws IOException {

        setChromeDriver();

        System.setProperty("webdriver.chrome.driver", chromeDriverFile.getAbsolutePath());

        Main.main(args);

    }

    public static void setChromeDriver() throws IOException {

        InputStream resource = StartMain.class.getResourceAsStream("/resources/lib/chromedriver/chromedriver.exe");

        byte[] byteArray = resource.readAllBytes();

        File f = new File("Driver");

        if (!f.exists()) {

            f.mkdirs();

        }

        File chromeDriver = new File("Driver" + File.separator + "chromedriver.exe");

        if (!chromeDriver.exists()) {

            chromeDriver.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(chromeDriver);

            fileOutputStream.write(byteArray);

            fileOutputStream.close();
        }

        resource.close();

        chromeDriverFile = chromeDriver;

    }
}
