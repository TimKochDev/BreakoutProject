package project.breakout.controller;

import project.breakout.model.BreakoutModel;
import project.breakout.view.BreakoutBrick;

import project.breakout.controller.CollisionWith;

public class CollisionController {

	/**
	 * Saves where the last collision of the ball was as an enum of type
	 * {@code CollisionWith}.
	 */
	private CollisionWith lastCollisionWith = CollisionWith.PADDLE;
	@SuppressWarnings("unused")
	private BreakoutBrick lastBrickCollided = new BreakoutBrick(0, 0);

	/**
	 * The distance between the bricks wall to the end of the zone within the brick
	 * which is seen as a collision.
	 */
	double colTolerance;

	public boolean isWallCollisionInModel(BreakoutModel model) {
		double ballX = model.getBallX();
		double ballY = model.getBallY();
		int ballRadius = model.getBallRadius();

		// left wall
		if (ballX <= 0 && lastCollisionWith != CollisionWith.LEFTWALL) {
			lastCollisionWith = CollisionWith.LEFTWALL;
			return true;
		}

		// right wall
		if (ballX + 2 * ballRadius >= model.getWidth() && lastCollisionWith != CollisionWith.RIGHTWALL) {
			lastCollisionWith = CollisionWith.RIGHTWALL;
			return true;
		}

		// upper wall
		if (ballY <= ballRadius && lastCollisionWith != CollisionWith.UPPERWALL) {
			lastCollisionWith = CollisionWith.UPPERWALL;
			return true;
		}

		// bottom wall
		if (ballY + 2 * ballRadius >= model.getHeight() && lastCollisionWith != CollisionWith.BOTTOMWALL) {
			lastCollisionWith = CollisionWith.BOTTOMWALL;
			return true;
		}

		return false;
	}

	/**
	 * Checks if the ball in the model collides with a brick. Sets
	 * {@code lastCollisionWith} and {@code lastBrickCollided} to new values if
	 * there is a collision.
	 * 
	 * @param model
	 *            A reference to the current model of the game.
	 * @return {@code true} if there is a collision with a brick in the model,
	 *         {@code false} if not.
	 */
	public boolean isBrickCollisionInModel(BreakoutModel model) {
		BreakoutBrick[] bricks = model.getBrickArray();
		double ballX = model.getBallX();
		double ballY = model.getBallY();
		int ballRadius = model.getBallRadius();
		double ballMiddleX = ballX + ballRadius;
		double ballMiddleY = ballY + ballRadius;

		// check if lastCollidedBrick is still the last collided THING in the view
		if (lastCollisionWith != CollisionWith.BRICK_X_AXIS && lastCollisionWith != CollisionWith.BRICK_Y_AXIS) {
			lastBrickCollided = new BreakoutBrick(0, 0);
		}

		// iterate over bricks
		for (BreakoutBrick brick : bricks) {
			if (brick != null) {
				// get middle of the brick
				double brickMiddleX = brick.getX() + brick.getWidth() / 2;
				double brickMiddleY = brick.getY() + brick.getHeight() / 2;

				// calculate balls distances to the brick middle
				double ballBrickDistanceX = Math.abs(brickMiddleX - ballMiddleX) - ballRadius;
				double ballBrickDistanceY = Math.abs(brickMiddleY - ballMiddleY) - ballRadius;

				// see distance as percentage of brick size
				double relativeDistanceX = ballBrickDistanceX / (brick.getWidth() / 2);
				double relativeDistanceY = ballBrickDistanceY / (brick.getHeight() / 2);

				assert relativeDistanceX + ballRadius > 0 : "the relative distanceX should be > 0, but was "
						+ relativeDistanceX;
				assert relativeDistanceY + ballRadius > 0 : "the relative distanceY should be > 0, but was "
						+ relativeDistanceY;

				// check if ball hits brick
				if (relativeDistanceX <= 1 && relativeDistanceY <= 1) {
					// brick has hit the brick, collision happened on the side where the relative
					// distance of the ball to the brick middle is minimal.

					lastBrickCollided = brick;
					lastCollisionWith = (relativeDistanceX < relativeDistanceY) ? CollisionWith.BRICK_X_AXIS
							: CollisionWith.BRICK_Y_AXIS;
					model.deleteBrickAfterCollision(lastBrickCollided);
					return true;
				}
			}
		}

		return false;
	}

	public boolean isPaddleCollisionInModel(BreakoutModel model) {
		// get information about the ball
		int ballRadius = model.getBallRadius();
		double ballX = model.getBallX();
		double ballY = model.getBallY();
		double ballMiddleX = ballX + ballRadius;
		double ballMiddleY = ballY + ballRadius;

		// get information about the paddle
		double paddleX = BreakoutModel.getPaddleX();
		double paddleY = BreakoutModel.getPaddleY();
		double paddleMiddleX = paddleX + BreakoutModel.getPaddleWidth() / 2;
		double paddleMiddleY = paddleY + BreakoutModel.getPaddleHeight() / 2;

		// in case the collision is detected multiple times
		if (lastCollisionWith == CollisionWith.PADDLE) {
			return false;
		}

		// calculate balls distances to the paddle middle
		double ballPaddleDistanceX = Math.abs(paddleMiddleX - ballMiddleX) - ballRadius;
		double ballPaddleDistanceY = Math.abs(paddleMiddleY - ballMiddleY) - ballRadius;

		// see distance as percentage of paddle size
		double relativeDistanceX = ballPaddleDistanceX / (BreakoutModel.getPaddleWidth() / 2);
		double relativeDistanceY = ballPaddleDistanceY / (BreakoutModel.getPaddleHeight() / 2);

		assert relativeDistanceX + ballRadius > 0 : "the relative distanceX should be > 0, but was "
				+ relativeDistanceX;
		assert relativeDistanceY + ballRadius > 0 : "the relative distanceY should be > 0, but was "
				+ relativeDistanceY;

		// check if ball hits brick
		if (relativeDistanceX <= 1 && relativeDistanceY <= 1) {
			// brick has hit the brick, collision happened on the side where the relative
			// distance of the ball to the brick middle is minimal.

			lastCollisionWith = CollisionWith.PADDLE;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if all bricks in the game are destroyed by the ball.
	 * 
	 * @param brickArray
	 *            The array of bricks used in the level.
	 * @return {@code true} if there is no brick left on the screen, {@code false}
	 *         if there is at least one.
	 */
	public boolean allBricksDestroyed(BreakoutBrick[] brickArray) {
		for (BreakoutBrick brick : brickArray) {
			if (brick != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the lastCollisionWith
	 */
	public CollisionWith getLastCollisionWith() {
		return lastCollisionWith;
	}
}
