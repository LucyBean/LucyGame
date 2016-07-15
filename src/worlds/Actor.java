package worlds;

import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;

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
