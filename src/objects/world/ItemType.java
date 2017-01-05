package objects.world;

import java.util.function.Supplier;

import org.newdawn.slick.Color;

import helpers.Point;
import helpers.Rectangle;
import objects.images.Sprite;
import objects.images.SpriteBuilder;

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
		MOVING_PLATFORM(() -> SpriteBuilder.createRectangle(
				new Rectangle(Point.ZERO, 2, 1), 32, new Color(220, 30, 220))),
		TRAMPOLINE(() -> SpriteBuilder.getTrampolineSprite()),
		PUSHABLE_BLOCK(() -> SpriteBuilder.createRectangle(
				new Rectangle(Point.ZERO, 2, 2), 32, new Color(30, 220, 30)));

	private boolean paintable = true;

	ItemType(int imageID) {
		spriteMaker = () -> SpriteBuilder.getWorldItem(imageID);
	}

	ItemType(Supplier<Sprite> spriteMaker) {
		this.spriteMaker = spriteMaker;
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

	public static ItemType byId(int id) {
		if (id >= 0 && id < ItemType.values().length) {
			return ItemType.values()[id];
		} else {
			return null;
		}
	}
}
