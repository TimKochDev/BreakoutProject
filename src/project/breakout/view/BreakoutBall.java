package project.breakout.view;

import acm.graphics.GOval;

@SuppressWarnings("serial")
/**
 * The class that constructs a new ball for the game.
 */
public class BreakoutBall extends GOval {
	/**
	 * Constructs a new ball with a specific size.
	 * 
	 * @param radius
	 *            the radius of the ball.
	 */
	public BreakoutBall(int radius) {
		super(radius, radius);
	}
}
