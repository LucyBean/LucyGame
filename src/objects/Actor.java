package objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import worlds.GlobalOptions;

public class Actor extends GameObject {
	boolean clickedInLastFrame = false;
	boolean clickedInThisFrame = false;
	List<GameObject> activeInteractables;

	public Actor(Point origin, Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, sprite, collider, interactBox);
		activeInteractables = new ArrayList<GameObject>();
	}

	public Actor(Point origin, Sprite sprite) {
		this(origin, sprite, null, null);
	}

	// TODO
	// Move, with collision checking
	//
	public void move(Dir d, float amount) {
		// Calculate the whole area through which the Actor moves excluding its original box
		Rectangle moveArea;

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
		origin = translateToWorldCoOrds(origin);

		float width;
		float height;
		if (d == Dir.NORTH || d == Dir.SOUTH) {
			width = getCollider().getWidth();
			height = amount;
		} else {
			width = amount;
			height = getCollider().getHeight();
		}

		moveArea = new Rectangle(origin, width, height);

		// Find all solid objects to check
		// Check each solid's collider to see whether it overlaps with the wholeArea.
		// activeSolids will contain a list of solids that must be considered for collision
		// checking.
		List<GameObject> solids = getWorld().getAllSolids();
		List<GameObject> activeSolids = new ArrayList<GameObject>();
		Iterator<GameObject> si = solids.iterator();
		while (si.hasNext()) {
			GameObject go = si.next();
			if (go != this) {
				// Get collider rectangle in relative co-ordinates
				Rectangle rectRel = go.getCollider().getRectangle();
				// Translate to world co-ords.
				Rectangle rectWorld = rectRel.translate(go.getPosition());
				// If the collider overlaps with wholeArea, add to activeSolids list.
				if (rectWorld.overlaps(moveArea)) {
					activeSolids.add(go);
				}
			}
		}

		// If there are no active solids, move to that position immediately.
		if (activeSolids.isEmpty()) {
			setPosition(getPosition().move(d.asPoint().scale(amount)));
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

			position = position.move(d, toMove);
		}

		// Check for any interactables that are at the Actor's current position
		Rectangle thisArea = getCollider().getRectangle().translate(getPosition());
		List<GameObject> interactables = getWorld().getAllInteractables();
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
					activeInteractables.add(go);
				}
			}
		}
	}

	// TODO
	// Event reactions
	//
	/*
	 * added to world on created on collision on interact
	 */

	// TODO
	// Update
	//
	final public void update(GameContainer gc, int delta) {
		clickedInThisFrame = false;

		Input input = gc.getInput();
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			Point mouseLocation = new Point(input.getMouseX(), input.getMouseY());
			Rectangle boundingRectangle = translateToScreenCoOrds(
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
	}

	public void act(GameContainer gc, int delta) {

	}

	public void interactWithAll() {
		if (activeInteractables.isEmpty()) {
			if (GlobalOptions.DEBUG) {
				System.out.println(this + " interacted with nothing.");
			}
		} else {
			Iterator<GameObject> aii = activeInteractables.iterator();
			while (aii.hasNext()) {
				GameObject go = aii.next();
				go.interactedhBy(this);
				if (GlobalOptions.DEBUG) {
					System.out.println(this + " interacted with " + go);
				}
			}
		}
	}

	public void onMouseDown() {

	}

	public void onClick() {

	}
}
