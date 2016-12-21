package objects.library;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import objects.attachments.ActorSticker;
import objects.images.SpriteBuilder;
import objects.world.Actor;
import objects.world.ItemType;
import worlds.WorldLayer;

public class MovingPlatform extends Actor {
	private float midPoint;
	private float velocity;
	private float omegaSquared = 0.0001f;

	public MovingPlatform(Point start, float distance) {
		super(start, WorldLayer.WORLD, ItemType.MOVING_WALL,
				SpriteBuilder.createRectangle(new Rectangle(Point.ZERO, 2, 1),
						32, new Color(220, 30, 220)));
		setColliderFromSprite();
		getCollider().setSolid(true);
		useGravity(false);
		midPoint = start.getX() + distance/2;
		velocity = 0.0f;
		
		ActorSticker as = new ActorSticker(new Point(0,-0.2f), 2, 0.2f);
		attach(as);
	}

	@Override
	protected void resetActorState() {
		
	}

	@Override
	public void act(GameContainer gc, int delta) {
		// Calculate acceleration from displacement
		float displacement = getPosition().getX() - midPoint;
		float acceleration = displacement * -omegaSquared;
		velocity += acceleration;
		
		// Move according to the velocity
		move(Dir.EAST, velocity * delta);
		
		// TODO: I think this probably suffers from rounding errors at low frame rates
		// TODO: Definitely some odd behaviour
		//    might have to evaluate it from using a sine function rather than
		//    using acceleration/velocity
	}

}
