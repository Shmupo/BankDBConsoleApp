# Bank Database Console App

Bank terminal application that uses a database to simulate banking operations.

## Description

The application stores all data within a database, such as login accounts, checking accounts, pending checking account applications, and balance transactions.

A customer can login, apply for an account with a starting balance, view their balance, make withdrawals and deposits, and generate, accept, or reject bank balance transfers. 
A customer can have multiple checking accounts.

An employee can login, view a customers checking accounts, approve or reject checking account creation applications, and view transaction logs.

The software architecture is based upon a state machine.

The program was built using Maven.

The database used in MySql80.

Some database interactions, such as getting all balance transactions related to a customer account, utilize stored procedures in MySql.

The application is written in Java 17.

Logs are generated using Log4j2 2.23.1

Database interactions were implemented using Hibernate 6.5.2 and MySql connector 8.4.0.

## Getting Started

### Dependencies

Language : Java 17
Relational Database Management System (RDMS) : MySql80

Maven

Maven Dependencies : 
	Hibernate 6.5.2
	Log4j2 2.23.1
	MySql connector 8.4.0

### Installing

* How/where to download your program
* Any modifications needed to be made to files/folders

### Executing program

Run Main inside Bank.java


## Help



## Authors

Andrew Doan

ex. Dominique Pizzie  
ex. [@DomPizzie](https://twitter.com/dompizzie)

## Version History

* 0.1
    * Initial Release

## License

This project is licensed under the [NAME HERE] License - see the LICENSE.md file for details