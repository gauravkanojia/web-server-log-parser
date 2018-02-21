package com.ef.config;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * This class is used for storing the constants that are used by the parser
 * while processing the access logs.
 * 
 * @author Gaurav Kanojia
 *
 */
public interface ParserConstants {

	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss", Locale.US);

	String HOURLY_PREFIX = "hourly";
	String DAILY_PREFIX = "daily";
	long MILISECONDS_IN_HOUR = 3600 * 1000;

	// Database information
	String MYSQL_DB_URL = "jdbc:mysql://localhost:3306/";
	String MYSQL_TABLE_NAME = "ACCESS_LOGS";
	String MYSQL_DB_NAME = "WEB_SERVER_LOGS";
	String MYSQL_USERNAME = "appuser";
	String MYSQL_PASSWORD = "appuser";

	// Column names for the database table
	String _ID = "_ID";
	String THRESHOLD = "THRESHOLD";
	String DURATION = "DURATION";
	String LOG_TIMESTAMP = "LOG_TIMESTAMP";
	String IP_ADDRESS = "IP_ADDRESS";
	String REQUESTS = "REQUESTS";
	String STATUS = "STATUS";
	String USER_AGENT = "USER_AGENT";
	String COMMENTS = "COMMENTS";

	String CREATE_DAILY_TABLE = new StringBuffer("CREATE TABLE IF NOT EXISTS ").append(DAILY_PREFIX).append("_")
			.append(MYSQL_TABLE_NAME).append("(")
			.append(_ID).append(" INT NOT NULL AUTO_INCREMENT,")
			.append(THRESHOLD).append(" INT(10) NOT NULL,")
			.append(DURATION).append(" VARCHAR(50) NOT NULL,")
			.append(LOG_TIMESTAMP).append(" VARCHAR(50) NOT NULL,")
			.append(IP_ADDRESS).append(" VARCHAR(50) NOT NULL UNIQUE,")
			.append(REQUESTS).append(" VARCHAR(50) NOT NULL,")
			.append(STATUS).append(" INT(10) NOT NULL,")
			.append(COMMENTS).append(" VARCHAR(500) NOT NULL,").append("PRIMARY KEY (_ID));").toString();

	String CREATE_HOURLY_TABLE = new StringBuffer("CREATE TABLE IF NOT EXISTS ").append(HOURLY_PREFIX).append("_")
			.append(MYSQL_TABLE_NAME).append("(")
			.append(_ID).append(" INT NOT NULL AUTO_INCREMENT,")
			.append(THRESHOLD).append(" INT(10) NOT NULL,")
			.append(DURATION).append(" VARCHAR(50) NOT NULL,")
			.append(LOG_TIMESTAMP).append(" VARCHAR(50) NOT NULL,")
			.append(IP_ADDRESS).append(" VARCHAR(50) NOT NULL UNIQUE,")
			.append(REQUESTS).append(" VARCHAR(50) NOT NULL,")
			.append(STATUS).append(" INT(10) NOT NULL,")
			.append(COMMENTS).append(" VARCHAR(500) NOT NULL,").append("PRIMARY KEY (_ID));").toString();
}
