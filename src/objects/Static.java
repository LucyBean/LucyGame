package objects;

import helpers.Point;
import images.Sprite;
import images.SpriteBuilder;
import worlds.WorldLayer;

public abstract class Static extends WorldObject {
	public Static(Point origin, WorldLayer layer, ItemType itemType,
			Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, layer, itemType, sprite, collider, interactBox);
	}

	public Static(Point origin, WorldLayer layer, ItemType itemType,
			Sprite sprite) {
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
