package project.breakout.model;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import jdk.nashorn.internal.ir.TernaryNode;
import project.breakout.view.BreakoutBrick;
import project.breakout.view.BrickType;

/**
 * This class specifies the access to the bricksConfig.properties. It is used to
 * get a brickArray for a specific level. The structure of the properties should
 * look like the following: 1x = (x-coordinate in pixels) 1y = (y-coordinate in
 * pixels) 1width = (brick width in pixels) 1height = (brick height in pixels)
 * 1color = (name of color as string, eg "yellow") 1type = (String with value of
 * BrickType-enum, eg "STANDARD", "SPARKLE_BRICK", "BRICK_OF_LOVE")
 * 
 * 2x ... 2y...
 */
public class BricksConfig {
	private static int brickWidth = 40;
	private static int brickHeight = 15;
	private static String BUNDLE_NAME = "project.breakout.model.bricksConfig"; //$NON-NLS-1$

	private static final String XCOORD = "x";
	private static final String YCOORD = "y";
	private static final String BRICKWIDTH = "width";
	private static final String BRICKHEIGHT = "height";
	private static final String BRICKCOLOR = "color";
	private static final String BRICKTYPE = "type";

	private static ResourceBundle RESOURCE_BUNDLE;

	private BricksConfig() {
	}

	/**
	 * Returns a simple brick array for testing.
	 * 
	 * @return brick array with three bricks in a line.
	 */
	public static BreakoutBrick[] getTestBrickArray() {
		return getBrickArray(0);
	}

	/**
	 * Returns a brickArray specified by the {@code levelNumber}.
	 * 
	 * @param levelNumber
	 *            Specifies which brick configuration should be returned.
	 * @return A brickArray representing the level.
	 */
	public static BreakoutBrick[] getBrickArray(int levelNumber) {
		// Set resource file to level specification
		try {
			BUNDLE_NAME = "project.breakout.model.level" + levelNumber;
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		} catch (Exception e) {
			System.out.println("could not load brick config for level " + levelNumber);
			return null;
		}

		// create arrayList with all breakoutBricks in the configuration file
		ArrayList<BreakoutBrick> brickList = new ArrayList<>();
		for (int i = 1; brickExistsInFile(i); i++) {
			brickList.add(getBrickFromFile(i));
		}

		// clear up the list in order to remove null elements
		brickList.removeAll(Collections.singleton(null));
		assert brickList.indexOf(null) == -1;

		// return brickList as array of BreakoutBricks
		BreakoutBrick[] brickArray = new BreakoutBrick[brickList.size()];
		brickArray = brickList.toArray(brickArray);
		return brickArray;
	}

	private static BreakoutBrick getBrickFromFile(int brickNumber) {
		// if brick doesn't exist return null
		if (!brickExistsInFile(brickNumber)) {
			return null;
		}

		BreakoutBrick brick = new BreakoutBrick();

		// get location of the brick
		try {
			double brickX = Double.parseDouble(getString(brickNumber + XCOORD));
			double brickY = Double.parseDouble(getString(brickNumber + YCOORD));
			brick.setLocation(brickX, brickY);
		} catch (Exception e) {
			System.out.println("BrickNr " + brickNumber + "in resource " + BUNDLE_NAME + "has no x- or y-coordinate");
			return null;
		}

		// get size of the brick
		try {
			double brickWidth = Double.parseDouble(getString(brickNumber + BRICKWIDTH));
			double brickHeight = Double.parseDouble(getString(brickNumber + BRICKHEIGHT));
			brick.setSize(brickWidth, brickHeight);
		} catch (Exception e) {
			System.out.println(
					"BrickNr " + brickNumber + "in resource " + BUNDLE_NAME + " was initialized with standard size");
			brick.setSize(brickWidth, brickHeight);
		}

		// get color of the brick
		try {
			String brickColor = getString(brickNumber + BRICKCOLOR);
			Field field = Color.class.getField(brickColor);
			Color color = (Color) field.get(null);
			brick.setFillColor(color);
		} catch (Exception e) {
			System.out.println(
					"BrickNr " + brickNumber + "in resource " + BUNDLE_NAME + " was initialized with standard color");
			brick.setColor(Color.BLACK);
		}

		// get type of brick
		try {
			String brickTypeString = getString(brickNumber + BRICKTYPE);
			BrickType brickTypeEnum = BrickType.valueOf(brickTypeString);
			brick.setBrickType(brickTypeEnum);
		} catch (Exception e) {
		}

		return brick;
	}

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
	private static boolean brickExistsInFile(int brickNumber) {
		try {
			String test = RESOURCE_BUNDLE.getString(brickNumber + "x");
			return true;
		} catch (MissingResourceException e) {
			return false;
		}
	}
}
