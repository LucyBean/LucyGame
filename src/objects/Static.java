package objects;

import helpers.Point;

public class Static extends GameObject {
	public Static(Point origin, Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, sprite, collider, interactBox);
	}
	
	public Static(Point origin, Sprite sprite) {
		this(origin, sprite, null, null);
	}
}
