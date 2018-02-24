/**
 * 
 */
package com.ef;

import java.io.File;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.ef.config.ParserConstants;
import com.ef.model.Duration;
import com.ef.model.InputParams;
import com.ef.parse.AccessLogsParseUtility;

/**
 * @author Gaurav Kanojia
 *
 */
public class Parser {

	private static final String ARGS_SEPARATOR = "=";

	private static final String ARG_START_DATE = "--startDate";

	private static final String ARG_DURATION = "--duration";

	private static final String ARG_THRESHOLD = "--threshold";

	private static final String ARG_PATH_TO_LOGS = "--pathToAccessLog";

	private static final String ARG_DB_USERNAME = "--dbUsername";

	private static final String ARG_DB_PASSWORD = "--dbPassword";

	/**
	 * Entry point for the program which captures the command line arguments and
	 * then processes it as per the instructions provided.
	 * 
	 * @param args
	 *            - An Array of arguments provided to the program. In this case
	 *            they are as:
	 *            <ol>
	 *            <li>"startDate"</li>
	 *            <li>"duration"</li>
	 *            <li>"threshold"</li>
	 *            <li>"pathToAccessLog"</li> <b><i>Optional parameters</i></b>:
	 *            <li>"dbUsername"</li>
	 *            <li>"dbPassword"</li>
	 *            </ol>
	 */
	public static void main(String[] args) {

		AccessLogsParseUtility parseUtil = new AccessLogsParseUtility();

		// Check if the arguments are provided to the program or not
		if (args.length > 0) {

			InputParams inputParams = new InputParams();

			for (String arg : args) {
				String[] params = arg.split(ARGS_SEPARATOR);

				// Depending on the argument type, fetch the value of it.
				switch (params[0]) {
				case ARG_START_DATE:
					try {
						Date providedStartDate = ParserConstants.DATE_FORMAT.parse(params[1]);
						inputParams.setStartDate(ParserConstants.DATE_FORMAT.format(providedStartDate));
					} catch (ParseException parseEx) {
						throw new IllegalArgumentException("Argument " + ARG_START_DATE
								+ " is in improper format. Exception is: " + parseEx.getMessage());
					}

					break;
				case ARG_DURATION:
					Duration providedDuration = Duration.findTheDuration(params[1].toUpperCase());
					if (providedDuration == null) {
						throw new IllegalArgumentException(
								"Argument " + ARG_DURATION + " is invalid. Valid values are 'daily' and/or 'hourly'.");
					}
					inputParams.setDuration(providedDuration);

					break;
				case ARG_THRESHOLD:
					int providedThreshold = Integer.parseInt(params[1]);
					if (providedThreshold <= 0) {
						throw new IllegalArgumentException("Argument" + ARG_THRESHOLD + " must be an integer");
					}
					inputParams.setThreshold(providedThreshold);

					break;
				case ARG_PATH_TO_LOGS:
					String providedFilePath = params[1];
					if (!new File(providedFilePath).exists()) {
						throw new IllegalArgumentException("File path " + ARG_PATH_TO_LOGS + " does not exists.");
					}
					inputParams.setPathToAccessLog(providedFilePath);

					break;
				case ARG_DB_USERNAME:
					String providedDBUserName = params[1];
					if (StringUtils.isNotBlank(providedDBUserName)) {
						inputParams.setDatabaseUsername(providedDBUserName);
					}

					break;
				case ARG_DB_PASSWORD:
					String providedDBPassword = params[1];
					if (StringUtils.isNotBlank(providedDBPassword)) {
						inputParams.setDatabasePassword(providedDBPassword);
					}

					break;

				default:
					System.out.println(
							"Please provide valid arguments to execute the parser. Refer to README file for execution commands");
				}
			}

			parseUtil.calculateEndDateFromInputParams(inputParams);

			// Parse the logs and output on console
			parseUtil.parseLogsFromInputParams(inputParams);
			System.out.println("The logs are processed now.");

		} else {
			// Send messages to the console logs
			System.out.println("Please provide arguments to execute the program. Refer to README file.");
		}
	}
}
