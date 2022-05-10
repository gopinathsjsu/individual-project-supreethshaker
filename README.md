# CMPE 202 Individual Project
### ( Supreeth Chandrasekhar 015919566 )

## Running the project
* Clone the repository
* Import the project in your editor of choice. The project uses Maven
* The entry point of the file is located in `Billing.java`. Run this file to run the application
* Ensure that your Java version matches the application's ( Java 8)

## Project Data
* The CSV files are located in the root folder in the files `Orders.csv`, `Items.csv`, and `Cards.csv`.
* If you would like to input data, you can replace the contents of the respective files above. 
* Note that removing the files or adding non-CSV type of data WILL not run the program as expected

## Design Patterns
* The project utilizes two design patterns
* `Factory pattern` is used for writing different content to different output. An `Outputter` interface defines the writing operations whereas the classes implementing this interface extend the functionality. An `OutputterFactory` class creates new instances of the output methods based on the context provided by client
* `Strategy pattern` is used for handling the different processing needs of each .CSV file while being processed. Based on the file, there is a different strategy used, and this decouples the loading functionality based on strategy and how it is consumed downstream
