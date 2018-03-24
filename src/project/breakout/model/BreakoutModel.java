package project.breakout.model;

import java.awt.Point;
import java.util.Timer;

import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import project.breakout.controller.BreakoutController;
import project.breakout.controller.BreakoutTimer;
import project.breakout.controller.CollisionController;
import project.breakout.controller.CollisionWith;
import project.breakout.view.BreakoutBrick;
import project.breakout.view.BreakoutView;
import project.breakout.view.LighthouseView;

/**
 * This class represents the main class of the breakout game. It takes a Canvas
 * from the BreakoutView-class and draws it on the drawing area. It is
 * controlled by the controller it initializes.
 * 
 * It can be found on GitHub via https://github.com/TiKo98/BreakoutProject
 */
@SuppressWarnings("serial")
public class BreakoutModel extends GraphicsProgram {
	private static int paddleWidth = 100;
	private static int paddleHeight = 10;
	private static int paddleX, paddleY;

	private static int ballRadius = 3;
	private static double ballX, ballY;
	private static int ballDirection = 320;
	private BallModel ball = new BallModel(3);

	private static BreakoutBrick[] brickArray;

	private static int framesPerSecond = 40;
	private static int pixelsPerSecond = 200;
	private static long lastFrameAtTime;

	private static BreakoutView view;
	private static CollisionController collisionControl;
	private Timer timer;
	@SuppressWarnings("unused")
	private static BreakoutController controller;
	private Thread timerThread;

	private static boolean lighthouseEnabled = false;
	private static boolean gameStarted = false;
	private static boolean gamePaused = false;
	private static int currentLevel = 0;

	/**
	 * RUN METHOD - HERE STARTS EVERYTHING!!!
	 */
	@Override
	public void run() {
		initView();
		initController();
		initLighthouse();

	}

	// ------------------initializing methods----------------------------

	/**
	 * Initializes the controller connected with this class.
	 */
	private void initController() {
		collisionControl = new CollisionController();
		controller = new BreakoutController(this, view);
	}

	/**
	 * Initializes the canvas which represents the model of the game in the current
	 * class.
	 */
	private void initView() {
		view = new BreakoutView(getWidth(), getHeight());

		// init paddle
		paddleX = (getWidth() - paddleWidth) / 2;
		paddleY = getHeight() - paddleHeight - 2;
		view.setPaddleLocation(paddleX, paddleY);
		view.setPaddleSize(paddleWidth, paddleHeight);

		// init ball
		ballX = paddleX + paddleWidth / 2;
		ballY = paddleY - 3 * ballRadius;
		ball.setX(paddleX + paddleWidth / 2);
		ball.setY(paddleY - 3 * ballRadius);
		view.setBallsPosition(ballX, ballY);
		view.setBallsRadius(ballRadius);

		// init bricks for level
		initBricksForLevel(currentLevel);

		// init view
		removeAll();
		add(view, 0, 0);
	}

	/**
	 * Initializes the brick array with the standard configuration.
	 */
	private void initBricks() {
		// bricks for view
		brickArray = BricksConfig.getTestBrickArray();
		assert brickArray != null : "Test-brickarray is null!";
		view.updateBricks(brickArray);
	}

	/**
	 * Initializes the brick array with the configuration for the
	 * {@code levelNumber}.
	 */
	private void initBricksForLevel(int levelNumber) {
		// init bricks in view
		brickArray = BricksConfig.getBrickArray(levelNumber);
		if (brickArray != null) {
			view.updateBricks(brickArray);
		}

		// init bricks on lighthouse
		if (LighthouseView.isConnected()) {
			for (BreakoutBrick brick : brickArray) {
				double relativeBrickX = brick.getX() / getWidth();
				double relativeBrickY = brick.getY() / getHeight();
				LighthouseView.setBrick(relativeBrickX, relativeBrickY);
			}
		}
	}

