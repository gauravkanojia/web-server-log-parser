# Project web-server-log-parser
This repository will be used to track and store code base for the web-server-log-parser exercise.<br/>
This is a Maven-based Java application, which is used for parsing a provided web server log file as per the arguments.

## Pre-requisites
1.  The environment where this project will be run should have Java 8 setup with environment variables.
2.  The environment should have Maven build tool configured for building the project.

## Getting the code
1.  To get the code on your local machine, you can either fork the repository to your account and then clone it into your local machine.
2.  Another option will be to directly clone this repository using the below command in your local machine.

```script
git clone https://github.com/gauravkanojia/web-server-log-parser.git
```

Changes can be pushed through a pull request and will be reviewed before merging them to the main (master) repository.

For `Forking` this repository, simply click on the Fork button on the top right corner of this page.


## Build & Package the project
As this is a Maven-based project, the dependencies and build properties are mentioned in the parent `pom.xml` file. To build the project, simply run the below command in the parent directory where `pom.xml` is present.

```script
mvn -U clean install
```

Now, if you want to run the `jar` from the target folder, it's necessary to package all the dependent jars as well. This can be achieved using `shade` command.
```script
mvn -U clean install package shade:shade
```
Above command will build the project for the mentioned dependencies and will package it into a JAR file which can be run as any other Java JAR file.

## Running the application
#### Steps for running the project
The project can be run using the built `jar` file under the `target` folder using the below commands.

##### Hourly Run
```script
java -cp target/parser-1.1-SNAPSHOT.jar com.ef.Parser --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200 --pathToAccessLog=./access.log --dbUsername=appuser --dbPassword=appuser
```

##### Daily Run
```script
java -cp target/parser-1.1-SNAPSHOT.jar com.ef.Parser --startDate=2017-01-01.00:00:00  --duration=daily --threshold=500 --pathToAccessLog=./access.log --dbUsername=appuser --dbPassword=appuser
```
##### Arguments params explained
*   --startDate --> This argument specifies the `start date time` of the logs which needs to be chosen as starting reference point.
*   --duration --> This argument specifies the `duration`, DAILY or HOURLY, which in turn determines the threshold value.
*   --threshold --> This argument specifies the `count of requests` which when crossed determines the blocking of a particular IP address in the logs.
*   --pathToAccessLog --> This argument specifies the `absolute location` of the log file which needs to be read and processed.
*   --dbUsername --> This argument defines the `username` for the MySQL DB connection.
*   --dbPassword --> This argument defines the `password` for the MySQL DB connection.

#### Results
Here are the results from the sample test runs

![Test_Run_Results](./docs/results.png?raw=true "Test Run Results")

## Database
For this application, we are using MySQL database. The `schema` and `tables` creation script are present under the `docs` folder in the repository.
### Database Deliverables
*   Write MySQL query to find IPs that mode more than a certain number of requests for a given time period.
<br>Here is the query</br>

```sql
SELECT IP_ADDRESS FROM daily_access_logs WHERE LOG_TIMESTAMP BETWEEN '2017-01-01.13:00:00' AND '2017-01-01.14:00:00' AND REQUESTS >= 100;

SELECT IP_ADDRESS FROM hourly_access_logs WHERE LOG_TIMESTAMP BETWEEN '2017-01-01.13:00:00' AND '2017-01-01.14:00:00' AND REQUESTS >= 200;
```

*   Write MySQL query to find requests made by a given IP.

```sql
SELECT REQUESTS FROM hourly_access_logs WHERE IP_ADDRESS = '192.168.11.231';

SELECT REQUESTS FROM daily_access_logs WHERE IP_ADDRESS = '192.168.62.176';
```
## Versions
The versions mentioned below are oldest to latest. <br/>
*   1.0-SNAPSHOT: Initial release with first code shipment.
*   1.1-SNAPSHOT: Next release with unit test classes and code cleanup.

## Useful Links
*   My SQL Downloads: Use [this](https://dev.mysql.com/downloads/) link to download different flavors of MySQL.  
*   MY SQL Workbench: [This](https://dev.mysql.com/downloads/workbench/) link was used to download MySQL Workbench which will help in interacting with database.

## Notes
1.  Eclipse IDE has been used to develop this project.
2.  This project was tested as a runnable JAR file as well as through the Eclipse Utility `Run Configurations`.
3.  JUnit test classes have not been written for this project.
4.  Reach out to me: gisgaurav@gmail.com
