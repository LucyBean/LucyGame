package characters;

import helpers.Point;
import images.Sprite;
import images.SpriteBuilder;
import objects.Actor;
import objects.Collider;
import objects.InteractBox;
import objects.ItemType;
import worlds.WorldLayer;

public abstract class Enemy extends Actor {

	public Enemy(Point origin, ItemType itemType,
			Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, WorldLayer.WORLD, itemType, sprite, collider, interactBox);
	}
	
	public Enemy(Point origin, ItemType itemType) {
		this(origin, itemType, SpriteBuilder.getWorldItem(itemType), null, null);
		setColliderFromSprite();
	}
	

}
