package project.breakout.controller;

import java.util.TimerTask;

import project.breakout.model.BreakoutModel;

public class BreakoutTimer extends TimerTask {
	private long pauseTime;
	private BreakoutModel model;

	/**
	 * @param model
	 *            The current BreakoutModel.
	 * @param pauseTime
	 *            Time between frames in milliseconds.
	 */
	public BreakoutTimer(BreakoutModel model, long pauseTime) {
		this.model = model;
		this.pauseTime = pauseTime;
	}

	@Override
	public void run() {
		// create new frame
		model.updateBallsPosition((double) pauseTime / 1000);
	}
}
