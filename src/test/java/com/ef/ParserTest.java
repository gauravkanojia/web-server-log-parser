/**
 * 
 */
package com.ef;

import org.junit.Test;

/**
 * @author Gaurav Kanojia
 *
 */
public class ParserTest {

	@Test
	public void testParsing() {
		System.out.println("Hello from testing!!");

		String[] args = { "--startDate=2017-01-01.15:00:00", "--duration=hourly", "--threshold=200",
				"--pathToAccessLog=./src/test/resources/test_access.log", "--dbUsername=appuser",
				"--dbPassword=appuser" };
		Parser.main(args);
	}
}
