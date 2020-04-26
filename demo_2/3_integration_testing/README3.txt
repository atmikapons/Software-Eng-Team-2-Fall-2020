Integration Testing

The 3 subsystems, (1) Mobile App, (2) Manager Website, and (3) Scanner GUI are connected through the MySQL database. Each subsystem queries the shared MySQL tables, which in turn affects the other subsystems. Because the subsystems do not directly interact with each other, integration testing is focused on using one subsystem at a time to make a change in the database, then verifying that the change has taken place. Changes in the database can be seen through the other systems. As a result, the systems can integrate functions without direct communication with one another by using only the database to transfer information.

Verification with the database:
Unless otherwise detailed below, verification with the database can be done using either the website to view MySQL databases (https://coewww.rutgers.edu/phpmyadmin/) or in an SSH session on soe.rutgers.edu. On the website, you can simply click the table and view whether the change has taken place. In the SSH session, you can query the database with SELECT * FROM <TABLE>, or a more specific query.

1&2 Website-App Integration:

Note: It is assumed that the customer will typically add, edit, and delete their account and reservations. However, the manager has the capability of doing so as well since it may be needed in rare cases.

Creating an Account:
For new users, upon customer “Sign Up” by entering information into the fields required to make a new customer account, the new entry will appear in the CustomerInfo SQL table. Now, this information is available to the manager as well and can be viewed on the Customers page of the manager website.The manager portal can be used to create an account as well. The manager would input customer information as required by the website. This customer will now be held in the CustomerInfo table in the database. The app features are accessible to this customer now and they do not have to sign up. 

Editing an Account:
The customer can edit their account by changing one of the fields that was used to create their account in the app. This change will be seen in the CustomerInfo database table and also reflected in the Customers page on the manager website. The manager may also edit an account. This will cause the same change to occur in the CustomerInfo table and the customer will be able to see these changes to their profile as well when they use the app.

Deleting an Account:
Delete a customer account on the app and this customer will no longer be held in the CustomerInfo table. As a result, the website will no longer see this customer in the table on Customer pages either. Deleting an account from the manager website will cause the entry to be deleted in the CustomerInfo table as well. The customer deleted is no longer seen as a customer and can’t use the app.

Creating a Reservation:
After creating a new reservation by entering the expected information as a customer using the app, the new entry will appear in the Reservations SQL table, as well as make a copy in the Records SQL table. The manager website can view the information of the Reservations table on the Reservations page of the website. The Records table is used to make the graphs and past reservations are used to collect data from. The manager website can also be used to make a reservation for a specific customer. This will be seen in the Reservations and Records table as well. The customer under which this reservation was made will notice that “Current Reservations” has a new entry when they are using the app.

Updating a Reservation:
A customer may update their reservation by changing one of the fields they used to create it. This change will be seen in the Reservations and Records table. The manager website can view the information of the Reservations table on the Reservations page of the website. The Records table is used to make the graphs and past reservations are used to collect data from. The manager website can also be used to edit a reservation for a specific customer. This change will be seen in the Reservations and Records table as well. The customer under which this reservation was made will notice that “Current Reservations” has a changed entry when they are using the app.

Delete Reservation:
A customer may delete any upcoming reservation through the app. This will be deleted from the Reservations table and the Records table will fill in “YES” under the deleted column for this reservation entry. The manager website will no longer be able to see this reservation on the Reservations page. The manager may delete a reservation as well. This will change the Reservation and Records table in the same way outlined above. As a result, the user will no longer see this reservation in their “Current Reservations”.

Changing Points:
To change the point system, navigate to the dashboard and view the point adjustment form. Change any value in the form for an action and hit the update button at the end. Go to the database table called “Points” and see that the same change can be seen in this table as well. Now, the customer performs the action that just had its point value changed. They will see that their points are changed accurately to the update. This action will also update the CustomerInfo database so the website will also see this change in points for the specific customer on the Customers page.

Changing the Price:
To change the price system, navigate to the dashboard and view the price adjustment form. Change any value in the form for any time and hit the update button at the end. Go to the database table called “Payment” and see that the same change can be seen in this table as well. In the customer app, create a reservation for the time under which the payment was just changed and see the charge for that reservation accurately reflect this update as well. This will add an entry to the Reservations table so the charge for this reservation can be seen to match the update on the Reservations page of the manager portal as well.

Creating Charts:
Charts can be viewed by navigating to the statistics page and clicking on a button as the graph to be viewed. There is a table below the graph that can be used to view the data collected to make it. The information used to make the tables are seen to match data found in the “Records” table of the database. If 12 customers make a reservation for a certain date then once that date has passed the charts that use reservation data will be made to reflect this. For example, the chart looking at Reservations per Date will show the number of reservations per date with data collected over the past 30 days. This chart will have 12 listed for this date after it has passed but is still within the 30 days past limit.

1&3.Scanner GUI-Manager Website Integration:

Customer Entering Garage:
A customer enters the garage and the scanner will recognize it as a reservation or decide it’s a walkin. If they are a walkin the scanner adds it to the Records table of the database as such. The manager website can use this information once after some time as information for charts. For example, the chart looking at Walkins per Date will show the number of walkins per date with data collected over the past 30 days. If 12 customers walkin on a certain date, this chart will have 12 listed for this date after it has passed but is still within the 30 days past limit.

Customer Exiting Garage:
A customer who was a walkin leaves the garage after a certain period of time. The scanner records the entry and exit times to determine from when to when the spot was occupied. It will use this to input charge into the Records table of the database. For a customer with a reservation, the scanner will remove the reservation entry from the database, and the change will be reflected in the website’s reservations page. The scanner will also update charge if a customer with a reservation stays for longer than expected. This table can then be used to make the charts regarding revenue. For example, the Revenue per Day Last Week chart will show how much revenue was made every day last week. Say that on this Monday, the charge in the records table with rows having this Monday’s date are summed up. Next Monday a chart will reflect this exact sum as the Revenue in the chart on Monday.

Spot Status:
When a spot has a car parked in it, the scanner updates the ParkingSpots table in the database to say that it is “occupied” under status. This information is available to the manager on the dashboard as the card holding the number of open spots is determined by the summing of the number of rows where status is “unoccupied”
