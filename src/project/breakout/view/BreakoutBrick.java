package project.breakout.view;

import acm.graphics.GRect;

@SuppressWarnings("serial")

/**
 * The class that constructs a new brick for the game.
 */
public class BreakoutBrick extends GRect {
	/**
	 * Constructs a new brick with a specific size.
	 * 
	 * @param width
	 *            the width of the brick.
	 * @param height
	 *            the height of the brick.
	 */
	public BreakoutBrick(int width, int height) {
		super(width, height);
	}

	public BreakoutBrick() {
		super(0,0);
	}

}
