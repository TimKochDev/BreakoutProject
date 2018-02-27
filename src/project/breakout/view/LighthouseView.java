package project.breakout.view;

import java.io.IOException;

import de.cau.infprogoo.lighthouse.LighthouseDisplay;

/**
 * 
 *
 */
public class LighthouseView {
	private static final String USERNAME = Messages.getString("LighthouseView.0"); //$NON-NLS-1$
	private static final String PASSWORD = Messages.getString("LighthouseView.1");  //$NON-NLS-1$
	private static LighthouseDisplay display = new LighthouseDisplay(USERNAME, PASSWORD,25);
	private static byte[] data = new byte[14 * 28 * 3];
	
	/**
	 * Sets up the connection of this class to the lighthouse.
	 * 
	 * @return {@code true} if connection was successful, {@code false} if not.
	 */
	public static boolean connectToLighthouse() {
		try {
			display.connect();
			if (display.isConnected()) {
				System.out.println("connection successfull!"); //$NON-NLS-1$
			} else {
				System.out.println("connection failed!"); //$NON-NLS-1$
			}
			return true;
		} catch (Exception e) {
			System.out.println("Connection failed: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
			return false;
		}
	}

	public static void setBallsPosition(double ballX, double ballY) {
		// ball size 1*1
		// TODO This is just a test yet.
//		byte b = (byte) x;
//		int c = b & 0xff;
	
		for (int i = 0; i < data.length; i += 2) {
			data[i] = (byte) 255;
			//data[i] = (byte) 100;
		}
		System.out.println("send test data");
		updateLighthouseView();
	}
	
	public static void setPaddlePosition(double paddleX, double paddleY) {
		//data position 365-392
		// paddle size 8*1
	}
	
	/**
	 * Removes a brick from the view.
	 * 
	 * @param brick
	 *            The {@code BreakoutBrick} to be removed.
	 */
//	public void removeBrick( brick) {
//		remove(brick);
//	}
//	
	

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
			System.out.println("Data sending failed: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
			return false;
		}
	}

	// ---------Getter-------------------------------------

	/**
	 * Checks if there in a connection to the lighthouse.
	 * 
	 * @return {@code true} if LighthouseView is connected, {@code false} if not.
	 */
	public static boolean isConnected() {
		if (display != null) {
			return display.isConnected();
		} else {
			return false;
		}
	}
}
