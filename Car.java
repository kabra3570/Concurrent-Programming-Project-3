/**File: Car.java
 * Author: Kevin Abrahams
 * Date: 02-03-2021
 * Purpose: This classs shall implement the cars in the application. 
 */
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
	private int position;
	// Boolean value to indicates that car is running or not
	private final AtomicBoolean running = new AtomicBoolean(false);
	// Boolean value to indicates that car is lighting or not
	public final AtomicBoolean lighting = new AtomicBoolean(false);
	// Boolean value to indicates that car is susspended or not
	public final AtomicBoolean suspended = new AtomicBoolean(false);
	private String name = "";// Thread name
	public Thread thread;// Thread to run each car
	private int speed = 0;// Speed of the car

	/**
	 * Constructor used to initialize the car
	 * 
	 * @param name - Name of the car
	 * @param max  - Max X position
	 * @param min  - Min X position
	 */
	public Car(String name, int max, int min) {
		this.name = name;
		this.position = ThreadLocalRandom.current().nextInt(min, max);
	}

	/**
	 * Get the X position of car
	 * 
	 * @return X position of car
	 */
	public synchronized int getPosition() {
		return position;
	}

	/**
	 * Get the speed of the car
	 * 
	 * @return
	 */
	public int getSpeed() {
		if (running.get()) {
			if (lighting.get())
				speed = 0;
			else
				speed = 3 * 60;
		} else
			speed = 0;
		return speed;
	}

	/**
	 * Start the car
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this, name);
			thread.start();
		}
	}

	/**
	 * Stop the car
	 */
	public void stop() {
		thread.interrupt();
		running.set(false);
	}

	/**
	 * Suspend the car
	 */
	public void suspend() {
		suspended.set(true);
	}

	/**
	 * Resume the car
	 */
	public synchronized void resume() {
		if (suspended.get() || lighting.get()) {
			suspended.set(false);
			lighting.set(false);
			notify();
		}
	}

	/**
	 * Start the thread
	 */
	@Override
	public void run() {
		running.set(true);
		while (running.get()) {
			try {
				while (position < 3000) {
					synchronized (this) {
						while (suspended.get() || lighting.get()) {
							wait();
						}
					}
					if (running.get()) {
						Thread.sleep(100);
						position += 5;
					}
				}
				position = 0;
			} catch (InterruptedException ex) {
				return;
			}
		}
	}

}
