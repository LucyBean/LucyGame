package objects;

import helpers.Point;
import worlds.WorldLayer;

public abstract class Static extends GameObject {
	public Static(Point origin, WorldLayer layer, Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, layer, sprite, collider, interactBox);
	}
	
	public Static(Point origin, WorldLayer layer, Sprite sprite) {
		this(origin, layer, sprite, null, null);
	}
	
	public Static(Point origin, WorldLayer layer) {
		this(origin, layer, null, null, null);
	}

	@Override
	protected final void resetState() {
		resetStaticState();
	}
	
	protected abstract void resetStaticState();
}
