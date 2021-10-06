/**File: Time.java
 * Author: Kevin Abrahams
 * Date: 02-03-2021
 * Purpose: This classs shall functions to update the time text.
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time implements Runnable {
	private boolean isRunning;// Boolean value to indicate thread is running
	private String timePattern = "hh:mm:ss a";// Time format
	private DateFormat timeFormat = new SimpleDateFormat(timePattern);// Simple date time formatter
	private Date date;// Current date

	/**
	 * Constructor used to set thread is running or not
	 */
	public Time() {
		this.isRunning = Thread.currentThread().isAlive();
	}

	/**
	 * Get the time
	 * 
	 * @return Formated time
	 */
	public String getTime() {
		date = new Date(System.currentTimeMillis());
		return timeFormat.format(date);
	}

	@Override
	public void run() {
		while (isRunning) {
			TrafficTrackerGUI.timeText.setText(getTime());
		}
	}
}
