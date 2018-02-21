/**
 * 
 */
package com.ef.model;

/**
 * This model class is used for capturing the input parameters/arguments to the
 * application.
 * 
 * @author Gaurav Kanojia
 *
 */
public class InputParams {

	private String startDate;
	private String endDate;
	private Duration duration;
	private String databaseUsername;
	private String databasePassword;
	private int threshold;
	private String pathToAccessLog;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public String getPathToAccessLog() {
		return pathToAccessLog;
	}

	public void setPathToAccessLog(String pathToAccessLog) {
		this.pathToAccessLog = pathToAccessLog;
	}

	@Override
	public String toString() {
		return "InputParams [startDate=" + startDate + ", endDate=" + endDate + ", duration=" + duration
				+ ", databaseUsername=" + databaseUsername + ", databasePassword=" + databasePassword + ", threshold="
				+ threshold + ", pathToAccessLog=" + pathToAccessLog + "]";
	}

}
