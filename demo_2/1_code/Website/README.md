# ![zippy park logo](public/images/zplogo.png)ippyPark
Rutgers University
Software Engineering Course Project Spring 2020
Team 2

## **Manager Portal Website**
The Manager Portal allows parking garage managers to add, edit, and delete customer accounts and reservations. The website also provides quick and handy information about the garage's current status, as well as statistics in the form of graphs and tables.

### *Remote Setup*
To use the current remote repository, you must have a Rutgers SOE account. This option is RECOMMENDED if you are only viewing the website and not making any changes to the project.

- Using a Linux/Unix based terminal, SSH into **coewww.rutgers.edu** using your NetID. Login with your Rutgers SOE account.
    - `ssh [netid]@coewww.rutgers.edu`
- Change directory 
    - `cd /www/custom/zippypark/ZippyPark/Website`
- Run `npm install` 
- Run `npm start` to start the server, or `npm test` to start the server and run automated tests
- View the website at **zippypark.rutgers.edu**
- This takes you to our Login page. Access the portal with username: *manager*, password: *password*  . (Additional instructions below)

### *Local Setup*
To use this option, you must setup your own local database. Remote database access is restricted.
- Clone repository from Github
- Install Node.js
- Change directory into **Website** folder. You will find `server.js` in this folder.
- Set up a local copy of our remote database by going to `https://coewww.rutgers.edu/phpmyadmin/` and logging in with credentials. Change the database information in `server.js` accordingly. If this step is not completed, the website will not have any database access.
- Run `npm install` 
- Run `npm start` to start the server, or `npm test` to start the server and run automated tests
- View the website at **localhost:8080**
- This takes you to our Login page. Access the portal with username: *manager*, password: *password*  . (Additional instructions below)

### *Page Breakdown*
- #### Login
    - There are currently two manager accounts in the database:
        - username: Man1, password: Pass
        - username: manager, password: password
    - Additional manager accounts can be added manually
- #### Dashboard
    - View current garage status through cards
    - View and update price scheme
        - "Suggest Price" to use data collected on average capacity per hour to suggest new multipliers
        - "Reset" to revert to original price scheme (cancel update)
    - View and update point scheme
- #### Statistics
    - Open tab to view corresponding graph and table
- #### Reservations
    - Add, edit, or delete a reservation
    - Leave "Charge" blank when adding reservation to use current price scheme, or add custom charge
- #### Customers
    - Add, edit, or delete a customer account