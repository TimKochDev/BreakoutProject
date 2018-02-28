package project.breakout.controller;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import project.breakout.model.BreakoutModel;
import project.breakout.view.BreakoutView;

/**
 * This class represents the mouse and keyboard controllers used for the game.
 *
 */
public class BreakoutController {
	private BreakoutView view;
	private BreakoutModel model;

	/**
	 * Constructor for the mouse and keyboard controllers.
	 * 
	 * @param model
	 *            The {@code BreakoutModel} of the current game.
	 * @param view
	 *            The view representing the current game.
	 */
	public BreakoutController(BreakoutModel model, BreakoutView view) {
		this.view = view;
		this.model = model;
		initControllers();
	}

	private void initControllers() {

		view.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				model.startGame();
			}
		});
		view.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				model.updateMouseLocation(e.getPoint());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		model.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				model.continueGame();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				model.resizedView(model.getWidth(), model.getHeight());

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				model.pauseGame();
			}
		});

		view.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				switch (e.getKeyChar()) {
				case 'i':
					// toggle info text / overlay in the view
					if (view.isInfoVisible()) {
						view.showInfoText(false);
					} else {
						view.showInfoText(true);
					}
					break;
				case 'p':
					if (!model.isGamePaused()) {
						model.pauseGame();
					} else {
						model.continueGame();
					}
					break;
				case 'l':
					if (BreakoutModel.isLighthouseEnabled()) {
						BreakoutModel.setLighthouseEnabled(false);
					} else {
						BreakoutModel.setLighthouseEnabled(true);
					}
				}				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {				
			}
		});

	}
}
