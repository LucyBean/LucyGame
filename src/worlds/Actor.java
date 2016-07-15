package worlds;

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
		// Calculate the whole area through which the Actor moves
		// width = move amount + collider width
		Rectangle wholeArea = new Rectangle(position, d.asPoint().getX()*amount + collider.getWidth(), d.asPoint().getY()*amount + collider.getHeight());
		
		// Find all solid objects to check
		List<GameObject> activeSolids = getWorld().getOnScreenSolids();
		
		position = position.move(d, amount);
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
