package objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import images.Sprite;
import worlds.WorldLayer;

public abstract class Actor extends WorldObject {
	private Collection<WorldObject> activeInteractables;
	private Dir lastDirectionMoved;
	private final static float GRAVITY = 0.0001f;
	private float vSpeed;
	private boolean gravityEnabled;

	public Actor(Point origin, WorldLayer layer, Sprite sprite,
			Collider collider, InteractBox interactBox) {
		super(origin, layer, sprite, collider, interactBox);
	}

	public Actor(Point origin, WorldLayer layer, Sprite sprite) {
		this(origin, layer, sprite, null, null);
	}

	public Actor(Point origin, WorldLayer layer) {
		this(origin, layer, null, null, null);
	}

	protected final void resetState() {
		activeInteractables = new ArrayList<WorldObject>();
		vSpeed = 0.0f;
		gravityEnabled = true;
		resetActorState();
	}

	/**
	 * This should be used to set any initial state. This is called when the
	 * Actor is constructed and reset.
	 */
	protected abstract void resetActorState();

	//
	// Getters
	//
	public Dir getLastDirectionMoved() {
		return lastDirectionMoved;
	}

	public boolean gravityEnabled() {
		return gravityEnabled;
	}

	//
	// Setters
	//
	public void useGravity(boolean gravity) {
		gravityEnabled = gravity;
	}

	//
	// Move, with collision checking
	//
	public void move(Dir d, float amount) {
		if (getCollider() == null) {
			setPosition(getPosition().move(d, amount));
		} else {
			Point newPos = findNewPosition(d, amount);
			setPosition(newPos);
		}
		lastDirectionMoved = d;
	}

	/**
	 * Calculates the rectangle through which the object will move, assuming
	 * there are no collisions.
	 * 
	 * @param d
	 *            Direction in which to move.
	 * @param amount
	 *            Amount by which to move.
	 * @return The area through which the Actor moves, assuming no collisions
	 */
	private Rectangle calculateMoveArea(Dir d, float amount) {
		// Reverse the direction if moving a negative amount
		if (amount < 0) {
			d = d.neg();
			amount = -amount;
		}

		// Calculate the whole area through which the Actor moves excluding its
		// original box
		Point origin;
		// move the origin for NORTH and WEST
		if (d == Dir.NORTH || d == Dir.WEST) {
			origin = getCollider().getTopLeft().move(d, amount);
		}
		// set origin to bottom left for SOUTH
		else if (d == Dir.SOUTH) {
			origin = getCollider().getBottomLeft();
		}
		// set origin to top right for EAST
		else {
			origin = getCollider().getTopRight();
		}
		// Translate the origin from object to world co-ords
		origin = getCoOrdTranslator().objectToWorldCoOrds(origin);

		float width;
		float height;
		if (d == Dir.NORTH || d == Dir.SOUTH) {
			width = getCollider().getWidth();
			height = amount;
		} else {
			width = amount;
			height = getCollider().getHeight();
		}

		return new Rectangle(origin, width, height);
	}

	protected Collection<WorldObject> getCollidingSolids(Rectangle rect) {
		Collection<WorldObject> solids = getWorld().getActiveSolids();
		Collection<WorldObject> collidingSolids = new ArrayList<WorldObject>();
		Iterator<WorldObject> si = solids.iterator();
		while (si.hasNext()) {
			WorldObject go = si.next();
			if (go != this) {
				// Get collider rectangle in relative co-ordinates
				Rectangle rectRel = go.getCollider().getRectangle();
				// Translate to world co-ords.
				Rectangle rectWorld = rectRel.translate(go.getPosition());
				// If the collider overlaps with wholeArea, add to activeSolids
				// list.
				if (rectWorld.overlaps(rect)) {
					collidingSolids.add(go);
				}
			}
		}

		return collidingSolids;
	}

