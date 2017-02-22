package objects.world.characters;

import java.util.Optional;

import helpers.Point;
import objects.attachments.Collider;
import objects.attachments.InteractBox;
import objects.images.Sprite;
import objects.world.Actor;
import objects.world.ItemType;
import worlds.WorldLayer;

public abstract class Enemy extends Actor {

	public Enemy(Point origin, ItemType itemType,
			Sprite sprite, Optional<Collider> collider, InteractBox interactBox) {
		super(origin, WorldLayer.WORLD, itemType, sprite, collider, interactBox);
	}
	
	public Enemy(Point origin, ItemType itemType) {
		this(origin, itemType, itemType.getSprite(), Optional.empty(), null);
		setColliderFromSprite();
	}

	public abstract void damage(int amount);
	

}
