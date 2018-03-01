package project.breakout.view;

import java.io.IOException;

import de.cau.infprogoo.lighthouse.LighthouseDisplay;

/**
 * Lighthouse View class. This class handles the connection between the
 * BreakoutModel and the LighthouseDisplay. The username and token for the
 * connection is in a properties-file for safety reasons.
 */
public class LighthouseView {
	private static final String USERNAME = Messages.getString("LighthouseView.0"); //$NON-NLS-1$
	private static final String PASSWORD = Messages.getString("LighthouseView.1"); //$NON-NLS-1$
	public static LighthouseDisplay display = new LighthouseDisplay(USERNAME, PASSWORD);

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

	private static int ballX;
	private static int ballY;

	// brick size 7*2
	private final static int BRICK_LENGTH = 7;
	private final static int BRICK_HEIGHT = 2;

	/**
	 * Sets the position of the brick at a certain position in the data array. The
	 * brick position shouldn't be higher/smaller than the amount of floors and
	 * windows per floor.
	 * 
	 * @param brickX
	 *            The X-position of the brick in the array.
	 * @param brickY
	 *            The Y-position of the brick in the array.
	 */
	public static void setBrick(int brickX, int brickY) {

		assert brickX >= 0 && brickX < WINDOWS_PER_FLOOR
				- BRICK_LENGTH : "LighhouseView: ballX < 0 or > WINDOWS_PER_FLOOR - BRICK_LENGTH";
		assert brickY >= 0 && brickY < FLOORS - BRICK_HEIGHT : "LighhouseView: ballY < 0 or > FLOORS - BRICK_HEIGHT";

		// red
		for (int h = 0; h < BRICK_HEIGHT; h++) {
			for (int l = 0; l < BRICK_LENGTH; l++) {
				int index = (int) (((brickX + l) + (brickY + h) * WINDOWS_PER_FLOOR) * RGB);
				data[index] = (byte) 0;
			}
		}

		// green
		for (int h = 0; h < BRICK_HEIGHT; h++) {
			for (int l = 0; l < BRICK_LENGTH; l++) {
				int index = (int) ((((brickX + l) + (brickY + h) * WINDOWS_PER_FLOOR) * RGB) + 1);
				data[index] = (byte) 205;
			}
		}

		// blue
		for (int h = 0; h < BRICK_HEIGHT; h++) {
			for (int l = 0; l < BRICK_LENGTH; l++) {
				int index = (int) ((((brickX + l) + (brickY + h) * WINDOWS_PER_FLOOR) * RGB) + 2);
				data[index] = (byte) 255;
			}
		}
		updateLighthouseView();

	}

	/**
	 * Sets the position of the ball at a certain position in the data array. The
	 * ball position shouldn't be higher/smaller than the amount of floors and
	 * windows per floor.
	 * 
	 * @param relativeX
	 *            The X-position of the ball in the game.
	 * @param relativeY
	 *            The Y-position of the ball in the game.
	 */
	public static void setBallPosition(double relativeX, double relativeY) {
		for (int i = 0; i < BALL_LENGTH; i++) {
			setWindowDark(getBallXPosition() + i, getBallYPosition() + i);
		}
		// Convert from relative position to window position
		ballX = (int) (27 * relativeX);
		ballY = (int) (13 * relativeY);

		// exception handling
		if (ballX < 0 || ballX + BALL_LENGTH >= WINDOWS_PER_FLOOR) {
			throw new IllegalArgumentException("X-Coordinate of the ball out of range.");
		}
		if (ballY < 0 || ballY + BALL_HEIGHT >= FLOORS) {
			throw new IllegalArgumentException("Y-Coordinate of the ball out of range.");
		}

		// insert ball in array
		for (int i = 0; i < BALL_LENGTH; i++) {
			int index = (int) (((ballX + i) + ballY * WINDOWS_PER_FLOOR) * RGB);
			data[index] = (byte) 255;
		}
		updateLighthouseView();
	}

	/**
	 * Sets the position of the paddle at a certain position in the data array. The
	 * paddle X-position shouldn't be higher/smaller than the amount of windows per
	 * floor. The Y-position of the paddle should be in the lowest row of windows.
	 * 
	 * @param paddleX
	 *            The X-position of the paddle in the game.
	 * @param paddleY
	 *            The Y-position of the paddle in the game.
	 */
	public static void setPaddlePosition(double paddleX, double paddleY) {
		assert paddleX < WINDOWS_PER_FLOOR - BALL_LENGTH : "LighhouseView: paddleX < 0";
		assert paddleY == FLOORS - 1 : "LighhouseView: paddleY != FLOORS";

		// red
		for (int i = 0; i < PADDEL_LENGTH; i++) {
			int index = (int) (((paddleX + i) + paddleY * WINDOWS_PER_FLOOR) * RGB);
			data[index] = (byte) 255;
		}
		// green
		for (int i = 0; i < PADDEL_LENGTH; i++) {
			int index = (int) ((((paddleX + i) + paddleY * WINDOWS_PER_FLOOR) * RGB) + 1);
			data[index] = (byte) 100;
		}
		// blue
		for (int i = 0; i < PADDEL_LENGTH; i++) {
			int index = (int) ((((paddleX + i) + paddleY * WINDOWS_PER_FLOOR) * RGB) + 2);
			data[index] = (byte) 200;
		}
		updateLighthouseView();
	}

	/**
	 * Sets all windows of the lighthouse dark.
	 */
	public static void setAllDark() {
		// TODO remove after testing
		setWindowDark(13, 13);
		updateLighthouseView();

		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) 0;
		}
		updateLighthouseView();
	}

	/**
	 * Sets the window dark.
	 * 
	 * @param windowX
	 *            The x-th window in a Lighthouse-floor.
	 * 
	 * @param windowY
	 *            The floor in which the window is set.
	 */
	private static void setWindowDark(int windowX, int windowY) {
		// compute index in array
		int index = (int) ((windowX + windowY * WINDOWS_PER_FLOOR) * RGB);

		// Set red, green and blue to zero
		for (int i = 0; i < RGB; i++) {
			data[index + i] = (byte) 0;
		}
	}

	/**
	 * This method updates the lightHouseView and sends the new data-array to it.
	 * 
	 * @return {@code true} if successful, {@code false} if not.
	 */
	private static void updateLighthouseView() {
		try {
			display.send(data);
		} catch (IOException e) {
			System.out.println("Data sending failed: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		} catch (IllegalStateException e) {
			if (e.getMessage().contains("BLOCKING")) {
				System.out.println("Connection overflow");
			}
		}
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

	private static int getBallXPosition() {
		return ballX;
	}

	private static int getBallYPosition() {
		return ballY;
	}

	private static void removeBall() {
		setWindowDark(getBallXPosition(), getBallYPosition());
		updateLighthouseView();
	}

	private static int getBallPosition() {
		return 0;

	}

}