	/**
	 * Initializes the connection to the lighthouse.
	 */
	private void initLighthouse() {
		LighthouseView.connectToLighthouse();

		while (!LighthouseView.isConnected()) {
			System.out.println("wait for connection");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			LighthouseView.setPaddlePosition(0.5, 0.1);

			// init bricks on lighthouse
			LighthouseView.updateBricks(brickArray, getWidth(), getHeight());

		} catch (Exception e) {
			System.out.println("initital push to LighthouseView didn't work");
		}

		if (LighthouseView.isConnected()) {
			System.out.println("connected");
		}
	}

	// -------------methods for controller-----------
	/**
	 * This method is called by the controller when the mouse was moved.
	 * 
	 * @param point
	 *            The point where the mouse pointer is.
	 */
	public void updateMouseLocation(Point point) {
		int mouseX = (int) point.getX();
		int paddleHalf = paddleWidth / 2;

		// Check if paddle would be still in the view after moving it
		if (mouseX > paddleHalf && mouseX < view.getWidth() - paddleHalf) {
			paddleX = mouseX - paddleHalf;
			view.setPaddleLocation(paddleX, paddleY);

			// move paddle in LighthouseView
			double relativeX = (double) paddleX / getWidth();
			double relativePaddleWidth = (double) paddleWidth / getWidth();
			try {
				LighthouseView.setPaddlePosition(relativeX, relativePaddleWidth);
			} catch (Exception e) {
			}

			// move ball over paddle if game not started yet
			if (!gameStarted) {
				ballX = mouseX;
				ballY = paddleY - 3 * ballRadius;
				ball.setX(mouseX);
				ball.setY(paddleY - 3 * ballRadius);
				view.setBallsPosition(ballX, ballY);
			}
		}
	}

	/**
	 * This method is called by the controller when the window was resized by the
	 * user.
	 * 
	 * @param width
	 *            the new width of the window.
	 * @param height
	 *            the new height of the window.
	 */
	public void resizedView(int width, int height) {
		paddleY = height - paddleHeight - 2;
		view.setSize(width, height);
		view.setPaddleLocation(paddleX, paddleY);
	}

	// --------------------game control methods----------------------------

	/**
	 * Called by the timer. Updates the ball position depending on { @code
	 * pixelsPerSecond} and the time gone by since the last frame.
	 */
	public void updateBallsPosition() {
		// compute time since the last frame was created
		double frameTime = (double) (System.currentTimeMillis() - lastFrameAtTime);
		// view.setInfoText(String.valueOf(frameTime));
		frameTime /= 1000.0;
		lastFrameAtTime = System.currentTimeMillis();	
		

		// TODO comment out when not debugging
		// frameTime = 0.3;

		ball.updatePosition(frameTime);
		

		// apply changes
		view.updateBallsPosition(ball);
		view.setInfoText("Balldirection: " + ball.getDirection());

		// compute relative position for lighthouse use
		double relativeX = (ballX / getWidth());
		double relativeY = (ballY / getHeight());
		try {
			LighthouseView.setBallPosition(relativeX, relativeY);
			// System.out.println("Set ball to window " + relativeX + "/" + relativeY);
		} catch (Exception e) {
			System.out.println("failes to set ball to " + relativeX + "/" + relativeY);
			System.out.println(e.getMessage());
		}
	}

	

	/**
	 * Deletes a brick from the brickArray and updates the view.
	 * 
	 * @param lastBrickCollided
	 *            The {@code BreakoutBrick} which collided with the ball.
	 */
	public void deleteBrickAfterCollision(BreakoutBrick lastBrickCollided) {
		for (int i = 0; i < brickArray.length; i++) {
			if (brickArray[i] != null && brickArray[i].equals(lastBrickCollided)) {
				brickArray[i] = null;
			}
		}
		view.removeBrick(lastBrickCollided);

		// remove brick on Lighthouse
		double relativeBrickX = lastBrickCollided.getX() / getWidth();
		double relativeBrickY = lastBrickCollided.getY() / getHeight();
		LighthouseView.removeBrick(relativeBrickX, relativeBrickY);
	}

	// ----------------game states methods------------------

