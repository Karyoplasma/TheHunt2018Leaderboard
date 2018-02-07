# TheHunt2018Leaderboard
This is a Java application that will parse the progression spreadsheets for The Hunt 2018 and display it in a leaderboard.

Sadly, there is no way around the authentication process to pull data from a spreadsheet, so you'll need to turn on the Google Sheets API yourself.

Prerequisites:
To run this application, you'll need:
Java 1.7 or greater.
Access to the internet.
A Google account.

Step 1: Turn on the Google Sheets API
a) Go to https://console.developers.google.com/start/api?id=sheets.googleapis.com to create or select a project in the Google Developers Console and automatically turn on the API. Click Continue, then Go to credentials. It doesn't matter whether you name the project or not.
b) On the Add credentials to your project page, click the Cancel button.
c) At the top of the page, select the OAuth consent screen tab. Select an Email address, enter a "The Hunt 2018 Leaderboard" (without quotes, case is important) as the Product name, and click the Save button.
d) Select the Credentials tab, click the Create credentials button and select OAuth client ID.
e) Select the application type Other, enter the name "The Hunt 2018 Leaderboard" (again without quotes, case is important), and click the Create button.
f) Click OK to dismiss the resulting dialog.
g) Click the "Download JSON" button to the right of the client ID.
h) Move this file to the "resource" folder and rename it to client_secret.json
i) Run the jar file with a double click
