package objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import worlds.WorldLayer;

public abstract class Actor extends GameObject {
	boolean clickedInLastFrame;
	boolean clickedInThisFrame;
	List<GameObject> activeInteractables;
	Dir lastDirectionMoved;

	public Actor(Point origin, WorldLayer layer, Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, layer, sprite, collider, interactBox);
	}

	public Actor(Point origin, WorldLayer layer, Sprite sprite) {
		this(origin, layer, sprite, null, null);
	}
	
	public Actor(Point origin, WorldLayer layer) {
		this(origin, layer, null, null, null);
	}

	protected final void resetState() {
		clickedInLastFrame = false;
		clickedInThisFrame = false;
		activeInteractables = new ArrayList<GameObject>();
		resetActorState();
	}

	/**
	 * This should be used to set any initial state. This is called when the Actor is constructed
	 * and reset.
	 */
	protected abstract void resetActorState();
	
	//
	// Getters
	//
	public Dir getLastDirectionMoved() {
		return lastDirectionMoved;
	}

	// TODO
	// Move, with collision checking
	//
	public void move(Dir d, float amount) {
		if (getCollider() == null) {
			setPosition(getPosition().move(d,amount));
		} else {
			Point newPos = findNewPosition(d, amount);
			setPosition(newPos);
		}
		lastDirectionMoved = d;
	}

	/**
	 * Calculates the rectangle through which the object will move, assuming there are no
	 * collisions.
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
		
		// Calculate the whole area through which the Actor moves excluding its original box
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
		origin = objectToWorldCoOrds(origin);

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
	
	protected List<GameObject> getCollidingSolids(Rectangle rect) {
		List<GameObject> solids = getWorld().getActiveSolids();
		List<GameObject> collidingSolids = new ArrayList<GameObject>();
		Iterator<GameObject> si = solids.iterator();
		while (si.hasNext()) {
			GameObject go = si.next();
			if (go != this) {
				// Get collider rectangle in relative co-ordinates
				Rectangle rectRel = go.getCollider().getRectangle();
				// Translate to world co-ords.
				Rectangle rectWorld = rectRel.translate(go.getPosition());
				// If the collider overlaps with wholeArea, add to activeSolids list.
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
		List<GameObject> activeSolids = getCollidingSolids(moveArea);

		// If there are no active solids, move to that position immediately.
		if (activeSolids.isEmpty()) {
			return getPosition().move(d.asPoint().scale(amount));
		}
		// Else move to edge of the d.neg()-most point
		else {
			float toMove = 0.0f;
			Iterator<GameObject> asi = activeSolids.iterator();
			GameObject go;
			switch (d) {
				case NORTH: {
					go = asi.next();
					float maxSouth = go.getCollider().getBottomLeft().move(go.getPosition()).getY();
					while (asi.hasNext()) {
						go = asi.next();
						Point bl = go.getCollider().getBottomLeft().move(go.getPosition());
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
					float maxNorth = go.getCollider().getTopLeft().move(go.getPosition()).getY();
					while (asi.hasNext()) {
						go = asi.next();
						Point tl = go.getCollider().getTopLeft().move(go.getPosition());
						// If this is WEST-most object set as new origin
						if (tl.getY() < maxNorth) {
							maxNorth = tl.getY();
						}
					}
					toMove = maxNorth - getPosition().getY() - getCollider().getHeight();
					break;
				}

				case EAST: {
					go = asi.next();
					float maxWest = go.getCollider().getTopLeft().move(go.getPosition()).getX();
					while (asi.hasNext()) {
						go = asi.next();
						Point tl = go.getCollider().getTopLeft().move(go.getPosition());
						// If this is WEST-most object set as new origin
						if (tl.getX() < maxWest) {
							maxWest = tl.getX();
						}
					}
					toMove = maxWest - getPosition().getX() - getCollider().getWidth();
					break;
				}

				case WEST: {
					go = asi.next();
					float maxEast = go.getCollider().getTopRight().move(go.getPosition()).getX();
					while (asi.hasNext()) {
						go = asi.next();
						Point tr = go.getCollider().getTopRight().move(go.getPosition());
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

	private List<GameObject> findInteractablesHere() {
		if (getCollider() == null) {
			return new ArrayList<GameObject>();
		}
		// Check for any interactables that are at the Actor's current position
		Rectangle thisArea = getCollider().getRectangle().translate(getPosition());
		List<GameObject> interactables = getWorld().getAllInteractables();
		List<GameObject> nowActive = new ArrayList<GameObject>();
		Iterator<GameObject> ii = interactables.iterator();
		while (ii.hasNext()) {
			GameObject go = ii.next();
			if (go != this) {
				// Get interaction rectangle in relative co-ordinates
				Rectangle rectRel = go.getInteractBox().getRectangle();
				// Translate to world co-ords.
				Rectangle rectWorld = rectRel.translate(go.getPosition());
				// If the interaction overlaps with wholeArea, add to activeInteractables list.
				if (rectWorld.overlaps(thisArea)) {
					nowActive.add(go);
				}
			}
		}

		return nowActive;
	}

	/**
	 * Returns the list (a - b) (i.e. all elements in a that are not in b). This will not modify the
	 * original lists.
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	private <T> List<T> subtract(List<T> a, List<T> b) {
		List<T> toReturn = new ArrayList<T>(a);
		toReturn.removeAll(b);
		return toReturn;
	}

	private void checkForInteractions() {
		List<GameObject> nowActive = findInteractablesHere();
		List<GameObject> prevActive = activeInteractables;
		// Figure out the objects that are newly active/inactive
		// activeInteractables is the list of interactables active last iteration
		// nowActive is the list of interactables active this iteration
		List<GameObject> newlyActive = subtract(nowActive, prevActive);
		List<GameObject> newlyInactive = subtract(prevActive, nowActive);

		// Call overlapStart and overlapEnd on these newly active/inactive objects
		Iterator<GameObject> ii = newlyActive.iterator();
		while (ii.hasNext()) {
			GameObject go = ii.next();
			go.overlapStart(this);
		}

		ii = newlyInactive.iterator();
		while (ii.hasNext()) {
			GameObject go = ii.next();
			go.overlapEnd(this);
		}

		activeInteractables = nowActive;
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
	final public void update(GameContainer gc, int delta) throws InvalidObjectStateException {
		if (!isEnabled()) {
			throw new InvalidObjectStateException(
					"Tried to update " + this + " but it is disabled.");
		}

		clickedInThisFrame = false;

		Input input = gc.getInput();
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			Point mouseLocation = new Point(input.getMouseX(), input.getMouseY());
			Rectangle boundingRectangle = objectToScreenCoOrds(
					getSprite().getBoundingRectangle(), getWorld().getCamera());
			if (boundingRectangle.contains(mouseLocation)) {
				clickedInThisFrame = true;
				onMouseDown();
			}
		}

		if (clickedInThisFrame && !clickedInLastFrame) {
			onClick();
		}

		clickedInLastFrame = clickedInThisFrame;
		act(gc, delta);
		checkForInteractions();
	}

	public abstract void act(GameContainer gc, int delta);

	public void interactWithAll() {
		if (activeInteractables.isEmpty()) {
		} else {
			Iterator<GameObject> aii = activeInteractables.iterator();
			while (aii.hasNext()) {
				GameObject go = aii.next();
				go.interactedBy(this);
			}
		}
	}
}
