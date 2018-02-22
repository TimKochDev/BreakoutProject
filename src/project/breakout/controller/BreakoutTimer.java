package project.breakout.controller;

import java.util.TimerTask;

import project.breakout.model.BreakoutModel;

public class BreakoutTimer extends TimerTask {
	private BreakoutModel model;

	/**
	 * @param model
	 *            The current BreakoutModel.
	 * 
	 */
	public BreakoutTimer(BreakoutModel model) {
		this.model = model;

	}

	@Override
	public void run() {
		// create new frame
		model.updateBallsPosition();
	}
}
