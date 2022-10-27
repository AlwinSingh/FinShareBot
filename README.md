# FinShareBot
A simple twitter bot that sends Financial News and Reddit Memes/Articles depending on whether the Stock Market is open!

* https://twitter.com/FinShareBot


## How it works
* When the stock markets are open, the Bot will send Financial News every hour until the stock market closes.
* When the stock markets are closed, the Bot will send Memes or trending Posts from Reddit!


## APIs in use
The project relies on Reddit, Twitter and Yahoo Finance APIs!


## Project Setup
1. Download the project and extract to a location of your choice
2. There is a folder called 'JavaLibraries', it contains all the Jar files that you must set up for your project
3. Set up the dependencies in your Java Project (https://www.ibm.com/docs/en/app-connect/11.0.0?topic=SSTTDS_11.0.0/com.ibm.etools.mft.doc/ac30280_.htm)
4. Register a Twitter and Yahoo Developer Account. Get the API credentials from there. The reddit API does not need any authentication!
5. Open up api.js, enter your API Credentials
6. Note that storing Creds in the source code such as Api.js is not suggested, feel free to change it to a Environment file or you could even use AWS Key Manager or Secret Manager.
7. Run Main.java


## Working Examples
![FinShareBot](https://user-images.githubusercontent.com/62194353/197994287-e166b4f9-65c0-48aa-8d6b-c2be91374834.PNG)
![image](https://user-images.githubusercontent.com/62194353/197994791-a425d68c-a289-482a-b027-291c6acdeace.png)
