package project.breakout.view;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import acm.graphics.GCanvas;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import project.breakout.model.BreakoutModel;

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
	private GLabel winnerLabel;

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
	 * 
	 * @param bricks
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

	// ---------Highscore methods-------------------
	/**
	 * Causes the view to show a highscore Ranking
	 */
	public void showHighscoreRanking(List names, List values) {
		removeAll();
		for (int i = 0; i < 10; i++) {
			GLabel name = new GLabel(names.getItem(i));
			GLabel value = new GLabel(values.getItem(i));

			name.setLocation(10, 10 + 15 * i);
			value.setLocation(name.getX() + name.getWidth() + 20, 10 + 15 * i);
			
			add(name);
			add(value);
		}
	}

	/**
	 * Asks the user for his name and returns it as a String.
	 * 
	 */
	public void showPlayersNameDialog() {
		removeAll();

		// Show textfield
		JTextField txtField = new JTextField();
		txtField.setSize(getWidth() / 2, txtField.getHeight());
		txtField.setLocation(getWidth() / 2 - txtField.getWidth() / 2, (int) (getHeight() * 0.65));
		add(txtField);

		// Add OK Button
		JButton okButton = new JButton("OK");
		okButton.setText("OK");
		okButton.setSize(100, txtField.getHeight() + 10);
		okButton.setLocation(txtField.getX() + txtField.getWidth() / 2 - okButton.getWidth() / 2, txtField.getY() + 20);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BreakoutModel.playersNameTyped(txtField.getText());

			}
		});
		add(okButton);
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
