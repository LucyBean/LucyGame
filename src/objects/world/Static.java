package objects.world;

import java.util.Optional;

import helpers.Point;
import objects.attachments.Collider;
import objects.attachments.InteractBox;
import objects.images.Sprite;
import worlds.WorldLayer;

public abstract class Static extends WorldObject {
	public Static(Point origin, WorldLayer layer, ItemType itemType,
			Sprite sprite, Optional<Collider> collider, Optional<InteractBox> interactBox) {
		super(origin, layer, itemType, sprite, collider, interactBox);
	}

	/**
	 * Create a new Static of the required ItemType without a Collider or InteractBox.
	 * @param origin
	 * @param layer
	 * @param itemType
	 */
	public Static(Point origin, WorldLayer layer, ItemType itemType) {
		this(origin, layer, itemType, itemType.getSprite(),
				Optional.empty(), Optional.empty());
	}

	@Override
	protected final void resetState() {
		resetStaticState();
	}

	protected abstract void resetStaticState();
}
