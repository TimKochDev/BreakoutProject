package project.breakout.view;

import java.io.IOException;

import de.cau.infprogoo.lighthouse.LighthouseDisplay;
import javafx.scene.paint.Color;

/**
 * Lighthouse View class. This class handles the connection between the
 * BreakoutModel and the LighthouseDisplay. The username and token for the
 * connection is in a properties-file for safety reasons.
 */
public class LighthouseView {
	private static final String USERNAME = Messages.getString("LighthouseView.0"); //$NON-NLS-1$
	private static final String PASSWORD = Messages.getString("LighthouseView.1"); //$NON-NLS-1$
	public static LighthouseDisplay display = new LighthouseDisplay(USERNAME, PASSWORD);

	// data of the lighthouse
	private final static int FLOORS = 14;
	private final static int WINDOWS_PER_FLOOR = 28;
	private final static int RGB = 3;

	/**
	 * ByteArray which represents the window colors of the lighthouse. Every window
	 * is related with three bytes in a row - red, green and blue. Thus the first
	 * three indices of the array represent the rgb value of the top left window.
	 */
	private static byte[] data = new byte[FLOORS * WINDOWS_PER_FLOOR * RGB];

	// paddle size 8*1
	private static int paddleWidth = 8;

	private static int paddleX;
	private static final int PADDLE_Y = FLOORS - 1;

	// ball size 2*1
	private final static int BALL_LENGTH = 2;
	private final static int BALL_HEIGHT = 1;

	private static int ballX;
	private static int ballY;

	// brick size 7*1
	private final static int BRICK_LENGTH = 7;
	private final static int BRICK_HEIGHT = 1;

	/**
	 * Sets the position of the brick at a certain position in the data array. The
	 * brick position shouldn't be higher/smaller than the amount of floors and
	 * windows per floor.
	 * 
	 * @param relativeBrickX
	 *            The relative X-position of the brick in the view.
	 * @param relativeBrickY
	 *            The relative Y-position of the brick in the view.
	 */
	public static void setBrick(double relativeBrickX, double relativeBrickY) throws IllegalArgumentException {
		// Convert from relative position to window position
		int brickX = (int) (27 * relativeBrickX);
		int brickY = (int) (13 * relativeBrickY);

		// exception handling
		if (brickX < 0 || brickX + BRICK_LENGTH >= WINDOWS_PER_FLOOR) {
			throw new IllegalArgumentException("X-Coordinate of the brick out of range.");
		}
		if (brickY < 0 || brickY + BRICK_HEIGHT >= FLOORS) {
			throw new IllegalArgumentException("Y-Coordinate of the brick out of range.");
		}

		// red
		for (int h = 0; h < BRICK_HEIGHT; h++) {
			for (int l = 0; l < BRICK_LENGTH; l++) {
				int index = indexInDataArray(brickX + l, brickY + h, Color.RED);
				data[index] = (byte) 0;
			}
		}

		// green
		for (int h = 0; h < BRICK_HEIGHT; h++) {
			for (int l = 0; l < BRICK_LENGTH; l++) {
				int index = indexInDataArray(brickX + l, brickY + h, Color.GREEN);
				data[index] = (byte) 205;
			}
		}

		// blue
		for (int h = 0; h < BRICK_HEIGHT; h++) {
			for (int l = 0; l < BRICK_LENGTH; l++) {
				int index = indexInDataArray(brickX + l, brickY + h, Color.BLUE);
				data[index] = (byte) 255;
			}
		}
		updateLighthouseView();
	}

