package project.breakout.model;

import java.awt.Point;

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
 * test
 */
public class BreakoutModel extends GraphicsProgram {
	private static int paddleWidth = 100;
	private static int paddleHeight = 10;
	private static int paddleX, paddleY;

	private int ballRadius = 3;
	private double ballX, ballY;
	private static int ballDirection = 290;

	private static int brickWidth = 40;
	private static int brickHeight = 15;
	BreakoutBrick[] brickArray;

	private static int framesPerSecond = 60;
	private static int pixelsPerSecond = 200;

	private static BreakoutView view;
	private static BreakoutTimer timer;
	private static CollisionController collisionControl;
	private static BreakoutController controller;
	Thread timerThread;

	private static boolean gameStarted = false;

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
		view.setBallsPosition(ballX, ballY);
		view.setBallsRadius(ballRadius);

		// init bricks
		brickArray = new BreakoutBrick[3];
		for (int i = 0; i < brickArray.length; i++) {
			brickArray[i] = new BreakoutBrick(brickWidth, brickHeight);
			brickArray[i].setLocation(10 + i * (brickWidth + 10), 50);
		}
		view.updateBricks(brickArray);

		// init view
		removeAll();
		add(view, 0, 0);
	}

	private void initLighthouse() {
		LighthouseView.connectToLighthouse();
		LighthouseView.setBallsPosition(0, 0);
		view.setInfoText("connected");
	}

	// -------------methods for controller-----------
	/**
	 * This method is called by the controller when the mouse was moved.
	 * 
	 * @param point
	 */
	public void updateMouseLocation(Point point) {
		int mouseX = (int) point.getX();
		int paddleHalf = paddleWidth / 2;
		if (mouseX > paddleHalf && mouseX < view.getWidth() - paddleHalf) {
			paddleX = mouseX - paddleHalf;
			view.setPaddleLocation(paddleX, paddleY);

			// move ball over paddle if game not started yet
			if (!gameStarted) {
				ballX = mouseX;
				ballY = paddleY - 3 * ballRadius;
				view.setBallsPosition(ballX, ballY);
			}
		}
	}

	/**
	 * This method is called by the controller when the window was resized by the
	 * user.
	 * 
	 * @param width
	 * @param height
	 */
	public void resizedView(int width, int height) {
		paddleY = height - paddleHeight - 2;
		view.setSize(width, height);
		view.setPaddleLocation(paddleX, paddleY);
	}

	/**
	 * This method is called by the controller when the user starts the game.
	 *
	 * @return {@code true} if game was started successfully, {@code false} if game
	 *         is running yet.
	 */
	public boolean startGame() {
		if (!gameStarted) {
			timer = new BreakoutTimer(this, framesPerSecond);
			timerThread = new Thread(timer);
			timerThread.start();
			gameStarted = true;
			return true;
		} else {
			return false;
		}
	}

	// --------------------game control methods----------------------------

	/**
	 * Called by the timer. Updates the ball position depending on
	 * {@code pixelsPerSecond} and {@code framesPerSecond}.
	 */
	public void updateBallsPosition(double frameTime) {
		// move ball in last known direction
		double xMovedBy = pixelsPerSecond * frameTime * Math.sin(Math.toRadians(ballDirection));
		double yMovedBy = -pixelsPerSecond * frameTime * Math.cos(Math.toRadians(ballDirection));
		ballX += xMovedBy;
		ballY += yMovedBy;

		// if there's a collision in the model NOW
		if (collisionControl.isWallCollisionInModel(this) || collisionControl.isBrickCollisionInModel(this)
				|| collisionControl.isPaddleCollisionInModel(this)) {
			ballDirection = directionAfterCollision();

			// clear up the ball direction although it works with directions > 360 and < 0.
			ballDirection = (ballDirection > 360) ? ballDirection - 360 : ballDirection;
			ballDirection = (ballDirection < 0) ? ballDirection + 360 : ballDirection;

			// go back to non collision state
			ballX -= xMovedBy;
			ballY -= yMovedBy;

			// compute new movement with now updated ballDirection
			xMovedBy = pixelsPerSecond * frameTime * Math.sin(Math.toRadians(ballDirection));
			yMovedBy = -pixelsPerSecond * frameTime * Math.cos(Math.toRadians(ballDirection));

			// update ball's position
			ballX += xMovedBy;
			ballY += yMovedBy;

			// show infoText
			assert collisionControl
					.getLastCollisionWith() != null : "lastCollisionWith is null and should be displayed -> NullPointerException";
			// view.setInfoText("Last Thing collided: " + collisionControl.getLastCollisionWith().toString());
		}

		view.setBallsPosition(ballX, ballY);
		// view.setInfoText("Balldirection: " + ballDirection);

	}

	/**
	 * @return The direction of the ball after a collision.
	 */
	private int directionAfterCollision() {
		CollisionWith lastCollisionWith = collisionControl.getLastCollisionWith();
		switch (lastCollisionWith) {
		case LEFTWALL:
		case RIGHTWALL:
		case BRICK_Y_AXIS:
			return 360 - ballDirection;
		case UPPERWALL:
		case BRICK_X_AXIS:
			return 180 - ballDirection;
		case BOTTOMWALL:
			restartGame();
			return ballDirection;
		case PADDLE:
			return directionAfterPaddleCollision();
		default:
			return ballDirection;
		}
	}

	/**
	 * @return The direction of the ball after it hit the paddle.
	 */
	private int directionAfterPaddleCollision() {
		double paddleHalfX = paddleX + paddleWidth / 2;
		double deviationFromPaddleMiddle = (ballX - paddleHalfX) / (paddleWidth / 2);

		// update ball direction with normal collision and make it depend on the
		// collision point
		ballDirection = 180 - ballDirection;
		ballDirection += deviationFromPaddleMiddle * 80;

		// clear up the ball direction to perform the next step properly
		ballDirection = (ballDirection > 360) ? ballDirection - 360 : ballDirection;
		ballDirection = (ballDirection < 0) ? ballDirection + 360 : ballDirection;

		// make sure that the ball jumps upwards after hitting the paddle
		ballDirection = (ballDirection > 90 && ballDirection < 180) ? 80 : ballDirection;
		ballDirection = (ballDirection < 270 && ballDirection >= 180) ? 280 : ballDirection;

		return ballDirection;
	}

	// ----------------game states methods------------------
	/**
	 * Sets the game to the beginning state.
	 */
	private void restartGame() {
		gameStarted = false;
		
		// stop timer
		timer.go = false;
		try {
			timerThread.join();
		} catch (InterruptedException e) {
		}

		// Re-init ball
		ballX = paddleX + paddleWidth / 2;
		ballY = paddleY - 3 * ballRadius;
		view.setBallsPosition(ballX, ballY);
		view.setBallsRadius(ballRadius);

		ballDirection = RandomGenerator.getInstance().nextInt(10) * 100 - 50;
	}

	public void pauseGame() {
		timerThread.interrupt();
	}

	public void continueGame() {
		timerThread.start();
	}

	// ---------Getters-------------------------
	/**
	 * @return the ballRadius
	 */
	public int getBallRadius() {
		return ballRadius;
	}

	/**
	 * @return the ballX
	 */
	public double getBallX() {
		return ballX;
	}

	/**
	 * @return the ballY
	 */
	public double getBallY() {
		return ballY;
	}

	/**
	 * @return the brickArray
	 */
	public BreakoutBrick[] getBrickArray() {
		return brickArray;
	}

	/**
	 * @return the paddleWidth
	 */
	public static int getPaddleWidth() {
		return paddleWidth;
	}

	/**
	 * @return the paddleHeight
	 */
	public static int getPaddleHeight() {
		return paddleHeight;
	}

	/**
	 * @return the paddleX
	 */
	public static int getPaddleX() {
		return paddleX;
	}

	/**
	 * @return the paddleY
	 */
	public static int getPaddleY() {
		return paddleY;
	}
}
