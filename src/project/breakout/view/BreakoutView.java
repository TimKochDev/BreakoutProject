package project.breakout.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.List;

import acm.graphics.GCanvas;
import acm.graphics.GLabel;
import acm.graphics.GRect;

/**
 * This class represents the {@code BreakoutModel} as a game view. It provides a
 * canvas which is drawn by the {@code BreakoutModel} and controlled by the
 * {@code BreakoutController}.
 */
@SuppressWarnings("serial")
public class BreakoutView extends GCanvas {

	private GRect paddleOld = new GRect(0, 0);
	private BreakoutPaddle paddle = new BreakoutPaddle(0, 0);
	private BreakoutBall ball = new BreakoutBall(0);
	private GLabel infoLabel = new GLabel("", 0, 10);
	private GLabel winnerLabel;

	/**
	 * Constructor for the BreakoutView.
	 * 
	 * @param canvasWidth
	 *            The desired width of the BreakoutView canvas.
	 * @param canvasHeight
	 *            The desired height of the BreakoutView canvas.
	 */
	public BreakoutView(int canvasWidth, int canvasHeight) {
		paddle.setColor(Color.red);
		ball.setFillColor(Color.red);
		ball.setFilled(true);
		infoLabel.setVisible(false);

		setSize(canvasWidth, canvasHeight);
		add(paddleOld);
		add(ball);
		add(infoLabel);
		add(paddle);
	}

	// ---------paddle methods--------------------------
	/**
	 * Sets the paddle location in the view.
	 * 
	 * @param xCoord
	 *            the x coordinate of the paddle.
	 * @param yCoord
	 *            the y coordinate of the paddle.
	 */
	public void setPaddleLocation(int xCoord, int yCoord) {
		paddle.setLocation(xCoord, yCoord);
	}

	/**
	 * Sets the paddle size in the view.
	 * 
	 * @param width
	 *            the width of the paddle.
	 * @param height
	 *            the height of the paddle.
	 */
	public void setPaddleSize(int width, int height) {
		paddle.setSize(width, height);
	}

	// ----------ball methods-------------------------
	/**
	 * Sets the ball radius in the view.
	 * 
	 * @param radius
	 *            the radius of the ball.
	 */
	public void setBallsRadius(int radius) {
		ball.setSize(radius * 2, radius * 2);
	}

	/**
	 * Sets the position of the ball in the view.
	 * 
	 * @param x
	 *            the x coordinate of the ball.
	 * @param y
	 *            the y coordinate of the ball.
	 */
	public void setBallsPosition(double x, double y) {
		ball.setLocation((int) x, (int) y);
	}

	// ------------Brick methods-------------------------
	/**
	 * Brings an array of {@code BreakoutBrick}s on the {@code BreakoutView}.
	 * 
	 * @param bricks
	 *            The array of {@code BreakoutBrick}s.
	 */
	public void updateBricks(BreakoutBrick[] bricks) {
		for (BreakoutBrick brick : bricks) {
			if (brick != null) {
				brick.setFilled(true);
				add(brick);
			}
		}
	}

	/**
	 * Removes a brick from the view.
	 * 
	 * @param brick
	 *            The {@code BreakoutBrick} to be removed.
	 */
	public void removeBrick(BreakoutBrick brick) {
		remove(brick);

		// TODO evtl mit Animation? Viel Spass, Cecile ;-)
	}

	// ---------level methods--------------------------

	/**
	 * This method handles it, if the level is completed by the player.
	 */
	public void levelDone() {
		// TODO viel Spass, Cecile ;-)
		winnerLabel = new GLabel("Winner!", getWidth() / 2, getHeight() / 2);
		winnerLabel.setFont(new Font("Serif", Font.BOLD, 20));
		winnerLabel.setColor(Color.green);
		add(winnerLabel);
	}

	/**
	 * This method is called when a new level is started in order to remove
	 * unnecessary things from the view such as winner signs.
	 */
	public void levelStarted() {
		if (winnerLabel != null) {
			remove(winnerLabel);
		}
	}

	// ----------infoLabel methods--------------------
	/**
	 * Sets a new information text in the view.
	 * 
	 * @param text
	 *            the added text.
	 */
	public void setInfoText(String text) {
		infoLabel.setLabel(text);
	}

	/**
	 * Sets the information text visible or invisible.
	 * 
	 * @param show
	 *            {@code true} if the information text is visible {@code false} if
	 *            not.
	 */
	public void showInfoText(boolean show) {
		infoLabel.setVisible(show);
	}

	/**
	 * Checks if the information text is visible.
	 * 
	 * @return {@code true} if the information text is visible {@code false} if not.
	 */
	public boolean isInfoVisible() {
		return infoLabel.isVisible();
	}
}
