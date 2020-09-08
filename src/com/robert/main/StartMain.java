package com.robert.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StartMain {

    static File chromeDriverFile;

    static File chromeBinFile;

    public static void main(String[] args) throws IOException {

        setChromeDriver();

        setChrome();

        System.setProperty("webdriver.chrome.driver", chromeDriverFile.getAbsolutePath());

        Main.main(args);

    }

    public static void setChromeDriver() throws IOException {

        InputStream resource = StartMain.class.getResourceAsStream("/com/robert/resources/lib/chromedriver/chromedriver.exe");

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

    public static void setChrome() throws IOException {

        InputStream resource = StartMain.class.getResourceAsStream("/com/robert/resources/lib/chromebin/chrome.exe");

        byte[] byteArray = resource.readAllBytes();

        File f = new File("ChromeBin");

        if (!f.exists()) {

            f.mkdirs();

        }

        File chromeBin = new File("ChromeBin" + File.separator + "chrome.exe");

        if (!chromeBin.exists()) {

            chromeBin.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(chromeBin);

            fileOutputStream.write(byteArray);

            fileOutputStream.close();
        }

        resource.close();

        chromeBinFile = chromeBin;




    }
}
