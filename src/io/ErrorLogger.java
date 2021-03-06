package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import options.GlobalOptions;

public class ErrorLogger {
	private static String logFileName = "errors.log";
	private static String version = "unknown";

	public static void setVersion(String version) {
		ErrorLogger.version = version;
	}

	/**
	 * Logs an exception.
	 * 
	 * @param e
	 * @param severity
	 *            Severity from 1 to 5
	 */
	public static void log(Exception e, String message, int severity) {
		if (GlobalOptions.debug()) {
			if (message != null) {
				System.err.println(message);
			}
			if (e != null) {
				e.printStackTrace();
			}
		}
		File logFile = new File(logFileName);
		Date d = new Date();
		try {
			logFile.createNewFile();
			// Open a BufferedWriter in append mode
			BufferedWriter out = new BufferedWriter(
					new FileWriter(logFile, true));
			out.newLine();
			out.write(d.toString());
			out.newLine();
			out.write(version);
			out.newLine();
			out.write("Severity: " + severity);
			out.newLine();
			if (message != null) {
				out.write(message);
				out.newLine();
			}
			if (e != null) {
				out.write(e.toString());
				out.newLine();
				if (e.getMessage() != null) {
					out.write(e.getMessage());
					out.newLine();
				}
				StackTraceElement[] stes = e.getStackTrace();
				for (StackTraceElement ste : stes) {
					out.write("\t" + ste.toString());
					out.newLine();
				}
			}
			out.close();
		} catch (IOException ioe) {
			System.err.println("IO exception while outputting to log file.");
		}
	}

	public static void log(String message, int severity) {
		log(null, message, severity);
	}
	
	public static void log(Exception e, int severity) {
		log(e, null, severity);
	}
}
