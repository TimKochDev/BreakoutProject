package project.breakout.view;

import java.io.IOException;

import de.cau.infprogoo.lighthouse.LighthouseDisplay;

/**
 * Lighthouse View class
 * 
 *
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

	// brick size 7*2
	private final static int BRICK_LENGTH = 7;
	private final static int BRICK_HEIGHT = 2;

	/**
	 * Sets the position of the brick at a certain position in the data array. The
	 * brick position shouldn't be higher/smaller than the amount of floors and
	 * windows per floor.
	 * 
	 * @param brickX
	 *            The X-position of the brick in the game.
	 * @param brickY
	 *            The Y-position of the brick in the game.
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
		// Convert from relative position to window position
		int windowX = (int) (27 * relativeX);
		int windowY = (int) (13 * relativeY);

		// exception handling
		if (windowX < 0 || windowX + BALL_LENGTH >= WINDOWS_PER_FLOOR) {
			throw new IllegalArgumentException("X-Coordinate of the ball out of range.");
		}
		if (windowY < 0 || windowY + BALL_HEIGHT >= FLOORS) {
			throw new IllegalArgumentException("Y-Coordinate of the ball out of range.");
		}

		// insert ball in array
		for (int i = 0; i < BALL_LENGTH; i++) {
			int index = (int) (((windowX + i) + windowY * WINDOWS_PER_FLOOR) * RGB);
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

	public static void setAllDark() {
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) 0;
		}
		updateLighthouseView();
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
	private static int getBallPosition() {
		return 0;
		
	}
	private static void removeBall() {
		
		updateLighthouseView();

	}
}
