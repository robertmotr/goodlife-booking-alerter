# goodlife-booking-alerter

Goodlife Booking Alerter is an automated bot that will check Goodlife Fitness for booking time slots and whether they are available or not. 
In order to use GBA, your account must first be logged in within the program. At the moment, GBA will only alert you for one time slot, not multiple.

Notes:
- past the login screen, the day number input field must be BEFORE the month you are currently in ends. (Goodlife updates their time slot API at the beginning of the month.)

GBA uses the following libraries and are packaged into the JAR:

- Selenium3
- gson for json parsing
- apache commons io (previously)
- javafx 

GBA also uses chrome which is packaged into the JAR as chromedriver.exe. Upon JAR execute, GBA will create a folder named "Driver" within the current running directory and unpack the chromedriver into that folder so that Java can run the executable. Chrome is used to search for web elements and interact with them, emulating a real user.

TO-DO:

- Instead of freezing the Controller while the webdriver loads the page, implement a separate thread for the driver so that the Controller does not hang while it loads.
- ~~Restructure package access so that everything fits under com.robert~~
 
