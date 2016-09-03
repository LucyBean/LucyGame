package objects.characters;

import helpers.Point;
import images.Sprite;
import objects.Actor;
import objects.Collider;
import objects.InteractBox;
import worlds.WorldLayer;

public abstract class GameCharacter extends Actor {

	public GameCharacter(Point origin, WorldLayer layer, Sprite sprite,
			Collider collider, InteractBox interactBox) {
		super(origin, layer, sprite, collider, interactBox);
	}

	public GameCharacter(Point origin, WorldLayer layer, Sprite sprite) {
		this(origin, layer, sprite, null, null);
	}

	public GameCharacter(Point origin, WorldLayer layer) {
		this(origin, layer, null, null, null);
	}
	
	public abstract String getName();
}
