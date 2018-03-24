package project.breakout.model;

import project.breakout.controller.CollisionWith;

public interface CollisionListener {
	void collisionEvent(CollisionWith lastCollisionWith);
}
