package project.breakout.view;

import java.awt.Color;
import java.awt.Font;

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

	private GRect paddle = new GRect(0, 0);
	private BreakoutBall ball = new BreakoutBall(0);
	private GLabel infoLabel = new GLabel("", 0, 10);

	public BreakoutView(int canvasWidth, int canvasHeight) {
		paddle.setFilled(true);
		paddle.setFillColor(Color.red);
		ball.setFillColor(Color.red);
		ball.setFilled(true);
		infoLabel.setVisible(false);

		setSize(canvasWidth, canvasHeight);
		add(paddle);
		add(ball);
		add(infoLabel);
	}

	// ---------paddle methods--------------------------
	/**
	 * Sets the paddle location in the view.
	 * 
	 * @param xCoord
	 * @param yCoord
	 */
	public void setPaddleLocation(int xCoord, int yCoord) {
		paddle.setLocation(xCoord, yCoord);
	}

	/**
	 * Sets the paddle size in the view.
	 * 
	 * @param width
	 * @param height
	 */
	public void setPaddleSize(int width, int height) {
		paddle.setSize(width, height);
	}

	// ----------ball methods-------------------------
	/**
	 * Sets the ball radius in the view
	 * 
	 * @param radius
	 */
	public void setBallsRadius(int radius) {
		ball.setSize(radius * 2, radius * 2);
	}

	/**
	 * Sets the position of the ball in the view.
	 * 
	 * @param x
	 * @param y
	 */
	public void setBallsPosition(double x, double y) {
		ball.setLocation((int) x, (int) y);
	}

	// ------------Brick methods-------------------------
	public void updateBricks(BreakoutBrick[] bricks) {
		for (BreakoutBrick brick : bricks) {
			if (brick != null) {
				brick.setFilled(true);
				add(brick);
			}
		}
	}

	/**
	 * Removes a brick from the view
	 * 
	 * @param brick
	 *            The {@code BreakoutBrick} to be removed.
	 */
	public void removeBrick(BreakoutBrick brick) {
		remove(brick);

		// TODO evtl mit Animation? Viel Spaﬂ, CÈcile ;-)
	}

	/**
	 * This method handles it, if the level is completed by the player.
	 */
	public void levelDone() {
		// TODO viel Spaﬂ, CÈcile ;-)
		GLabel winnerLabel = new GLabel("Winner!", getWidth()/2, getHeight()/2);
		winnerLabel.setFont(new Font("Serif", Font.BOLD, 20));
		winnerLabel.setColor(Color.red);
		add(winnerLabel);
	}

	// ----------infoLabel methods--------------------
	public void setInfoText(String text) {
		infoLabel.setLabel(text);
	}

	public void showInfoText(boolean show) {
		infoLabel.setVisible(show);
	}

	public boolean isInfoVisible() {
		return infoLabel.isVisible();
	}
}
