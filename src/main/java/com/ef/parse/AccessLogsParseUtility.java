/**
 * 
 */
package com.ef.parse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.ef.config.ParserConstants;
import com.ef.model.Duration;
import com.ef.model.InputParams;
import com.ef.model.LoggerRecord;
import com.ef.mysql.MySqlDBConnector;

/**
 * @author Gaurav Kanojia
 *
 */
public class AccessLogsParseUtility {

	public AccessLogsParseUtility() {
	}

	public void parseLogsFromInputParams(InputParams inputParams) {

		String readLine = "";
		final String pipeDelimiter = "\\|";
		BufferedReader bufferedReader = null;
		List<LoggerRecord> recordsList = new ArrayList<LoggerRecord>();
		Map<LoggerRecord, Integer> recordsMap = new HashMap<LoggerRecord, Integer>();
		MySqlDBConnector dbDaoImpl = new MySqlDBConnector(inputParams.getDatabaseUsername(),
				inputParams.getDatabasePassword());

		String hourlyComments = "This IP Address has made more than " + inputParams.getThreshold()
				+ " requests in the provided 1 hour time frame.";
		String dailyComments = "This IP Address has made more than " + inputParams.getThreshold()
				+ " requests in the provided 24 hours time frame.";

		try {
			bufferedReader = new BufferedReader(new FileReader(inputParams.getPathToAccessLog()));

			while (StringUtils.isNotBlank(readLine = bufferedReader.readLine())) {

				String[] accessLogElements = readLine.split(pipeDelimiter);
				String logTimestamp = parseLogTimetsampToString(accessLogElements[0]);

				if (verifyLogTimeStartAndEndDate(logTimestamp, inputParams.getStartDate(), inputParams.getEndDate())) {
					LoggerRecord insertRecord = new LoggerRecord();
					insertRecord.setIpAddress(accessLogElements[1]);
					insertRecord.setDuration(inputParams.getDuration());
					insertRecord.setThreshold(inputParams.getThreshold());
					insertRecord.setStartDate(inputParams.getStartDate());
					insertRecord.setStatus(accessLogElements[3]);
					insertRecord.setEndDate(inputParams.getEndDate());
					insertRecord.setComments(
							Duration.DAILY.equals(inputParams.getDuration()) ? dailyComments : hourlyComments);
					insertRecord.setLogTimestamp(logTimestamp);

					if (recordsMap.containsKey(insertRecord)) {
						recordsMap.put(insertRecord, recordsMap.get(insertRecord) + 1);
					} else {
						recordsMap.put(insertRecord, 1);
					}
				}
			}
		} catch (IOException ioEx) {
			System.out.println("IO Exception when parsing the access logs: " + ioEx.getMessage());
		}

		if (!recordsMap.isEmpty()) {
			for (Map.Entry<LoggerRecord, Integer> entry : recordsMap.entrySet()) {
				LoggerRecord key = entry.getKey();
				Integer value = entry.getValue();
				if (value >= inputParams.getThreshold()) {
					key.setRequests(value);
					recordsList.add(key);
				}
			}
		}

		if (recordsList.isEmpty()) {
			System.out.println("No results to insert in database and to print.");
		} else {
			System.out.println("Here are the records!");
			dbDaoImpl.printRecords(recordsList);
			Iterator<LoggerRecord> recordsIter = recordsList.iterator();
			while (recordsIter.hasNext()) {
				dbDaoImpl.insertRecords(recordsIter.next());
			}
			dbDaoImpl.closeConnection();
		}
	}

	public void calculateEndDateFromInputParams(InputParams inputParams) {

		// On the basis of duration provided, set number of hours for Duration.
		// Hourly - 1 hour, Daily - 24 hours
		int durationInHours = inputParams.getDuration().equals(Duration.HOURLY) ? 1 : 24;
		Date startDateInputParam = null;
		try {
			startDateInputParam = ParserConstants.DATE_FORMAT.parse(inputParams.getStartDate());
			Date endDate = new Date(
					startDateInputParam.getTime() + durationInHours * ParserConstants.MILISECONDS_IN_HOUR);
			inputParams.setEndDate(ParserConstants.DATE_FORMAT.format(endDate));

		} catch (ParseException parseEx) {
			System.out.println("Exception while getting end date: " + parseEx.getMessage());
		}
	}

	/**
	 * This utility method is used for fixing the logs timestamp to match with
	 * the input argument. <br/>
	 * e.g. Input argument date: 2017-01-01.15:00:00<br/>
	 * Log timestamp date: 2017-01-01 00:00:11.763
	 * 
	 * @param providedLogTimestamp
	 *            - The provided log timestamp which needs to be converted.
	 * @return - Converted timestamp as per the format being used.
	 */
	private String parseLogTimetsampToString(String providedLogTimestamp) {

		try {
			String logTimestamp = providedLogTimestamp;

			// If the log timestamp contains space, then replace it.
			if (providedLogTimestamp.contains(" ")) {
				logTimestamp = providedLogTimestamp.replace(" ", ".");
			}
			return ParserConstants.DATE_FORMAT.format(ParserConstants.DATE_FORMAT.parse(logTimestamp));

		} catch (ParseException parseEx) {
			System.out.println("Exception while converting log date to correct format: " + parseEx.getMessage());
		}

		return null;
	}

	/**
	 * This method returns <b><i>True</i></b>, if the log's timestamp is between
	 * start date and end date. And <b><i>False</i></b>, if it doesn't.
	 * 
	 * @param logTimestamp
	 *            - Timestamp from the access logs.
	 * @param startDate
	 *            - Start Date from the input argument.
	 * @param endDate
	 *            - End Date as calculated on the basis of the Duration provided
	 *            input argument.
	 * @return - True/False on the basis of the access log timestamp is between
	 *         start date and end date or not.
	 */
	private boolean verifyLogTimeStartAndEndDate(String logTimestamp, String startDate, String endDate) {

		try {
			Date logTimestampDate = ParserConstants.DATE_FORMAT.parse(logTimestamp);
			DateTime logTimeDateTime = new DateTime(logTimestampDate);

			Date startDateFormattted = ParserConstants.DATE_FORMAT.parse(startDate);
			DateTime startDateDT = new DateTime(startDateFormattted);

			Date endDateFormatted = ParserConstants.DATE_FORMAT.parse(endDate);
			DateTime endDateDT = new DateTime(endDateFormatted);

			if (logTimeDateTime.isAfter(startDateDT) && logTimeDateTime.isBefore(endDateDT)) {
				return true;
			}

		} catch (ParseException parseEx) {
			System.out.println("Exception when verifying log timestamp: " + parseEx.getMessage());
		}

		return false;
	}
}
