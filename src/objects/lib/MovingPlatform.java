package objects.lib;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import objects.images.SpriteBuilder;
import objects.world.Actor;
import objects.world.ItemType;
import worlds.WorldLayer;

public class MovingPlatform extends Actor {

	public MovingPlatform(Point origin) {
		super(origin, WorldLayer.WORLD, ItemType.MOVING_WALL,
				SpriteBuilder.createRectangle(new Rectangle(Point.ZERO, 2, 1),
						32, new Color(220, 30, 220)));
		setColliderFromSprite();
		getCollider().setSolid(true);
		useGravity(false);
	}

	@Override
	protected void resetActorState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void act(GameContainer gc, int delta) {
		// TODO Auto-generated method stub
		
	}

}
