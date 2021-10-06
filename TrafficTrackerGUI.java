/**File: TrafficTrackerGUI.java
 * Author: Kevin Abrahams
 * Date: 02-03-2021
 * Purpose: This classs shall implement the GUI for the traffic tracker.
 */
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TrafficTrackerGUI extends JFrame implements Runnable, ChangeListener {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 6060135120385256705L;
	// Labels
	static JLabel timeText = new JLabel();
	static JLabel trafficAtext = new JLabel();
	static JLabel trafficBtext = new JLabel();
	static JLabel trafficCtext = new JLabel();

	// start, pause, and stop buttons
	private JButton start = new JButton("Start");
	private JButton pause = new JButton("Pause");
	private JButton stop = new JButton("Stop");

	// JSliders for showing car progress
	static JSlider car1Slider = new JSlider(0, 3000);
	static JSlider car2Slider = new JSlider(0, 3000);
	static JSlider car3Slider = new JSlider(0, 3000);

	private static boolean isRunning;
	private static final AtomicBoolean isSimulationRunning = new AtomicBoolean(false);

	// Intersection Threads
	private Intersection intersection1 = new Intersection("intersectionThread1", trafficAtext);
	private Intersection intersection2 = new Intersection("intersectionThread2", trafficBtext);
	private Intersection intersection3 = new Intersection("intersectionThread3", trafficCtext);

	// Car objects
	Car car1 = new Car("Car1Thread", 300, 0);
	Car car2 = new Car("Car2Thread", 1000, 0);
	Car car3 = new Car("Car3Thread", 2000, 1000);

	// Array of cars to loop through later
	private Car[] carArray = { car1, car2, car3};
	private Intersection[] intersectionArray = { intersection1, intersection2, intersection3 };
	private static Thread gui;

	private Object[][] trafficData = { { "Car 1", car1.getPosition(), 0, 0 }, { "Car 2", car2.getPosition(), 0, 0 },
			{ "Car 3", car3.getPosition(), 0, 0 }};

	private String[] columnNames = { "Car", "X-Pos", "Y-Pos","Speed km/h" };
	private JTable dataTable = new JTable(trafficData, columnNames);

	/**
	 * Constructor used to initialize the GUI
	 */
	public TrafficTrackerGUI() {
		super("CMSC 335 Project 3: Traffic Tracker");
		isRunning = Thread.currentThread().isAlive();
		initializeGUI();
		setButtons();
	}

	/**
	 * Display the GUI
	 */
	public void display() {
		setSize(600, 400);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui = new Thread(this);
		Thread time = new Thread(new Time());
		time.start();
	}

	/**
	 * Initialize the GUI
	 */
	private void initializeGUI() {
		JLabel welcome = new JLabel("Welcome to the Traffic Tracker Simulator!");
		JLabel welcome2 = new JLabel("Click the Start button to begin simulation");

		JLabel time = new JLabel("Current time: ");
		JLabel trafficLightA = new JLabel("First Intersection: ");
		JLabel trafficLightB = new JLabel("Second Intersection: ");
		JLabel trafficLightC = new JLabel("Third Intersection: ");

		// Add changeListeners to car sliders
		car1Slider.addChangeListener(this);
		car2Slider.addChangeListener(this);
		car3Slider.addChangeListener(this);

		car1Slider.setValue(car1.getPosition());
		car2Slider.setValue(car2.getPosition());
		car3Slider.setValue(car3.getPosition());

		car1Slider.setMajorTickSpacing(1000);
		car1Slider.setPaintTicks(true);

		car2Slider.setMajorTickSpacing(1000);
		car2Slider.setPaintTicks(true);

		dataTable.setPreferredScrollableViewportSize(new Dimension(400, 70));

		JPanel dataPanel = new JPanel();

		JScrollPane scrollPane = new JScrollPane(dataTable);
		dataPanel.add(scrollPane);
		setLayout(welcome, welcome2, time, trafficLightA, trafficLightB, trafficLightC, dataPanel);
		pack();
	}

	/**
	 * Set the layout
	 * 
	 * @param welcome
	 * @param welcome2
	 * @param time
	 * @param trafficLightA
	 * @param trafficLightB
	 * @param trafficLightC
	 * @param dataPanel
	 */
	private void setLayout(JLabel welcome, JLabel welcome2, JLabel time, JLabel trafficLightA, JLabel trafficLightB,
			JLabel trafficLightC, JPanel dataPanel) {
		// GUI Layout
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup().addContainerGap(30, 30) // Container gap on left side
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(welcome)
						.addComponent(welcome2)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addComponent(time).addComponent(timeText)))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addGroup(layout.createSequentialGroup().addComponent(start).addComponent(pause)
										.addComponent(stop)))
						.addComponent(car1Slider).addComponent(car2Slider).addComponent(car3Slider)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addGroup(layout.createSequentialGroup().addComponent(trafficLightA)
										.addComponent(trafficAtext).addContainerGap(20, 20).addComponent(trafficLightB)
										.addComponent(trafficBtext).addContainerGap(20, 20).addComponent(trafficLightC)
										.addComponent(trafficCtext))
								.addComponent(dataPanel)))

				.addContainerGap(30, 30) // Container gap on right side

		);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createSequentialGroup().addComponent(welcome).addComponent(welcome2))
				.addGap(20, 20, 20)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(time)
						.addComponent(timeText))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(start)
						.addComponent(pause).addComponent(stop))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(car1Slider)
						.addComponent(car2Slider).addComponent(car3Slider))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(trafficLightA)
						.addComponent(trafficAtext).addComponent(trafficLightB).addComponent(trafficBtext)
						.addComponent(trafficLightC).addComponent(trafficCtext))
				.addComponent(dataPanel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addGap(20, 20, 20))
				.addGap(20, 20, 20));
	}

	private void setButtons() {
		start.addActionListener((ActionEvent e) -> {
			if (!isSimulationRunning.get()) {
				intersection1.start();
				intersection2.start();
				intersection3.start();
				car1.start();
				car2.start();
				car3.start();

				gui.start();

			}
			isSimulationRunning.set(true);
		});

		pause.addActionListener((ActionEvent e) -> {
			if (isSimulationRunning.get()) {
				for (Car i : carArray) {
					i.suspend();
				}
				for (Intersection i : intersectionArray) {
					i.interrupt();
					i.suspend();
				}

				pause.setText("Continue");
				isSimulationRunning.set(false);
			} else {
				for (Car i : carArray) {
					if (i.suspended.get()) {
						i.resume();
					}
				}
				for (Intersection i : intersectionArray) {
					i.resume();
				}
				pause.setText("Pause");
				isSimulationRunning.set(true);
			}
		});

		stop.addActionListener((ActionEvent e) -> {
			if (isSimulationRunning.get()) {
				for (Car i : carArray) {
					i.stop();
				}
				for (Intersection i : intersectionArray) {
					i.stop();
				}
				isSimulationRunning.set(false);
			}
		});
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		trafficData[0][1] = car1Slider.getValue();
		trafficData[1][1] = car2Slider.getValue();
		trafficData[2][1] = car3Slider.getValue();

		trafficData[0][3] = car1.getSpeed() + Constants.KM_PER_HOUR;
		trafficData[1][3] = car2.getSpeed() + Constants.KM_PER_HOUR;
		trafficData[2][3] = car3.getSpeed() + Constants.KM_PER_HOUR;
		dataTable.repaint();
	}

	/**
	 * Update the car details
	 */
	private void getData() {
		if (isSimulationRunning.get()) {
			switch (intersection1.getColor()) {
			case Constants.RED_COLOR:
				for (Car i : carArray) {
					if (i.getPosition() > 500 && i.getPosition() < 1000) {
						i.lighting.set(true);
					}
				}
				break;
			case Constants.GREEN_COLOR:
				for (Car i : carArray) {
					if (i.lighting.get()) {
						i.resume();
					}
				}
				break;
			}

			switch (intersection2.getColor()) {
			case Constants.RED_COLOR:
				for (Car i : carArray) {
					if (i.getPosition() > 1500 && i.getPosition() < 2000) {
						i.lighting.set(true);
					}
				}
				break;
			case Constants.GREEN_COLOR:
				for (Car i : carArray) {
					if (i.lighting.get()) {
						i.resume();
					}
				}
				break;
			}

			switch (intersection3.getColor()) {
			case Constants.RED_COLOR:
				for (Car i : carArray) {
					if (i.getPosition() > 2500 && i.getPosition() < 3000) {
						i.lighting.set(true);
					}
				}
				break;
			case Constants.GREEN_COLOR:
				for (Car i : carArray) {
					if (i.lighting.get()) {
						i.resume();
					}
				}
				break;
			}
		}

	}

	@Override
	public void run() {
		while (isRunning) {
			if (isSimulationRunning.get()) {
				car1Slider.setValue(car1.getPosition());
				car2Slider.setValue(car2.getPosition());
				car3Slider.setValue(car3.getPosition());
				getData();
			}
		}
	}
}
