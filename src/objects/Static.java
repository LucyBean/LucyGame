package objects;

import helpers.Point;
import images.SingleSprite;
import images.SpriteBuilder;
import worlds.WorldLayer;

public abstract class Static extends WorldObject {
	public Static(Point origin, WorldLayer layer, ItemType itemType,
			SingleSprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, layer, itemType, sprite, collider, interactBox);
	}

	public Static(Point origin, WorldLayer layer, ItemType itemType,
			SingleSprite sprite) {
		this(origin, layer, itemType, sprite, null, null);
	}

	public Static(Point origin, WorldLayer layer, ItemType itemType) {
		this(origin, layer, itemType, SpriteBuilder.getWorldItem(itemType),
				null, null);
	}

	@Override
	protected final void resetState() {
		resetStaticState();
	}

	protected abstract void resetStaticState();
}
