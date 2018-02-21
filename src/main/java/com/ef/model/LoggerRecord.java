/**
 * 
 */
package com.ef.model;

import java.util.Objects;

/**
 * This is a database entity model which is used for inserting records into
 * MySQL database on the basis of the runs done by the application.
 * 
 * @author Gaurav Kanojia
 *
 */
public class LoggerRecord {

	private long _id;
	private String startDate;
	private String endDate;
	private int threshold;
	private Duration duration;
	private String logTimestamp;
	private String ipAddress;
	private int requests;
	private String status;
	private String userAgent;
	private String comments;

	public LoggerRecord() {
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

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

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public String getLogTimestamp() {
		return logTimestamp;
	}

	public void setLogTimestamp(String logTimestamp) {
		this.logTimestamp = logTimestamp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getRequests() {
		return requests;
	}

	public void setRequests(int requests) {
		this.requests = requests;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ipAddress, startDate, endDate, threshold, duration, userAgent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		LoggerRecord loggerRecord = (LoggerRecord) obj;
		return Objects.equals(threshold, loggerRecord.threshold) 
				&& Objects.equals(endDate, loggerRecord.endDate)
				&& Objects.equals(startDate, loggerRecord.startDate) 
				&& Objects.equals(duration, loggerRecord.duration)
				&& Objects.equals(ipAddress, loggerRecord.ipAddress)
				&& Objects.equals(userAgent, loggerRecord.userAgent);
	}

	@Override
	public String toString() {
		return "LoggerRecord [_id=" + _id + ", startDate=" + startDate + ", endDate=" + endDate + ", threshold="
				+ threshold + ", duration=" + duration + ", logTimestamp=" + logTimestamp + ", ipAddress=" + ipAddress
				+ ", requests=" + requests + ", status=" + status + ", userAgent=" + userAgent + ", comments="
				+ comments + "]";
	}

}
