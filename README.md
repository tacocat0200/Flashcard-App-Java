# Flashcard App

## Overview
The Flashcard App is a web-based application designed to help users create, manage, and study flashcards efficiently. This application supports CRUD (Create, Read, Update, Delete) operations on flashcards, user authentication, and categorization of flashcards, providing an interactive learning experience.

## Features
- **User Authentication**: Secure login and registration using JWT (JSON Web Tokens).
- **Flashcard Management**: Create, read, update, and delete flashcards.
- **Categorization**: Organize flashcards into categories for better study management.
- **RESTful APIs**: Communicate between the frontend and backend using REST APIs.

## Technologies Used
- **Backend**: Java, Dropwizard
- **Database**: MySQL
- **Testing**: JUnit

## Installation

### Prerequisites
- Java JDK (version 8 or higher)
- Maven
- MySQL Server

### Steps to Set Up

1. **Clone the Repository**:
   ```bash
   git clone git@github.com:tacocat0200/Flashcard-App-Java-.git
   cd Flashcard-App-Java-
2. Configure Database:

-Create a MySQL database named flashcard_app
-Update the src/main/resources/config.yml file with your database connection details.

3. Install Dependencies:

mvn install

4. Run the Application


mvn exec:java -Dexec.mainClass="com.example.flashcardapp.FlashcardApplication"

## Usage
Navigate to http://localhost:8080 in your web browser.
Use the provided endpoints to interact with the Flashcard App:
POST /api/flashcards: Create a new flashcard.
GET /api/flashcards: Retrieve all flashcards.
GET /api/flashcards/{id}: Retrieve a flashcard by ID.
PUT /api/flashcards/{id}: Update a flashcard.
DELETE /api/flashcards/{id}: Delete a flashcard.
Testing

To run the tests, use the following command:

mvn test
