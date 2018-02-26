package project.breakout.model;

import java.awt.Color;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import project.breakout.view.BreakoutBrick;

/**
 * 
 *
 */
public class BricksConfig {
	private static int brickWidth = 40;
	private static int brickHeight = 15;
	private static final String BUNDLE_NAME = "project.breakout.model.bricksConfig"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	// private BricksConfig() {
	// }

	/**
	 * Returns a simple brick array for testing.
	 * 
	 * @return brick array with three bricks in a line.
	 */
	public static BreakoutBrick[] getTestBrickArray() {
		BreakoutBrick[] brickArray = new BreakoutBrick[3];
		for (int i = 0; i < brickArray.length; i++) {
			brickArray[i] = new BreakoutBrick(brickWidth, brickHeight);
			brickArray[i].setLocation(10 + i * (brickWidth + 10), 50);
		}
		return brickArray;
	}

	/**
	 * Returns a brickArray specified by the {@code levelNumber}.
	 * 
	 * @param levelNumber
	 *            Specifies which brick configuration should be returned.
	 * @return A brickArray representing the level.
	 */
	public static BreakoutBrick[] getBrickArray(int levelNumber) {
		BreakoutBrick[] brickArray = new BreakoutBrick[3];
		for (int i = 0; i < brickArray.length; i++) {
			brickArray[i] = new BreakoutBrick(brickWidth, brickHeight);
			brickArray[i].setLocation(10 + i * (brickWidth + 10), 50);
			brickArray[i].setFillColor(Color.RED);
		}
		return brickArray;

		// TODO Use the .properties file to make this class useful!
	}

	private static BreakoutBrick getBrickFromFile(int brickNumber) {
		// if brick doesn't exist return null
		if (!keyExists(brickNumber + "x")) {
			return null;
		}

		BreakoutBrick brick = new BreakoutBrick();

		// TODO implement brick request

		return brick;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	private static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Checks whether key exists in bricksConfig.properties.
	 * 
	 * @param key
	 *            The key which shall be proved.
	 * @return {@code true} if key exists, {@code false} if not.
	 */
	private static boolean keyExists(String key) {
		try {
			String test = RESOURCE_BUNDLE.getString(key);
			return true;
		} catch (MissingResourceException e) {
			return false;
		}
	}
}
