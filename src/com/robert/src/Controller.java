package com.robert.src;

import com.robert.check.CheckThreadable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Controller {

    public Controller() throws IOException {

        Stage mainStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainController.fxml"));
        loader.setController(this);

        mainStage.setResizable(false);
        mainStage.setScene(new Scene(loader.load(), 485, 224));
        mainStage.setTitle("GoodLife Booking Alerter");
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/robert/resources/goodlife-logo.png")));

        setVisibility(false);

        mainStage.show();

        this.mainStage = mainStage;

        displayGeneralInformation();

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");

        ChromeDriver driver = new ChromeDriver(options);

        chromeDriver = driver;

        driver.get(loginURL);

        initializeRequest();

        ArrayList<String> sectionList = new ArrayList<>();

        sectionList.add("Morning List");
        sectionList.add("Afternoon List");
        sectionList.add("Evening List");

        this.sectionList = sectionList;

    }

    private Boolean morningActive;
    private Boolean afternoonActive;
    private Boolean eveningActive;

    private static Request request;

    private Integer refreshRate;

    private ArrayList<Booking> morningBookings;
    private ArrayList<Booking> afternoonBookings;
    private ArrayList<Booking> eveningBookings;

    private final ArrayList<String> sectionList;

    private String goodlifeApiRequest;

    private Integer day;

    private int clubNumber;

    private String pageSourceApi;

    private static WebDriver chromeDriver;

    private String timeNow;

    private final Stage mainStage;

    private String email;

    private String password;

    private final String loginURL = GOODLIFE_URLS.URL_LOGIN.getString();

    private final String goodlifeMainPage = GOODLIFE_URLS.URL_MAIN_PAGE.getString();

    private WebElement emailInput;

    private WebElement passwordInput;

    private WebElement confirmButton;

    @FXML private TextField txtEmail;

    @FXML private TextField txtPassword;

    @FXML private Label lblCredentialDisplay;

    @FXML private Button btnSubmit;

    @FXML private Label lblSelectDate;

    @FXML private Label lblSelectSection;

    @FXML private Label lblSelectTime;

    @FXML private ComboBox comboSelectSection;

    @FXML private ComboBox comboSelectTime;

    @FXML private Button btnStartTracking;

    @FXML private TextField txtDayNum;

    @FXML private Label lblRefreshRate;

    @FXML private TextField txtRefreshRate;

    @FXML private Button btnExit;

    @FXML private Button btnSubmitInfo;

    @FXML private void btnSubmitClicked() throws Exception {

        this.email = txtEmail.getText();

        this.password = txtPassword.getText();

        getLoginElements();

        enterCredentials(this.email, this.password);

        Thread.sleep(5000);

        // FIXME: 8/19/2020 create a more elegant solution than just waiting for 5 seconds hoping the page loads

        if(chromeDriver.getCurrentUrl().equals(goodlifeMainPage)) { // successfully logged in, redirects to main page

            lblCredentialDisplay.setText("Succesfully got to main page.");

            // resize the scene to display the rest of the components

            setVisibility(true);

            displayLabels();

            this.mainStage.setWidth(485);
            this.mainStage.setHeight(589);

            String clubUrl = getClubUrl();

            int clubNumber = getClubNumber(clubUrl);

            this.clubNumber = clubNumber;

            goToMainPage();

            ObservableList<String> sectionList = FXCollections.observableList(this.sectionList);

            comboSelectSection.setItems(sectionList);

        }

        else {

            setVisibility(false);

            lblCredentialDisplay.setText("Error! Incorrect credentials.");

            displayIncorrectCredentials();

            lblCredentialDisplay.setText("");

            eraseLoginDetails();

        }
    }

    @FXML private void btnStartTrackingClicked() throws IOException {

        getRefreshRate();

        String sectionValue = (String) comboSelectSection.getValue();

        String timeValue = (String) comboSelectTime.getValue();

        if(sectionValue == null || refreshRate == null || timeValue == null || day == null || refreshRate < 60) {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Error! Enter all the options!");

            alert.setContentText("You must enter all the options in the menu in order to proceed!\n" +
                    "\n" +
                    "Note: the refresh rate MUST be an integer greater than 60.");

            alert.showAndWait();

        }

        else {

            formGoodlifeApiRequest();

            getPageSource();

            request.setPageSourceApi(pageSourceApi);

            request.getJson();

            int arrayIndex;

            Booking booking = null;

            if (morningActive) {

                for (Booking bookings : morningBookings) {

                    if (timeValue.equals(bookings.getTotalTime())) {

                        arrayIndex = bookings.getArrayIndex();

                        request.getSpecificJsonObject(arrayIndex, "Morning");

                        booking = request.getSpecificBooking();

                    }
                }
            } else if (afternoonActive) {

                for (Booking bookings : afternoonBookings) {

                    if (timeValue.equals(bookings.getTotalTime())) {

                        arrayIndex = bookings.getArrayIndex();

                        request.getSpecificJsonObject(arrayIndex, "Afternoon");

                        booking = request.getSpecificBooking();

                    }
                }
            } else if (eveningActive) {

                for (Booking bookings : eveningBookings) {

                    if (timeValue.equals(bookings.getTotalTime())) {

                        arrayIndex = bookings.getArrayIndex();

                        request.getSpecificJsonObject(arrayIndex, "Evening");

                        booking = request.getSpecificBooking();

                    }
                }
            } else {

                // time selected is null

                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setHeaderText("You need to select a valid time and section.");
                alert.setTitle("Error!");
                alert.setContentText("You have not selected a valid time and section in the options.\n" +
                        "Please enter all the options properly, then click get notified.");

                alert.showAndWait();

            }

            if(morningActive || afternoonActive || eveningActive) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Goodlife Booking Alerter is now tracking for changes.");

                alert.setHeaderText("GBA is now looking for free spots.");

                alert.setContentText("GBA is now looking for free spots for: \n" +
                        this.timeNow);

                alert.showAndWait();

                CheckThreadable checkThreadable = new CheckThreadable(request, refreshRate, booking, sectionValue);

                checkThreadable.start();

            }
        }
    }

    @FXML private void comboTimeSelected() throws IOException{

        String section = (String) comboSelectSection.getValue();

        day = Integer.valueOf(txtDayNum.getText());

        if(day == null || day > 31 || day < 1) {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Error");

            alert.setHeaderText("You did not enter a proper day number.");

            alert.setContentText("You did not enter a valid day number in the field. In order to select a time, you must\n" +
                    "enter a day number in the current month you are in.");

            alert.showAndWait();
        }

        else {

            // no section selected in above combo box
            switch (section) {

                case "Morning List" -> {

                    getDate(); setNewDate();

                    formGoodlifeApiRequest();

                    getPageSource();

                    request.setPageSourceApi(pageSourceApi);

                    request.getJson();

                    ArrayList<Booking> morningBookings = request.getMorningList();

                    this.morningBookings = morningBookings;

                    ArrayList<String> comboMorningList = new ArrayList<>();

                    for (Booking bookings : morningBookings) {

                        comboMorningList.add(bookings.getTotalTime());

                    }

                    ObservableList<String> morningObservable = FXCollections.observableList(comboMorningList);

                    this.comboSelectTime.setItems(morningObservable);

                    morningActive = true;

                    afternoonActive = false;
                    eveningActive = false;

                    break;
                }
                case "Afternoon List" -> {

                    getDate(); setNewDate();

                    formGoodlifeApiRequest();

                    getPageSource();

                    request.setPageSourceApi(pageSourceApi);

                    request.getJson();

                    ArrayList<Booking> afternoonBookings = request.getAfternoonList();

                    this.afternoonBookings = afternoonBookings;

                    ArrayList<String> comboAfternoonList = new ArrayList<>();

                    for (Booking bookings : afternoonBookings) {

                        comboAfternoonList.add(bookings.getTotalTime());

                    }

                    ObservableList<String> morningObservable = FXCollections.observableList(comboAfternoonList);

                    this.comboSelectTime.setItems(morningObservable);

                    afternoonActive = true;

                    morningActive = false;
                    eveningActive = false;

                    break;
                }
                case "Evening List" -> {

                    getDate(); setNewDate();

                    formGoodlifeApiRequest();

                    getPageSource();

                    request.setPageSourceApi(pageSourceApi);

                    request.getJson();

                    ArrayList<Booking> eveningBookings = request.getEveningList();

                    this.eveningBookings = eveningBookings;

                    ArrayList<String> comboEveningList = new ArrayList<>();

                    for (Booking bookings : eveningBookings) {

                        comboEveningList.add(bookings.getTotalTime());

                    }

                    ObservableList<String> morningObservable = FXCollections.observableList(comboEveningList);

                    this.comboSelectTime.setItems(morningObservable);

                    eveningActive = true;

                    morningActive = false;
                    afternoonActive = false;

                    break;
                }
                default -> {

                    ArrayList<String> noSectionSelected = new ArrayList<>();
                    noSectionSelected.add("Select a section");
                    noSectionSelected.add("above in order to");
                    noSectionSelected.add("view a list here");
                    ObservableList<String> noSectionList = FXCollections.observableList(noSectionSelected);

                    comboSelectTime.setItems(noSectionList);

                    morningActive = null;
                    afternoonActive = null;
                    eveningActive = null;

                }
            }
        }
    }

    @FXML private void btnExitClicked() {

        closeDriver();

        System.exit(0);

    }

    private void enterCredentials(String email, String password) {

        this.emailInput.click();

        this.emailInput.sendKeys(email);

        this.passwordInput.click();

        this.passwordInput.sendKeys(password);

        this.confirmButton.click();

    }

    private void getDate() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        LocalDateTime localDateTime = LocalDateTime.now();

        this.timeNow = dateTimeFormatter.format(localDateTime);

        this.timeNow = this.timeNow.replaceAll("/", "-");

    }

    private void setNewDate() {

        // 2020-08-19 < example

        timeNow = timeNow.substring(0, 8);

        timeNow = timeNow + day;

    }

    private void setVisibility(boolean var) {

        this.lblSelectDate.setVisible(var);
        this.lblSelectSection.setVisible(var);
        this.lblSelectTime.setVisible(var);

        this.btnStartTracking.setVisible(var);

        this.comboSelectSection.setVisible(var);
        this.comboSelectTime.setVisible(var);

        this.txtDayNum.setVisible(var);
        this.lblRefreshRate.setVisible(var);
        this.txtRefreshRate.setVisible(var);

    }

    private void displayLabels() {

        this.lblSelectTime.setText("Choose a time period:");

        this.lblSelectSection.setText("Choose a section:");

        this.lblSelectDate.setText("Choose a date before the end of the month:");

        this.lblRefreshRate.setText("Refresh rate (seconds):");

    }

    private String getClubUrl() throws InterruptedException {

        WebElement clubUpcomingClasses = chromeDriver.findElement(By.className("club-upcoming-classes"));

        WebElement clubActivities = clubUpcomingClasses.findElement(By.id("activitySideBar"));

        WebElement btnClassSchedule = clubActivities.findElement(By.className("secondary-btn"));

        btnClassSchedule.click();

        Thread.sleep(5000);

        // FIXME: 8/19/2020 create a more elegant solution than just waiting 5 seconds hoping the page loads

        String clubURL = chromeDriver.getCurrentUrl();

        return clubURL;

    }

    private int getClubNumber(String clubURL) {

        String clubNumber = clubURL.substring(57);

        return Integer.parseInt(clubNumber);

    }

    private void goToMainPage() {

        chromeDriver.get(goodlifeMainPage);

    }

    private void getLoginElements() {

        WebElement input = chromeDriver.findElement(By.className("ls"));

        WebElement inputBox = input.findElement(By.className("login-input-container"));

        WebElement emailInput = inputBox.findElement(By.name("Email/Member #"));

        WebElement passwordInput = inputBox.findElement(By.name("Password"));

        // login containers

        WebElement loginConfirmContainer = input.findElement(By.className("login-confirm-container"));

        WebElement loginBtnContainer = loginConfirmContainer.findElement(By.className("login-btn-container"));

        WebElement confirmButton = loginBtnContainer.findElement(By.id("btn-login"));

        this.emailInput =  emailInput;

        this.passwordInput = passwordInput;

        this.confirmButton = confirmButton;

    }

    private void displayIncorrectCredentials() {

        Alert alertEnterCredentials = new Alert(Alert.AlertType.ERROR);

        alertEnterCredentials.setTitle("Error!");

        alertEnterCredentials.setHeaderText("There has been an error retrieving the schedule.");

        alertEnterCredentials.setContentText("Your login credentials are incorrect. Please try entering them again.");

        alertEnterCredentials.showAndWait();

    }

    private void displayGeneralInformation() {

        Alert alertGeneralInformation = new Alert(Alert.AlertType.WARNING);

        alertGeneralInformation.setTitle("Warning:");

        alertGeneralInformation.setHeaderText("A quick notice before you use the booking alerter:");

        alertGeneralInformation.setContentText("The Goodlife Booking alerter's speed is dependent upon your computer's capabilities. \n" +
                "You will initially experience a slow startup and load once you click submit. This is normal as it loads elements from Goodlife's website. \n" +
                "\n" +
                "If you have any questions or encounter any issues, feel free to post an Issues request on my github which can be found below: \n" +
                "\n" +
                "https://github.com/damnbruuh/goodlife-booking-alerter/issues");

        alertGeneralInformation.showAndWait();

    }

    private void formGoodlifeApiRequest() {

        this.goodlifeApiRequest = GOODLIFE_URLS.URL_API_MAIN.getString() + clubNumber + GOODLIFE_URLS.URL_API_UNTIL_CLUB.getString() + this.timeNow + GOODLIFE_URLS.URL_API_END.getString();

    }

    private void initializeRequest() {

        Request request = new Request();

        Controller.request = request;

    }

    private void getRefreshRate() {

        this.refreshRate = Integer.parseInt(this.txtRefreshRate.getText());

    }

    public void closeDriver() {

        chromeDriver.quit();

    }

    private void eraseLoginDetails() {

        emailInput.clear();

        passwordInput.clear();

    }

    private void getPageSource() {

        chromeDriver.get(goodlifeApiRequest);

        WebElement text = chromeDriver.findElement(By.cssSelector("body:nth-child(2) > pre:nth-child(1)"));

        pageSourceApi = text.getText();

    }

}
