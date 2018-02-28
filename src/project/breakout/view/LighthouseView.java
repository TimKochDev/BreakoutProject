package project.breakout.view;

import java.io.IOException;
import java.util.Arrays;

import de.cau.infprogoo.lighthouse.LighthouseDisplay;

/**
 * Ligthhouse View class
 * 
 *
 */
public class LighthouseView {
	private static final String USERNAME = Messages.getString("LighthouseView.0"); //$NON-NLS-1$
	private static final String PASSWORD = Messages.getString("LighthouseView.1"); //$NON-NLS-1$
	public static LighthouseDisplay display = new LighthouseDisplay(USERNAME, PASSWORD, 2);

	private final static int FLOORS = 14;
	private final static int WINDOWS_PER_FLOOR = 28;
	private final static int RGB = 3;

	private static byte[] data = new byte[FLOORS * WINDOWS_PER_FLOOR * RGB];

	// paddle size 8*1
	private final static int PADDEL_LENGTH = 8;
	private final static int PADDEL_HEIGHT = 1;

	// ball size 2*1
	private final static int BALL_LENGTH = 2;
	private final static int BALL_HEIGHT = 1;

	// brick size 7*2
	private final static int BRICK_LENGTH = 7;
	private final static int BRICK_HEIGHT = 2;
	
	

	public static void setBrick(int brickX, int brickY) {
		// exception brickX >= WINDOWS_PER_FLOOR - BRICK_LENGTH, brickY >= FLOORS - BRICK_HEIGHT

		// red
		for (int h = 0; h < BRICK_HEIGHT; h++) {
			for (int l = 0; l < BRICK_LENGTH; l++) {
				data[(int) (((brickX + l) + (brickY + h) * WINDOWS_PER_FLOOR) * RGB)] = (byte) 0;
			}
		}

		// green
		for (int h = 0; h < BRICK_HEIGHT; h++) {
			for (int l = 0; l < BRICK_LENGTH; l++) {
				data[(int) ((((brickX + l) + (brickY + h) * WINDOWS_PER_FLOOR) * RGB) + 1)] = (byte) 205;
			}
		}

		// blue
		for (int h = 0; h < BRICK_HEIGHT; h++) {
			for (int l = 0; l < BRICK_LENGTH; l++) {
				data[(int) ((((brickX + l) + (brickY + h) * WINDOWS_PER_FLOOR) * RGB) + 2)] = (byte) 255;
			}
		}
		updateLighthouseView();

	}

	public static void setBallsPosition(double ballX, double ballY) {
		// exception ballX >= WINDOWS_PER_FLOOR - BALL_LENGTH, ballY >= FLOORS - BALL_HEIGHT
		for (int i = 0; i < BALL_LENGTH; i++) {
			data[(int) (((ballX + i) + ballY * WINDOWS_PER_FLOOR) * RGB)] = (byte) 255;
		}
		updateLighthouseView();
	}

	public static void setPaddlePosition(double paddleX, double paddleY) {
		// exception paddleX >= WINDOWS_PER_FLOOR - PADDEL_LENGTH || paddleY != FLOORS - PADDEL_HEIGHT

		// red
		for (int i = 0; i < PADDEL_LENGTH; i++) {
			data[(int) (((paddleX + i) + paddleY * WINDOWS_PER_FLOOR) * RGB)] = (byte) 255;
		}
		// green
		for (int i = 0; i < PADDEL_LENGTH; i++) {
			data[(int) ((((paddleX + i) + paddleY * WINDOWS_PER_FLOOR) * RGB) + 1)] = (byte) 100;
		}
		// blue
		for (int i = 0; i < PADDEL_LENGTH; i++) {
			data[(int) ((((paddleX + i) + paddleY * WINDOWS_PER_FLOOR) * RGB) + 2)] = (byte) 200;
		}
		updateLighthouseView();
	}
	
	public static void setAllDark() {
		for (int i = 0; i< data.length; i++) {
			data[i] = (byte) 0;
		}
		updateLighthouseView();
	}

	/**
	 * Removes a brick from the view.
	 * 
	 * @param brick
	 *            The {@code BreakoutBrick} to be removed.
	 */
	// public void removeBrick( brick) {
	// remove(brick);
	// }
	//

	/**
	 * This method updates the lightHouseView and sends the new data-array to it.
	 * 
	 * @return {@code true} if successful, {@code false} if not.
	 */
	private static void updateLighthouseView() {
		try {
			display.send(data);
			System.out.println(Arrays.toString(data));
		} catch (IOException e) {
			System.out.println("Data sending failed: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		}
		System.out.println(display.isConnected());
	}
	
	/**
	 * Sets up the connection of this class to the lighthouse.
	 * 
	 * @return {@code true} if connection was successful, {@code false} if not.
	 */
	public static boolean connectToLighthouse() {
		try {
			display.connect();
			return true;
		} catch (Exception e) {
			System.out.println("Connection failed: " + e.getMessage()); //$NON-NLS-1$
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
