# goodlife

Goodlife Booking Alerter is an automated bot that will check Goodlife Fitness for booking time slots and check whether they are available or not. 
In order to use GBA, your account must first be logged in within the program. At the moment, GBA will only alert you for one time slot, not multiple.

Notes:
- past the login screen, the day number input field must be BEFORE the month you are currently in ends. (Goodlife updates their time slot API at the beginning of the month.)

GBA uses the following libraries and are packaged into the JAR:

- Selenium3
- gson for json parsing
- apache commons io (previously)
- javafx 

TO-DO:

- Instead of freezing the Controller while the webdriver loads the page, implement a separate thread for the driver so that the Controller does not hang while it loads.
 
