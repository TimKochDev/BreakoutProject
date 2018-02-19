package project.breakout.view;

import java.io.IOException;

import de.cau.infprogoo.lighthouse.LighthouseDisplay;

public class LighthouseView {
	// test
	private static final String USERNAME = Messages.getString("LighthouseView.0"); //$NON-NLS-1$
	private static final String PASSWORD = Messages.getString("LighthouseView.1"); //$NON-NLS-1$
	private static LighthouseDisplay display = new LighthouseDisplay(USERNAME, PASSWORD);
	private static boolean isConnected = false;
	private static byte[] data = new byte[14 * 28 * 3];;

	/**
	 * Sets up the connection of this class to the lighthouse.
	 * 
	 * @return {@code true} if connection was successful, {@code false} if not.
	 */
	public static boolean connectToLighthouse() {
		try {
			display.connect();
			isConnected = true;
			return true;
		} catch (Exception e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static void setBallsPosition(double ballX, double ballY) {
		// TODO This is just a test yet.
		for (int i = 0; i < data.length; i+=2) {
			data[i] = 100;
		}
		updateLighthouseView();
	}

	/**
	 * This method updates the lightHouseView and sends the new data-array to it.
	 * 
	 * @return {@code true} if successful, {@code false} if not.
	 */
	private static boolean updateLighthouseView() {
		try {
			display.send(data);
			return true;
		} catch (IOException e) {
			System.out.println("Data sending failed: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// ---------Getter-------------------------------------

	/**
	 * @return {@code true} if LighthouseView is connected, {@code false} if not.
	 */
	public static boolean isConnected() {
		return isConnected;
	}
}
