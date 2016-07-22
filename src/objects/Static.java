package objects;

import helpers.Point;

public abstract class Static extends GameObject {
	public Static(Point origin, Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, sprite, collider, interactBox);
	}
	
	public Static(Point origin, Sprite sprite) {
		this(origin, sprite, null, null);
	}
	
	public Static(Point origin) {
		this(origin, null, null, null);
	}

	@Override
	protected final void resetState() {
		resetStaticState();
	}
	
	protected abstract void resetStaticState();
}
