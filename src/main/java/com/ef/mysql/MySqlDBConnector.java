/**
 * 
 */
package com.ef.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ef.config.ParserConstants;
import com.ef.model.Duration;
import com.ef.model.LoggerRecord;

import dnl.utils.text.table.TextTable;

/**
 * This class is used for opening and closing the connection to the MySQL
 * database which is used for storing the results of the parser runs.
 * 
 * @author Gaurav Kanojia
 *
 */
public class MySqlDBConnector {

	private Connection connection = null;
	private String dbUsername = null;
	private String dbPassword = null;
	private Statement statement = null;

	private static final String COMMA_DELIMITER = ",";

	public MySqlDBConnector() {

	}

	public MySqlDBConnector(String dbUsername, String dbPassword) {
		this.dbUsername = (dbUsername == null) ? ParserConstants.MYSQL_USERNAME : dbUsername;
		this.dbPassword = (dbPassword == null) ? ParserConstants.MYSQL_PASSWORD : dbPassword;
		this.connection = getConnection();
		createTableForDatabase();
	}

	public Connection getConnection() {

		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(ParserConstants.MYSQL_DB_URL + ParserConstants.MYSQL_DB_NAME,
						this.dbUsername, this.dbPassword);
			}
		} catch (SQLException sqlEx) {
			System.out.println("Unable to connect to database: " + sqlEx.getMessage());
		}
		return connection;
	}

	public void closeConnection() {

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException sqlEx) {
				System.out.println("Unable to close the database connection: " + sqlEx.getMessage());
			}
		}
	}

	/**
	 * @param sqlQuery
	 */
	public void createTableForDatabase() {

		try {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			ResultSet resultSet = dbMetaData.getTables(null, null, "%", null);

			if (!resultSet.next()) {
				statement = connection.createStatement();
				statement.execute(ParserConstants.CREATE_DAILY_TABLE);
				statement.execute(ParserConstants.CREATE_HOURLY_TABLE);
			}

			if (statement != null) {
				System.out.println("Tables created successfully.");
			} else {
				System.out.println("Unable to create tables in the database as tables already exists.");
			}
		} catch (SQLException sqlEx) {
			System.out.println("Exception while table creation: " + sqlEx);
		}
	}

	/**
	 * @param loggerRecord
	 */
	public boolean insertRecords(LoggerRecord loggerRecord) {
		// Before inserting check if the row exists. If yes, update instead.
		if (isLoggerRecordPresentInTable(loggerRecord)) {
			return true;
		}

		String durationTablePrefix = loggerRecord.getDuration().name().toUpperCase();

		String insertQuery = new StringBuffer("INSERT INTO ").append(durationTablePrefix).append("_")
				.append(ParserConstants.MYSQL_TABLE_NAME).append(" (")
				.append(ParserConstants.IP_ADDRESS).append(COMMA_DELIMITER)
				.append(ParserConstants.THRESHOLD).append(COMMA_DELIMITER)
				.append(ParserConstants.DURATION).append(COMMA_DELIMITER)
				.append(ParserConstants.REQUESTS).append(COMMA_DELIMITER)
				.append(ParserConstants.STATUS).append(COMMA_DELIMITER)
				.append(ParserConstants.COMMENTS).append(COMMA_DELIMITER)
				.append(ParserConstants.LOG_TIMESTAMP)
				.append(") VALUES (?, ?, ?, ?, ?, ?, ?)").toString();

		try {
			PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

			insertStatement.setString(1, loggerRecord.getIpAddress());
			insertStatement.setInt(2, loggerRecord.getThreshold());
			insertStatement.setString(3, loggerRecord.getDuration().name());
			insertStatement.setInt(4, loggerRecord.getRequests());
			insertStatement.setString(5, loggerRecord.getStatus());
			insertStatement.setString(6, loggerRecord.getComments());
			insertStatement.setString(7, loggerRecord.getLogTimestamp());

			int insertedRowCount = insertStatement.executeUpdate();
			if (insertedRowCount > 0) {
				System.out.println("Record inserted!!");
			}
			return true;
		} catch (SQLException sqlEx) {
			System.out.println("Inserting into database failed: " + sqlEx.getMessage());
		}
		return false;
	}

	/**
	 * @param preparedStatement
	 *            - The prepared statement for retrieving the records from the
	 *            database.
	 */
	public List<LoggerRecord> retrieveRecords(PreparedStatement preparedStatement) {

		List<LoggerRecord> loggerRecords = new ArrayList<LoggerRecord>();

		try {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				LoggerRecord record = new LoggerRecord();
				record.set_id(rs.getLong(ParserConstants._ID));
				record.setIpAddress(rs.getString(ParserConstants.IP_ADDRESS));
				record.setThreshold(rs.getInt(ParserConstants.THRESHOLD));
				record.setDuration(Duration.findTheDuration(rs.getString(ParserConstants.DURATION)));
				record.setRequests(rs.getInt(ParserConstants.REQUESTS));
				record.setComments(rs.getString(ParserConstants.COMMENTS));
				record.setLogTimestamp(rs.getString(ParserConstants.LOG_TIMESTAMP));

				loggerRecords.add(record);
			}

		} catch (SQLException sqlEx) {
			System.out.println("Record not found in database: " + sqlEx.getMessage());
		}
		return loggerRecords;
	}

	/**
	 * This utility method is used for printing the retrieved records on console
	 * in a tabular fashion. An external library of
	 * dnl.utils.text.table.TextTable has been used for this which prints out
	 * the data in a pretty fashion.
	 * 
	 * @param records
	 *            - The Logger Records from the database which needs to be
	 *            printed out.
	 */
	public void printRecords(List<LoggerRecord> records) {
		String[] columnNamesForOutput = { "IP Address", "Threshold value", "Duration selected",
				"Total number of requests", "Comments", "Log Timestamp" };
		Object[][] rowValues = new Object[records.size()][7];

		for (int i = 0; i < records.size(); i++) {
			rowValues[i][0] = records.get(i).getIpAddress();
			rowValues[i][1] = records.get(i).getThreshold();
			rowValues[i][2] = records.get(i).getDuration();
			rowValues[i][3] = records.get(i).getRequests();
			rowValues[i][4] = records.get(i).getComments();
			rowValues[i][5] = records.get(i).getLogTimestamp();
		}

		TextTable textTableForOutput = new TextTable(columnNamesForOutput, rowValues);

		// Sort the table on first column and print it
		textTableForOutput.setSort(0);
		textTableForOutput.printTable();
	}

	/**
	 * @param loggerRecord
	 * @return
	 */
	private boolean isLoggerRecordPresentInTable(LoggerRecord loggerRecord) {

		String durationTablePrefix = loggerRecord.getDuration().name().toUpperCase();
		String selectQuery = new StringBuffer("SELECT * FROM ").append(durationTablePrefix).append("_")
				.append(ParserConstants.MYSQL_TABLE_NAME).append(" WHERE ").append(ParserConstants.IP_ADDRESS)
				.append("=?").toString();
		try {

			PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
			selectStatement.setString(1, loggerRecord.getIpAddress());

			List<LoggerRecord> records = retrieveRecords(selectStatement);

			// If the records are found, then update them in the database
			if (records != null && records.size() > 0) {
				String updateQuery = new StringBuffer("UPDATE ").append(durationTablePrefix).append("_")
						.append(ParserConstants.MYSQL_TABLE_NAME).append(" SET ")
						.append(ParserConstants.THRESHOLD).append("=?,")
						.append(ParserConstants.DURATION).append("=?,")
						.append(ParserConstants.LOG_TIMESTAMP).append("=?,")
						.append(ParserConstants.REQUESTS).append("=?,")
						.append(ParserConstants.STATUS).append("=? ")
						.append(" WHERE ").append(ParserConstants.IP_ADDRESS).append("=?").toString();
				
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

				updateStatement.setInt(1, loggerRecord.getThreshold());
				updateStatement.setString(2, loggerRecord.getDuration().name());
				updateStatement.setString(3, loggerRecord.getLogTimestamp());
				updateStatement.setInt(4, loggerRecord.getRequests());
				updateStatement.setString(5, loggerRecord.getStatus());
				updateStatement.setString(6, loggerRecord.getIpAddress());

				int updatedRowCount = updateStatement.executeUpdate();
				return updatedRowCount > 0;
			}

		} catch (SQLException sqlExe) {
			System.out.println("Exception while looking for the record in database: " + sqlExe.getMessage());
		}
		return false;
	}
}
