package project.breakout.controller;

import project.breakout.model.BreakoutModel;

public class BreakoutTimer implements Runnable {
	private long pauseTime;
	private BreakoutModel model;
	public boolean go = true;

	public BreakoutTimer(BreakoutModel model, int framesPerSecond) {
		this.model = model;

		double pauseTimeDouble = 1.0 / framesPerSecond;
		pauseTime = (long) (pauseTimeDouble * 1000);
	}

	@Override
	public void run() {
		long lastTime = System.currentTimeMillis();
		while (go) {
			if (System.currentTimeMillis() >= lastTime + pauseTime)
			{
				lastTime = lastTime + pauseTime;
				model.updateBallsPosition((double) pauseTime / 1000);
			} 
		}

	}
}
