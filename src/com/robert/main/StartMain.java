package com.robert.main;

import com.robert.main.Main;

public class StartMain {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "lib/chromedriver/chromedriver.exe");
        System.setProperty("webdriver.chrome.bin", "lib/chrome/chrome.exe");

        Main.main(args);

    }


}
