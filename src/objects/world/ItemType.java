package objects.world;

import java.util.function.Supplier;

import objects.images.Sprite;
import objects.images.SpriteBuilder;
import options.GlobalOptions;

public enum ItemType {
	WALL(0),
		GEM(1),
		YELLOW_KEY(2),
		BLUE_KEY(3),
		RED_KEY(4),
		GREEN_KEY(5),
		YELLOW_LOCK(6),
		BLUE_LOCK(7),
		RED_LOCK(8),
		GREEN_LOCK(9),
		DOOR(() -> SpriteBuilder.getDoorSprite()),
		PLAYER(() -> SpriteBuilder.getCharacterSprite(0)),
		MATT(() -> SpriteBuilder.getCharacterSprite(1)),
		DOG_ENEMY(11),
		CLIMBING_WALL_MARKER,
		MOVING_PLATFORM(() -> SpriteBuilder.getColouredRectangle(2, 1, 4,
				GlobalOptions.GRID_SIZE), 2),
		TRAMPOLINE(() -> SpriteBuilder.getTrampolineSprite()),
		PUSHABLE_BLOCK(() -> SpriteBuilder.getColouredRectangle(2, 2, 5,
				GlobalOptions.GRID_SIZE)),
		NONE;

	private boolean paintable = true;
	private int numClicks = 1;

	ItemType(int imageID) {
		this(imageID, 1);
	}

	ItemType(int imageID, int numClicks) {
		spriteMaker = () -> SpriteBuilder.getWorldItem(imageID);
		this.numClicks = numClicks;
	}

	ItemType(Supplier<Sprite> spriteMaker) {
		this(spriteMaker, 1);
	}

	ItemType(Supplier<Sprite> spriteMaker, int numClicks) {
		this.spriteMaker = spriteMaker;
		this.numClicks = numClicks;
	}

	ItemType() {
		spriteMaker = () -> null;
		paintable = false;
	}

	Supplier<Sprite> spriteMaker;

	public Sprite getSprite() {
		return spriteMaker.get();
	}

	public boolean isPaintable() {
		return paintable;
	}

	/**
	 * Indicates whether this object requires multiple clicks to be placed in
	 * the map editor.
	 * 
	 * @return
	 */
	public boolean isMultiClick() {
		return numClicks > 1;
	}

	public static ItemType byId(int id) {
		if (id >= 0 && id < ItemType.values().length) {
			return ItemType.values()[id];
		} else {
			throw new IllegalArgumentException("Invalid ItemType id " + id);
		}
	}
}