	/**
	 * This method takes an array of {@code BreakoutBrick}s and puts it on the
	 * lighthouse display.
	 * 
	 * @param brickArray
	 *            The array of {@code BreakoutBrick}s to put on the screen.
	 * @param screenWidth
	 *            The width of the screen in the {@code BreakoutView}.
	 * @param screenHeight
	 *            The height of the screen in the {@code BreakoutView}.
	 */
	public static void updateBricks(BreakoutBrick[] brickArray, int screenWidth, int screenHeight) {
		for (BreakoutBrick brick : brickArray) {
			try {
				double relativeBrickX = brick.getX() / screenWidth;
				double relativeBrickY = brick.getY() / screenHeight;
				LighthouseView.setBrick(relativeBrickX, relativeBrickY);
			} catch (Exception e) {
				System.out.println("Could not show brick on lighthouse");
			}
		}
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
	public static void setBallPosition(double relativeX, double relativeY) throws IllegalArgumentException {
		removeBall();

		// Convert from relative position to window position
		int newBallX = (int) (27 * relativeX);
		int newBallY = (int) (13 * relativeY);

		// exception handling
		if (newBallX < 0 || newBallX + BALL_LENGTH >= WINDOWS_PER_FLOOR) {
			throw new IllegalArgumentException("X-Coordinate of the ball out of range.");
		}
		if (newBallY < 0 || newBallY + BALL_HEIGHT >= FLOORS) {
			throw new IllegalArgumentException("Y-Coordinate of the ball out of range.");
		}
		
		// check if the ball did move at all
		if (newBallX == ballX && newBallY == ballY) {
			System.out.println("Lighthouse updated aborted because nothing changed");
			return;
		} else {
			ballX = newBallX;
			ballY = newBallY;
		}

		// insert ball in array
		for (int i = 0; i < BALL_LENGTH; i++) {
			int index = indexInDataArray(ballX + i, ballY, Color.RED);
			data[index] = (byte) 255;
		}
		updateLighthouseView();
	}

	/**
	 * Sets the position of the paddle at a certain position in the data array. The
	 * paddle X-position shouldn't be higher/smaller than the amount of windows per
	 * floor. The Y-position of the paddle should be in the lowest row of windows.
	 * 
	 * @param relativePaddleWidth
	 * 
	 * @param paddleX
	 *            The X-position of the paddle in the game.
	 */
	public static void setPaddlePosition(double relativeX, double relativePaddleWidth) throws IllegalArgumentException {
		removePaddle();
		// System.out.println(getPaddleXPosition());
		// System.out.println(getPaddleYPosition());

		// Convert from relative position to window position
		paddleX = (int) (27 * relativeX);
		paddleWidth = (int) (27 * relativePaddleWidth);

		// exception handling
		if (paddleX < 0 || paddleX + paddleWidth >= WINDOWS_PER_FLOOR) {
			throw new IllegalArgumentException("X-Coordinate of the paddle out of range.");
		}

		// insert paddle in data array
		// implies that the height of the paddle is just 1 window
		// red
		for (int i = 0; i < paddleWidth; i++) {
			int index = indexInDataArray(paddleX + i, PADDLE_Y, Color.RED);
			data[index] = (byte) 255;
		}

		// green
		for (int i = 0; i < paddleWidth; i++) {
			int index = indexInDataArray(paddleX + i, PADDLE_Y, Color.GREEN);
			data[index] = (byte) 100;
		}

		// blue
		for (int i = 0; i < paddleWidth; i++) {
			int index = indexInDataArray(paddleX + i, PADDLE_Y, Color.BLUE);
			data[index] = (byte) 200;
		}
		updateLighthouseView();
	}

	/**
	 * Sets all windows of the lighthouse dark.
	 */
	public static void setAllDark() {
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
	private static void setWindowDark(int windowX, int windowY) throws IllegalArgumentException {
		// exception handling
		if (windowX < 0 || windowX >= WINDOWS_PER_FLOOR) {
			throw new IllegalArgumentException("X-Coordinate of the window out of range.");
		}
		if (windowY < 0 || windowY >= FLOORS) {
			throw new IllegalArgumentException("Y-Coordinate of the window out of range.");
		}

		// compute index in array
		int index = indexInDataArray(windowX, windowY);

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

	/**
	 * Deletes the ball.
	 */
	private static void removeBall() {
		for (int h = 0; h <= BALL_HEIGHT; h++) {
			for (int i = 0; i <= BALL_LENGTH; i++) {
				try {
					setWindowDark(getBallXPosition() + i, getBallYPosition() + h);
				} catch (Exception e) {
				}
			}
		}
		updateLighthouseView();
	}

	/**
	 * Removes the specified brick.
	 * 
	 * @param relativeBrickX
	 *            The relative x-coordinate of the brick.
	 * @param relativeBrickY
	 *            The relative y-coordinate of the brick.
	 */
	public static void removeBrick(double relativeBrickX, double relativeBrickY) {
		// compute the windows on the lighthouse the brick is blocking
		int brickX = (int) (relativeBrickX * WINDOWS_PER_FLOOR);
		int brickY = (int) (relativeBrickY * FLOORS);

		for (int y = 0; y <= BRICK_HEIGHT; y++) {
			for (int x = 0; x <= BRICK_LENGTH; x++) {
				setWindowDark(brickX + x, brickY + y);
			}
		}
		updateLighthouseView();
	}

	/**
	 * Deletes the paddle.
	 */
	private static void removePaddle() {
		for (int i = 0; i < WINDOWS_PER_FLOOR; i++) {
			setWindowDark(i, FLOORS - 1);
		}
		updateLighthouseView();
	}

	/**
	 * Computes the index of the array representing the red-value of the given
	 * window.
	 * 
	 * @param windowX
	 *            The column in which the window is on the lighthouse.
	 * @param windowY
	 *            The floor in which the window is on the lighthouse.
	 * @return The first index in the {@code data}-array representing the given
	 *         window, means the red-value.
	 */
	private static int indexInDataArray(int windowX, int windowY) {
		return (int) ((windowY * WINDOWS_PER_FLOOR + windowX) * RGB);
	}

	/**
	 * Computes the index of the array representing the given color in the given
	 * window.
	 * 
	 * @param windowX
	 *            The column in which the window is on the lighthouse.
	 * @param windowY
	 *            The floor in which the window is on the lighthouse.
	 * @param color
	 *            The color which the index is wanted of.
	 * @return The index of the {@code data}-array representing the given window and
	 *         the given color.
	 */
	private static int indexInDataArray(int windowX, int windowY, Color color) {
		// error handling
		assert windowX <= WINDOWS_PER_FLOOR : "It should be: 1 <= windowX <= " + WINDOWS_PER_FLOOR + " but was " + windowX;
		assert windowY <= FLOORS : "It should be: 1 <= windowY <= " + FLOORS + " but was " + windowY;
		assert color.equals(Color.RED) || color.equals(Color.GREEN) || color.equals(Color.BLUE) : "Color is not allowed, see javaDoc";
		
		// compute index for red value for the given window
		int index = (int) ((windowY * WINDOWS_PER_FLOOR + windowX) * RGB);
		
		// return the index or count further on for other colors
		if (color.equals(Color.RED)) {			
			return index;
		} else if (color.equals(Color.GREEN)) {
			// for green value
			return (index+1);
			
		} else if (color.equals(Color.BLUE)) {
			// for blue value
			return (index+2);
		} else {
			return -1;
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

	/**
	 * Get the x-position of the ball.
	 * 
	 * @return ballX.
	 */
	private static int getBallXPosition() {
		return ballX;
	}

	/**
	 * Get the y-position of the ball.
	 * 
	 * @return ballY.
	 */
	private static int getBallYPosition() {
		return ballY;
	}

	/**
	 * Get the y-position of the paddle.
	 * 
	 * @return paddleY.
	 */
	@SuppressWarnings("unused")
	private static int getPaddleYPosition() {
		return PADDLE_Y;
	}

	/**
	 * Get the x-position of the paddle.
	 * 
	 * @return paddleX.
	 */
	@SuppressWarnings("unused")
	private static int getPaddleXPosition() {
		return paddleX;
	}
}
