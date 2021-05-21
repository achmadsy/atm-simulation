# ATM Simulation with Java

The application built with Java 8 with Maven and Postgresql 
Included sample accounts.csv for import

# Features:

- Dual Interface (Web and CLI)
- Withdraw 
- Fund Tranfer
- Last 10 Transaction History with date filter
- Import accounts from csv file.

# How to use: 

1. Build the program with "mvn clean install"
2. Run the program with "java -jar target\ATM-1.0-SNAPSHOT.war"
3. Can include additional csv import with "java -jar target\ATM-1.0-SNAPSHOT.war --file.path=accounts.csv"

# Default Accounts

account number: 112233
pin: 012108

account number: 112244
pin: 932012