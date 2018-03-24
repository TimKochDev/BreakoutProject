package project.breakout.model;

import project.breakout.controller.CollisionWith;
import project.breakout.view.LighthouseView;

public class BallModel implements CollisionListener {
	private int ballRadius = 3;
	private double ballX, ballY;
	private int ballDirection = 320;
	private static int pixelsPerSecond = 200;

	// --------Constructors-----------------
	public BallModel() {

	}

	public BallModel(int ballRadius) {
		this(0, 0, ballRadius);
	}

	public BallModel(double ballX, double ballY, int ballRadius) {
		this.ballX = ballX;
		this.ballY = ballY;
		this.ballRadius = ballRadius;
	}

	// ----move methods--------------
	/**
	 * Moves the ball from the last position about the given pixels.
	 * 
	 * @param xDirection
	 *            The distance to move in xDirection in pixels.
	 * @param yDirection
	 *            The distance to move in yDirection in pixels.
	 */
	public void move(double xDirection, double yDirection) {
		ballX += xDirection;
		ballY += yDirection;
	}
	
	/**
	 * Called by the timer. Updates the ball position depending on { @code
	 * pixelsPerSecond} and the time gone by since the last frame.
	 */
	public void updatePosition(double frameTime) {
		// move ball in last known direction
		double xMovedBy = pixelsPerSecond * frameTime * Math.sin(Math.toRadians(ballDirection));
		double yMovedBy = -pixelsPerSecond * frameTime * Math.cos(Math.toRadians(ballDirection));
		this.move(xMovedBy, yMovedBy);

		// The distance the ball moved is a^2 + b^2 = c^2
		double ballMoveDistance = Math.sqrt(Math.pow(xMovedBy, 2) + Math.pow(yMovedBy, 2));
		double pixelsPerFrametime = pixelsPerSecond * frameTime;
		assert ballMoveDistance <= pixelsPerFrametime + 0.01 : "Ball moves faster than pixelsPerSecond allows to!";
	}

	// ---------Collision handling--------------------

	public void collisionEvent(CollisionWith lastCollisionWith) {
		// compute new direction of the ball
		ballDirection = directionAfterCollision(lastCollisionWith);

		// clear up the ball direction although it works with directions > 360 and < 0.
		ballDirection = (ballDirection > 360) ? ballDirection - 360 : ballDirection;
		ballDirection = (ballDirection < 0) ? ballDirection + 360 : ballDirection;
	}

	/**
	 * Changes the direction of the ball after a collision with the paddle, upper,
	 * left or right wall. When the ball hits the bottom wall the game restarts.
	 * 
	 * @return the new direction of the ball.
	 */
	private int directionAfterCollision(CollisionWith lastCollisionWith) {
		switch (lastCollisionWith) {
		case LEFTWALL:
		case RIGHTWALL:
		case BRICK_Y_AXIS:
			return 360 - ballDirection;
		case UPPERWALL:
		case BRICK_X_AXIS:
			return 180 - ballDirection;
		case BOTTOMWALL:
			return ballDirection;
		case PADDLE:
			return directionAfterPaddleCollision();
		default:
			return ballDirection;
		}
	}

	/**
	 * Changes the direction of the ball after it hit the paddle. The new ball
	 * direction depends on where the ball hits the paddle.
	 * 
	 * @return ballDirection, the new direction of the ball.
	 */
	private int directionAfterPaddleCollision() {
		double paddleHalfX = BreakoutModel.getPaddleX() + BreakoutModel.getPaddleWidth() / 2;
		double deviationFromPaddleMiddle = (ballX - paddleHalfX) / (BreakoutModel.getPaddleWidth() / 2);

		// update ball direction with normal collision and make it depend on the
		// collision point
		ballDirection = 180 - ballDirection;
		ballDirection += deviationFromPaddleMiddle * 80;

		// clear up the ball direction to perform the next step properly
		ballDirection = (ballDirection > 360) ? ballDirection - 360 : ballDirection;
		ballDirection = (ballDirection < 0) ? ballDirection + 360 : ballDirection;

		// specify the angles in which the ball is allowed to bounce away
		int maxRightAngle = 70;
		int minLeftAngle = 290;

		// make sure that the ball jumps upwards after hitting the paddle
		ballDirection = (ballDirection >= maxRightAngle && ballDirection < 180) ? maxRightAngle : ballDirection;
		ballDirection = (ballDirection <= minLeftAngle && ballDirection >= 180) ? minLeftAngle : ballDirection;

		// assertions
		boolean ballDirection1 = ballDirection >= 0 && ballDirection <= maxRightAngle;
		boolean ballDirection2 = ballDirection <= 360 && ballDirection >= minLeftAngle;
		assert ballDirection1 || ballDirection2 : "BallDirection out of specified bounds. Was " + ballDirection;

		return ballDirection;
	}

	// ---------Getter and Setter --------------
	/**
	 * @return the ballRadius
	 */
	public int getRadius() {
		return ballRadius;
	}

	/**
	 * @param ballRadius
	 *            the ballRadius to set
	 */
	public void setRadius(int ballRadius) {
		this.ballRadius = ballRadius;
	}

	/**
	 * @return the ballX
	 */
	public double getX() {
		return ballX;
	}

	/**
	 * @param ballX
	 *            the ballX to set
	 */
	public void setX(double ballX) {
		this.ballX = ballX;
	}

	/**
	 * @return the ballY
	 */
	public double getY() {
		return ballY;
	}

	/**
	 * @param ballY
	 *            the ballY to set
	 */
	public void setY(double ballY) {
		this.ballY = ballY;
	}

	/**
	 * @return the ballDirection
	 */
	public int getDirection() {
		return ballDirection;
	}

	/**
	 * @param ballDirection
	 *            the ballDirection to set
	 */
	public void setDirection(int ballDirection) {
		this.ballDirection = ballDirection;
	}

}