	/**
	 * Checks for collisions to determine the new position of the object.
	 * 
	 * @return The object's new position
	 */
	private Point findNewPosition(Dir d, float amount) {
		// flip direction and amount if amount is negative
		if (amount < 0) {
			d = d.neg();
			amount = -amount;
		}
		Rectangle moveArea = calculateMoveArea(d, amount);
		Collection<WorldObject> activeSolids = getCollidingSolids(moveArea);

		// If there are no active solids, move to that position immediately.
		if (activeSolids.isEmpty()) {
			return getPosition().move(d.asPoint().scale(amount));
		}
		// Else move to edge of the d.neg()-most point
		else {
			float toMove = 0.0f;
			Iterator<WorldObject> asi = activeSolids.iterator();
			WorldObject go;
			switch (d) {
				case NORTH: {
					go = asi.next();
					float maxSouth = go.getCollider().getBottomLeft().move(
							go.getPosition()).getY();
					while (asi.hasNext()) {
						go = asi.next();
						Point bl = go.getCollider().getBottomLeft().move(
								go.getPosition());
						// If this is WEST-most object set as new origin
						if (bl.getY() > maxSouth) {
							maxSouth = bl.getY();
						}
					}
					toMove = getPosition().getY() - maxSouth;
					break;

				}

				case SOUTH: {
					go = asi.next();
					float maxNorth = go.getCollider().getTopLeft().move(
							go.getPosition()).getY();
					while (asi.hasNext()) {
						go = asi.next();
						Point tl = go.getCollider().getTopLeft().move(
								go.getPosition());
						// If this is WEST-most object set as new origin
						if (tl.getY() < maxNorth) {
							maxNorth = tl.getY();
						}
					}
					toMove = maxNorth - getPosition().getY()
							- getCollider().getHeight();
					break;
				}

				case EAST: {
					go = asi.next();
					float maxWest = go.getCollider().getTopLeft().move(
							go.getPosition()).getX();
					while (asi.hasNext()) {
						go = asi.next();
						Point tl = go.getCollider().getTopLeft().move(
								go.getPosition());
						// If this is WEST-most object set as new origin
						if (tl.getX() < maxWest) {
							maxWest = tl.getX();
						}
					}
					toMove = maxWest - getPosition().getX()
							- getCollider().getWidth();
					break;
				}

				case WEST: {
					go = asi.next();
					float maxEast = go.getCollider().getTopRight().move(
							go.getPosition()).getX();
					while (asi.hasNext()) {
						go = asi.next();
						Point tr = go.getCollider().getTopRight().move(
								go.getPosition());
						// If this is WEST-most object set as new origin
						if (tr.getX() > maxEast) {
							maxEast = tr.getX();
						}
					}
					toMove = getPosition().getX() - maxEast;
					break;
				}
			}

			return getPosition().move(d, toMove);
		}
	}

	private Collection<WorldObject> findInteractablesHere() {
		// Check for any interactables that are at the Actor's current position
		Rectangle thisArea = getCollider().getRectangle().translate(
				getPosition());
		Collection<WorldObject> interactables = getWorld().getAllInteractables();
		Collection<WorldObject> nowActive = new ArrayList<WorldObject>();
		Iterator<WorldObject> ii = interactables.iterator();
		while (ii.hasNext()) {
			WorldObject go = ii.next();
			if (go != this && go.isEnabled()) {
				// Get interaction rectangle in relative co-ordinates
				Rectangle rectRel = go.getInteractBox().getRectangle();
				// Translate to world co-ords.
				Rectangle rectWorld = rectRel.translate(go.getPosition());
				// If the interaction overlaps with wholeArea, add to
				// activeInteractables list.
				if (rectWorld.overlaps(thisArea)) {
					nowActive.add(go);
				}
			}
		}

		return nowActive;
	}

