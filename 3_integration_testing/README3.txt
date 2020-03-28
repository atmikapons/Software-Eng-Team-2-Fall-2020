INTEGRATION TESTING

The 3 subsystems, (1) Mobile App, (2) Manager Website, and (3) Scanner GUI are connected through the MySQL database. Each subsystem queries the shared MySQL tables, which in turn affects the other subsystems. Because the subsystems do not directly interact with each other, integration testing is focused on using one subsystem at a time to make a change in the database, then verifying that the change has taken place.

Verification with the database:
Unless otherwise detailed below, verification with the database can be done using either the website to view MySQL databases (https://coewww.rutgers.edu/phpmyadmin/) or in an SSH session on soe.rutgers.edu. On the website, you can simply click the table and view whether the change has taken place. In the SSH session, you can query the database with SELECT * FROM <TABLE>, or a more specific query.

1. MOBILE APP
For new users, upon customer “Sign Up” as detailed in Unit Testing, the new entry will appear in the CustomerInfo SQL table. Verify that the CustomerInfo table now holds an entry with the same field values as inputted into the app with the SQL query: “SELECT * FROM CustomerInfo”.

After creating a new reservation as detailed in Unit Testing, the new entry will appear in the Reservations SQL table, as well as make a copy in the Records SQL table. Verify that both tables now hold an entry with the same fields as inputted into the app with the SQL queries: “SELECT * FROM Reservations” and “SELECT * FROM Records”.

To verify that the “Current Reservations” table being viewed is correct, you can do the following SQL query: “SELECT * FROM Reservations WHERE barcode = <customer barcode>” and see the subset of table entries for that customer’s current reservations

To verify that the “Reservation History” table being viewed is correct, you can do the following SQL query: “SELECT * FROM Records WHERE barcode = <customer barcode>” and see the subset of table entries for that customer’s past reservations.

2. MANAGER WEBSITE
To run integration testing, make sure the node application is running as specified in Unit Testing:
a. SSH into remote server and go to the directory /www/custom/zippypark/ZippyPark
b. Enter “npm install” in the terminal

Automated testing:
In this folder, subgroup3_test_routes.js contains code for automated testing for the manager website. This cannot be used on its own; it is a representative copy of ZippyPark/test/test_routes.js. Testing is done using Mocha, a Javascript test framework that runs on Node.js, and Chai, an assertion library that pairs with Mocha. 

Currently, the code runs basic tests on a few of the routes to confirm that the server returns HTTP status code 200 after an SQL query. It checks the routes to add and delete a customer account, as well as to get and edit a reservation. These tests will become more advanced as we move further along in the project. In the future, we will use Sequelize, a Node.js Object Relational Mapping for MySQL, to validate specific table fields to confirm that a test passes, along with the HTTP status code.

Run this test in the terminal with “npm test” (making sure steps 2a and 2b are followed). When the test completes, the terminal will display the test cases, the time it takes to complete each case, and whether the test has passed or failed. Upon completion, the node server remains running. We can now continue with manual testing so we can utilize user inputs.

Manual testing (use verification methods as detailed above):
On the Customers tab, a table of customers is on display. To verify that the table being viewed is correct, you can do the following SQL query: “SELECT * FROM CustomerInfo”. 

After using the “Add Customer” button and filling out the form fields correctly, a new customer entry will be added to the CustomerInfo SQL table. As with the Mobile App, verify that the CustomerInfo table now holds an entry with the same field values as inputted into the app with the SQL query: “SELECT * FROM CustomerInfo”. The new entry will also appear on the displayed table on the website.

Delete the entry that was just made by pressing the “Delete” button on the rightmost column of that entry’s row. The entry will disappear from the website’s table. Verify that the entry has been deleted from the CustomerInfo SQL table with the SQL query: “SELECT * FROM CustomerInfo,” or “SELECT * FROM CustomerInfo WHERE barcode = <barcode>.” The latter query will not return any entries.

On the Reservations tab, a table of current reservations is on display. To verify that the table being viewed is correct, you can do the following SQL query: “SELECT * FROM Reservations”.

Upon pressing an “Update” button on the table, the populated fields of the modal form verify that that the database has been successfully queried (but not yet modified). After editing any fields on the form and pressing “Update” again, verify that the changes have been made to that entry with the SQL query: “SELECT * FROM Reservations WHERE rID = <reservation ID>”. The new changes will also appear on the displayed table on the website.

Delete an entry by pressing the “Delete” button on the rightmost column of that entry’s row. The entry will disappear from the website’s table. Verify that the entry has been deleted from the Reservations SQL table with the SQL query: “SELECT * FROM Reservations,” or “SELECT * FROM Reservations WHERE rID = <reservation ID>.” The latter query will not return any entries.

3. SCANNER GUI
Test a: Customer enters: does not have an account
The customer should not be allowed into the garage. Verify this with the CustomerInfo SQL table with the SQL query “SELECT * FROM CustomerInfo WHERE LicenseNum= <license plate number>”. No entry should be returned to the terminal.

Test b: Customer enters: has account, but no reservation
The customer is allowed into the garage. Verify this with the CustomerInfo SQL table with the SQL query “SELECT * FROM CustomerInfo WHERE LicenseNum= <license plate number>”. One entry should be returned to the terminal.

Verify that the customer has no reservation with the SQL query “SELECT * FROM Reservations WHERE LicenseNum= <license plate number>”.

Perform a subtest where the walk-in floor is full, verified with the SQL query “SELECT * FROM ParkingSpots WHERE Type = “Walk-In””. 

Perform a subtest where the walk-in floor is open, verified with the same SQL query as above. Once the customer parks, verify the spot has been marked as occupied in the ParkingSpots SQL table with the SQL query “SELECT * FROM ParkingSpots WHERE Type = “Walk-In””, and check the status of the parking spots.

Test c: Customer enters: has account and reservation
The customer is allowed into the garage. Verify this with the CustomerInfo SQL table with the SQL query “SELECT * FROM CustomerInfo WHERE LicenseNum= <license plate number>”. One entry should be returned to the terminal.

Verify that the customer has a reservation with the SQL query “SELECT * FROM Reservations WHERE LicenseNum= <license plate number>”.

Once parked, verify that the spot has been marked as occupied in the ParkingSpots SQL table with the SQL query “SELECT * FROM ParkingSpots WHERE Type = “Reserved””, and check the status of the parking spots.

Test d: Customer exits: has no reservation
Verify that the customer did not have a reservation as in Test b.

Verify that the spot has been marked as unoccupied in the ParkingSpots SQL table with the SQL query “SELECT * FROM ParkingSpots WHERE Type = “Walk-In””, and check the status of the parking spots.

Verify that the customer entry has been recorded with the SQL query “SELECT * FROM Records WHERE LicenseNum =<license plate number>”, and check the exit time.

Test e: Customer exits: has reservation
Verify that the customer had a reservation as in Test c.

Verify that the spot has been marked as unoccupied in the ParkingSpots SQL table with the SQL query “SELECT * FROM ParkingSpots WHERE Type = “Reserved””, and check the status of the parking spots.

Verify that the customer has exited with the SQL query “SELECT * FROM Reservations WHERE LicenseNum =<license plate number>”, and check that there is no current reservation returned to the terminal.

Perform a subtest for if the customer is tardy. Verify that the overtime rate has changed with the SQL query “SELECT * FROM Records WHERE LicenseNum =<license plate number>”, and check the exit time and charge.
