/**File: Intersection.java
 * Author: Kevin Abrahams
 * Date: 02-03-2021
 * Purpose: This classs shall facilitate the implementation of the intersection threads.
 */
import java.awt.Color;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JLabel;

public class Intersection implements Runnable {
	private final String[] COLORS = { "Green", "Yellow", "Red" };
	private int i = 0;
	private String currentLight = COLORS[i];
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private final AtomicBoolean suspended = new AtomicBoolean(false);
	private Thread thread;
	private String threadName;
	private JLabel colorLabel;

	/**
	 * Constructor used to be initialize
	 * 
	 * @param name       - Name of the thread
	 * @param colorLabel - Color label
	 */
	public Intersection(String name, JLabel colorLabel) {
		this.threadName = name;
		this.colorLabel = colorLabel;
	}

	/**
	 * Get the color
	 * 
	 * @return Get the current color
	 */
	public synchronized String getColor() {
		this.currentLight = COLORS[i];
		return this.currentLight;
	}

	/**
	 * Suspend the thread
	 */
	public void suspend() {
		suspended.set(true);
	}

	/**
	 * Resume the thread
	 */
	public synchronized void resume() {
		suspended.set(false);
		notify();
	}

	/**
	 * Start the thread
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}

	/**
	 * Stop the thread
	 */
	public void stop() {
		thread.interrupt();
		isRunning.set(false);
	}

	/**
	 * Interrupt the thread
	 */
	public void interrupt() {
		thread.interrupt();
	}

	@Override
	public void run() {
		isRunning.set(true);
		while (isRunning.get()) {
			try {
				synchronized (this) {
					while (suspended.get()) {
						wait();
					}
				}
				// Update the color
				switch (getColor()) {
				case Constants.GREEN_COLOR:
					colorLabel.setForeground(new Color(0, 200, 10));
					colorLabel.setText(getColor());
					Thread.sleep(10000);
					i++;
					break;
				case Constants.YELLOW_COLOR:
					colorLabel.setForeground(new Color(247, 226, 35));
					colorLabel.setText(getColor());
					Thread.sleep(5000);
					i++;
					break;
				case Constants.RED_COLOR:
					colorLabel.setForeground(Color.RED);
					colorLabel.setText(getColor());
					Thread.sleep(5000);
					i = 0;
					break;
				default:
					break;
				}

			} catch (InterruptedException ex) {
				suspended.set(true);
			}
		}
	}

}
