/**File: Main.java
 * Author: Kevin Abrahams
 * Date: 02-03-2021
 * Purpose: This classs shall serve as the main class of the application which shall launch it through its main method.
 */
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

	/**
	 * Main method of the application
	 * 
	 * @param args - Array of the command line arguments
	 */
	public static void main(String[] args) {
		setLookAndFeel();
		TrafficTrackerGUI test = new TrafficTrackerGUI();
		test.display();
	}

	/**
	 * Set the look and feel
	 */
	public static void setLookAndFeel() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}
	}
}
