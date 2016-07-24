package objects;

import java.util.List;

import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import worlds.WorldLayer;

public abstract class GravityActor extends Actor {
	final static float GRAVITY = 0.0001f;
	float vSpeed;

	public GravityActor(Point origin, WorldLayer layer, Sprite sprite, Collider collider,
			InteractBox interactBox) {
		super(origin, layer, sprite, collider, interactBox);
	}

	@Override
	protected void resetActorState() {
		vSpeed = 0.0f;
		resetGravityActorState();
	}

	protected abstract void resetGravityActorState();

	//
	// #JustGravityThings
	//
	/**
	 * Checks whether the GravityActor is on the floor.
	 * 
	 * @return
	 */
	protected boolean isOnGround() {
		if (getCollider() == null) {
			return false;
		}

		// Check a very narrow rectangle at the bottom of the actor for any solid objects.
		Collider c = getCollider();
		Rectangle bottomEdge = new Rectangle(c.getBottomLeft(), c.getWidth(), 0.001f).translate(
				getPosition());
		bottomEdge = bottomEdge.translate(new Point(0, 0.001f));

		// See if it collides with any objects
		List<GameObject> collidingSolids = getCollidingSolids(bottomEdge);
		return !collidingSolids.isEmpty();
	}

	/**
	 * Checks whether the GravityActor has hit the ceiling.
	 * 
	 * @return
	 */
	protected boolean isOnCeiling() {
		if (getCollider() == null) {
			return false;
		}

		// Check a very narrow rectangle at the top of the actor for any solid objects.
		Collider c = getCollider();
		Rectangle topEdge = new Rectangle(c.getTopLeft(), c.getWidth(), 0.001f).translate(
				getPosition());
		topEdge = topEdge.translate(new Point(0, -0.001f));

		// See if it collides with any objects
		List<GameObject> collidingSolids = getCollidingSolids(topEdge);
		return !collidingSolids.isEmpty();
	}

	private void calculateVSpeed(int delta) {
		// If player is on the ground and is falling, stop them.
		if (isOnGround() && vSpeed >= 0.0f) {
			vSpeed = 0.0f;
		} else if (isOnCeiling() && vSpeed < 0.0f) {
			vSpeed = 0.0f;
		} else {
			vSpeed += GRAVITY * delta;
		}
	}

	protected void jump(float strength) {
		if (isOnGround()) {
			vSpeed = -strength;
		}
	}

	public final void act(GameContainer gc, int delta) {
		gravityAct(gc, delta);

		calculateVSpeed(delta);
		move(Dir.SOUTH, vSpeed * delta);
	}

	public abstract void gravityAct(GameContainer gc, int delta);

}
