/**
 * 
 */
package com.ef.model;

import org.apache.commons.lang3.StringUtils;

/**
 * This ENUM is basically used for comparing the <b>`threshold`</b> input
 * argument value with the pre-defined values. If the user provides any other
 * value to <b>`threshold`</b> argument then the application should throw an
 * error accordingly.
 * 
 * @author Gaurav Kanojia
 *
 */
public enum Duration {
	HOURLY("hourly"), DAILY("daily");

	private String duration;

	Duration() {
	}

	Duration(String duration) {
		this.duration = duration;
	}

	public static Duration findTheDuration(String duration) {

		for (Duration durationValues : Duration.values()) {
			if (StringUtils.equalsIgnoreCase(durationValues.name(), duration.trim())) {
				return durationValues;
			}
		}

		return null;
	}
}
