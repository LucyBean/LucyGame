package worlds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;

public class Actor extends GameObject {
	public Actor(Point origin, Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, sprite, collider, interactBox);
	}

	public Actor(Point origin, Sprite sprite) {
		this(origin, sprite, null, null);
	}

	// TODO
	// Move, with collision checking
	//
	public void move(Dir d, float amount) {
		// Calculate the whole area through which the Actor moves excluding its original box
		Rectangle wholeArea;
		
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
		
		wholeArea = new Rectangle(origin, width, height);

		// Find all solid objects to check
		// Check each solid's collider to see whether it overlaps with the wholeArea.
		// activeSolids will contain a list of solids that must be considered for collision checking.
		List<GameObject> solids = getWorld().getOnScreenSolids();
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
				if (rectWorld.overlaps(wholeArea)) {
					activeSolids.add(go);
				}
			}
		}

		// If there are no active solids, move to that position immediately.
		if (activeSolids.isEmpty()) {
			setPosition(getPosition().move(d.asPoint().scale(amount)));
			System.out.println("No active solids");
		}
		// Else move to edge of the d.neg()-most point
		else {
			System.out.println(activeSolids);
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
	public void update(GameContainer gc, int delta) {

	}
}
