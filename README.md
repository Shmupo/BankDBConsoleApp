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

Install the files into a folder and add them to an IDE or Code Editor.
For this application, it was developed using IntelliJ.

### Executing program

The name of the pre-configured database url is 'jdbc:mysql://localhost:3306/bankdb'
If not created, create a database within MySQL80 with the same alias and port 3306 (this should be the default port).

If you wish to change the alias of the database, alter the file src/main/resources/hibernate.cfg.xml property
hibernate.connection.url with the URL of your database

Ensure MySQL80 server is running

If using Maven, allow maven to FIRST configure the project to apply dependencies to the project structure in IntelliJ before executing.

Run Main inside Bank.java

## Authors

Andrew Doan
AKA Shmupo

## Version History

* 0.1
    * Initial Release

## License

This project is licensed under the MIT License - see the LICENSE.md file for details