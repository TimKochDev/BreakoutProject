package project.breakout.view;

import acm.graphics.GRect;
import javafx.scene.paint.Color;

@SuppressWarnings("serial")

/**
 * The class that constructs a new brick for the game.
 */
public class BreakoutBrick extends GRect {
	private BrickType brickType;

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
		brickType = BrickType.STANDARD;
	}

	/**
	 * Constructs an empty BreakoutBrick with size (0,0)
	 */
	public BreakoutBrick() {
		super(0, 0);
		brickType = BrickType.STANDARD;
	}

	/**
	 * @return the brickType of the {@code BreakoutBrick} as a BrickType-enum.
	 */
	public BrickType getBrickType() {
		return brickType;
	}

	/**
	 * @param brickType
	 *            the brickType to set as a BrickType-enum.
	 */
	public void setBrickType(BrickType brickType) {
		this.brickType = brickType;
		
		// set brickColor to violet if it is brickOfLove
		if (brickType == BrickType.BRICK_OF_LOVE) {
			setFillColor(new java.awt.Color((float) Color.VIOLET.getRed(),
                    (float) Color.VIOLET.getGreen(),
                    (float) Color.VIOLET.getBlue(),
                    (float) Color.VIOLET.getOpacity()));
		}
	}
}
