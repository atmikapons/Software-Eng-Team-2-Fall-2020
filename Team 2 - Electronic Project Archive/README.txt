ZippyPark
-------------------------------------------------------------------------------------------------------------------------------
For full file descriptions please refer to these documents under the doc directory:
        - Team 2 - Technical Documentation.pdf
        - Team 2 - User Documentation.pdf
-------------------------------------------------------------------------------------------------------------------------------
File and Code Access
-------------------------------------------------------------------------------------------------------------------------------
To Access the Website
1. SSH into the remote server coewww.rutgers.edu using a Linux/Unix based terminal
   1. Use your Rutgers SOE username (netID) and password
2. Viewing website and website code
   1. Cd into /www/custom/zippypark/ZippyPark/Website
      1. This folder should have ‘server.js’
   2. Run: ‘npm install’ to install all needed node modules
   3. Run: ‘npm start’ to start the server
   4. Go to zippypark.rutgers.edu and view the website
   5. Pages can be navigated to using the sidebar
Additional information on local vs remote setup, as well as manager login credentials,
can be found at code/Website/README.md
-------------------------------------------------------------------------------------------------------------------------------
To Run the Scanner GUI
Prereq: Install JRE 8.0 or higher
1. Enter the Scanner folder
2. Execute the runnable .jar file 
   1. Double click on the file
   2. Or execute using command line
3. You’ll be prompted to enter your netid and engineering account password
-------------------------------------------------------------------------------------------------------------------------------
To Run the Android Application
Prereq: Install Android Studio
1. Import the AndroidApp directory as a new project into Android Studio
2. Install the app
   1. If you have an android phone:
      1. Enable developer mode in settings
      2. Plug your phone in to your computer and install directly
   2. Otherwise:
      1. Install a virtual device, preferably the Pixel 3a if possible
-------------------------------------------------------------------------------------------------------------------------------