	/**
	 * Returns the list (a - b) (i.e. all elements in a that are not in b). This
	 * will not modify the original lists.
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	private <T> Collection<T> subtract(Collection<T> a, Collection<T> b) {
		Collection<T> toReturn = new HashSet<T>(a);
		toReturn.removeAll(b);
		return toReturn;
	}

	private void checkForInteractions() {
		if (getCollider() != null) {
			Collection<WorldObject> nowActive = findInteractablesHere();
			Collection<WorldObject> prevActive = activeInteractables;
			// Figure out the objects that are newly active/inactive
			// activeInteractables is the list of interactables active last
			// iteration
			// nowActive is the list of interactables active this iteration
			Collection<WorldObject> newlyActive = subtract(nowActive, prevActive);
			Collection<WorldObject> newlyInactive = subtract(prevActive, nowActive);

			// Call overlapStart and overlapEnd on these newly active/inactive
			// objects
			Iterator<WorldObject> ii = newlyActive.iterator();
			while (ii.hasNext()) {
				WorldObject go = ii.next();
				overlapStart(go);

				// If the overlapping WorldObject is an Actor it will detect the
				// overlap
				// and trigger overlapStart itself. Otherwise, it must be
				// triggered by
				// the newly overlapping Actor
				if (!(go instanceof Actor)) {
					go.overlapStart(this);
				}
			}

			ii = newlyInactive.iterator();
			while (ii.hasNext()) {
				WorldObject go = ii.next();
				overlapEnd(go);
				if (!(go instanceof Actor)) {
					go.overlapEnd(this);
				}
			}

			activeInteractables = nowActive;
		}
	}

	//
	// #JustGravityThings
	//
	/**
	 * Checks whether this Actor is on the floor.
	 * 
	 * @return
	 */
	protected boolean isOnGround() {
		if (getCollider() == null) {
			return false;
		}

		// Check a very narrow rectangle at the bottom of the actor for any
		// solid objects.
		Collider c = getCollider();
		Rectangle bottomEdge = new Rectangle(c.getBottomLeft(), c.getWidth(),
				0.001f).translate(getPosition());
		bottomEdge = bottomEdge.translate(new Point(0, 0.001f));

		// See if it collides with any objects
		Collection<WorldObject> collidingSolids = getCollidingSolids(bottomEdge);
		return !collidingSolids.isEmpty();
	}

	/**
	 * Checks whether this Actor has hit the ceiling.
	 * 
	 * @return
	 */
	protected boolean isOnCeiling() {
		if (getCollider() == null) {
			return false;
		}

		// Check a very narrow rectangle at the top of the actor for any solid
		// objects.
		Collider c = getCollider();
		Rectangle topEdge = new Rectangle(c.getTopLeft(), c.getWidth(),
				0.001f).translate(getPosition());
		topEdge = topEdge.translate(new Point(0, -0.001f));

		// See if it collides with any objects
		Collection<WorldObject> collidingSolids = getCollidingSolids(topEdge);
		return !collidingSolids.isEmpty();
	}

	private void calculateVSpeed(int delta) {
		if (gravityEnabled()) {
			// If player is on the ground and is falling, stop them.
			if (isOnGround() && vSpeed >= 0.0f) {
				vSpeed = 0.0f;
			} else if (isOnCeiling() && vSpeed < 0.0f) {
				vSpeed = 0.0f;
			} else {
				vSpeed += GRAVITY * delta;
			}
		}
	}

	protected void jump(float strength) {
		if (gravityEnabled() && isOnGround()) {
			vSpeed = -strength;
		}
	}

	// TODO
	// Event reactions
	//
	/*
	 * added to world, on created, on collision, on interact
	 */
	public void onMouseDown() {

	}

	public void onClick() {

	}

	// TODO
	// Update
	//
	final public void update(GameContainer gc, int delta) {
		if (isEnabled()) {
			act(gc, delta);
			checkForInteractions();

			if (gravityEnabled()) {
				calculateVSpeed(delta);
				move(Dir.SOUTH, vSpeed * delta);
			}
		}
	}

	public abstract void act(GameContainer gc, int delta);

	public void interactWithAll() {
		if (activeInteractables.isEmpty()) {
		} else {
			Iterator<WorldObject> aii = activeInteractables.iterator();
			while (aii.hasNext()) {
				WorldObject go = aii.next();
				go.interactedBy(this);
			}
		}
	}
}
