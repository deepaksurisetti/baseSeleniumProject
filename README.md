# Automated test example in Java with Cucumber and Selenium WebDriver #

This project is an example of UI automated functional test using Selenium and Cucumber.

Test scenarios are described in the feature files located here ./src/test/resources/Features.

## Installation ##

You need to have [Java 8 JDK]

Chrome Driver has been added to the project under .src/test/resources/Drivers

To install all dependencies, run 

```console
$ mvn clean install
```

## Running tests ##

```console
$ mvn test
```

We need to specify `-Dbrowser={browser}` where `{browser}` is either `chrome` or `firefox`.
You can also select specific scenarios to execute using `-Dcucumber.options="--tags @yourFeature_File_Path @your_tag"`. 

This Project will generate an Custom Emailable Report under ./target/Emailable-Report.htm