	/**
	 * Starting a new Game. This method is called by the controller when the user
	 * starts the game.
	 *
	 * @return {@code true} if game was started successfully, {@code false} if the
	 *         game is already running.
	 */
	public boolean startGame() {
		if (!gameStarted) {

			// set up a new timer which updates the ball's position depending on the frame
			// rate
			timer = new Timer();
			BreakoutTimer timerTask = new BreakoutTimer(this);
			lastFrameAtTime = System.currentTimeMillis();
			long frameTime = 1000 / framesPerSecond;
			timer.schedule(timerTask, 0, frameTime);

			gameStarted = true;
			view.levelStarted();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the game to the beginning state.
	 */
	private void restartGame() {
		gameStarted = false;

		// stop timer
		timer.cancel();

		// Re-init view and controllers
		initView();
		initController();

		ballDirection = RandomGenerator.getInstance().nextInt(10) * 10 - 50;
	}

	/**
	 * Handles when a level is completed by the player.
	 */
	public void levelDone() {
		view.levelDone();
		gameStarted = false;
		timer.cancel();

		// start next level or begin again at the first
		if (BricksConfig.getBrickArray(currentLevel + 1) != null) {
			currentLevel++;
		} else {
			currentLevel = 0;
		}

		brickArray = BricksConfig.getBrickArray(currentLevel);
		view.updateBricks(brickArray);
		LighthouseView.setAllDark();
		LighthouseView.updateBricks(brickArray, getWidth(), getHeight());
	}

	/**
	 * Pauses the game and the timer.
	 */
	public void pauseGame() {
		if (timer != null) {
			timer.cancel();
		}
		gamePaused = true;
	}

	/**
	 * Continues the game and the timer starts running.
	 */
	public void continueGame() {
		// set up a new timer which updates the ball's position depending on the frame
		// rate
		timer = new Timer();
		BreakoutTimer timerTask = new BreakoutTimer(this);
		lastFrameAtTime = System.currentTimeMillis();
		long frameTime = 1000 / framesPerSecond;
		timer.schedule(timerTask, 0, frameTime);
		gamePaused = false;
		gameStarted = true;
	}

	// ---------Getters-------------------------
	/**
	 * Gets the ball radius.
	 * 
	 * @return ballRadius, the radius of the ball.
	 */
	public int getBallRadius() {
		return ballRadius;
	}

	/**
	 * Gets the ball's x-Position.
	 * 
	 * @return ballX, the x-Position of the ball.
	 */
	public double getBallX() {
		return ballX;
	}

	/**
	 * Gets the ball's y-Position.
	 * 
	 * @return ballY, the y-Position of the ball.
	 */
	public double getBallY() {
		return ballY;
	}

	/**
	 * Gets the array where the bricks are saved in.
	 * 
	 * @return brickArray
	 */
	public BreakoutBrick[] getBrickArray() {
		return brickArray;
	}

	/**
	 * Gets the width of the paddle.
	 * 
	 * @return paddleHeight the width of the paddle.
	 */
	public static int getPaddleWidth() {
		return paddleWidth;
	}

	/**
	 * Gets the height of the paddle.
	 * 
	 * @return paddleHeight the height of the paddle.
	 */
	public static int getPaddleHeight() {
		return paddleHeight;
	}

	/**
	 * Gets the paddle's x-Position.
	 * 
	 * @return paddleX the paddle's x-Position.
	 */
	public static int getPaddleX() {
		return paddleX;
	}

	/**
	 * Gets the paddle's y-Position.
	 * 
	 * @return paddleY the paddle's y-Position.
	 */
	public static int getPaddleY() {
		return paddleY;
	}

	/**
	 * Gets if the game is paused or not.
	 * 
	 * @return {@code true} if game is paused, {@code false} if not.
	 */
	public boolean isGamePaused() {
		return gamePaused;
	}

	/**
	 * @return the lighthouseEnabled
	 */
	public boolean isLighthouseEnabled() {
		return lighthouseEnabled;
	}

	/**
	 * @param lighthouseEnabled
	 *            the lighthouseEnabled to set
	 */
	public void setLighthouseEnabled(boolean lighthouseEnabled) {
		BreakoutModel.lighthouseEnabled = lighthouseEnabled;

		if (lighthouseEnabled) {
			view.setInfoText("Connection to lighthouse started");
			view.showInfoText(true);
			if (!LighthouseView.isConnected()) {
				initLighthouse();
			}
		}
	}
}
