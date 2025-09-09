# Online Quiz System (Java Swing + MySQL)

## Objective

An Online Quiz System built with Java Swing and MySQL.
It allows students to log in, take timed quizzes, and view their results.

## Tech Stack

* Language: Java (Swing for GUI)
* Database: MySQL
* Build Tool: Maven (or plain javac if preferred)
* Libraries:
    * JDBC (Database connection)

### Features

* 🔑 Login system

* 🎲 Randomized questions for each quiz

* ⏳ Quiz timer (auto-submit on time expiry)

* 🧮 Automatic scoring with negative marking support

* 💾 Results stored in MySQL with detailed breakdown

* 📊 View results with correct answers

### You need to change the [DB.java](https://github.com/Narayana48/Quiz_app/blob/master/src/com/quizapp/db/DB.java#L9) file to connect to your database

### DataBase Script --> [sql_Script.sql](https://github.com/Narayana48/Quiz_app/blob/master/sql_Script.sql) you need to Run this script in your mySql application before you start this Java application

### *** Before you start the Java application you need to Download the [mySql-connecter](https://sourceforge.net/projects/jdbcsql/files/jdbc/mysql-connector-java-8.0.18.jar/download) file and add this in your [classpath](https://github.com/Narayana48/Quiz_app/blob/master/.classpath) file.
