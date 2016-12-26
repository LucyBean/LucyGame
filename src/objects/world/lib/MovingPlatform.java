package objects.world.lib;

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
	private float equilibriumX;
	private float amplitude;
	private final int period;
	private final double omega;
	private int t;

	public MovingPlatform(Point start, float distance, int period) {
		super(start, WorldLayer.WORLD, ItemType.MOVING_WALL,
				SpriteBuilder.createRectangle(new Rectangle(Point.ZERO, 2, 1),
						32, new Color(220, 30, 220)));
		setColliderFromSprite();
		getCollider().setSolid(true);
		useGravity(false);
		amplitude = distance/2;
		equilibriumX = start.getX() + amplitude;
		this.period = period;
		omega = 2 * Math.PI / period;
		// TODO: check if the platform heads E/W initially
		t = period / 2; // The platform starts in the EAST-most position

		ActorSticker as = new ActorSticker(new Point(0, -0.2f), 2, 0.2f);
		attach(as);
	}

	@Override
	protected void resetActorState() {

	}

	@Override
	public void act(GameContainer gc, int delta) {
		t = (t + delta) % period;
		float newX = (float) (amplitude * Math.cos(omega * t)) + equilibriumX;
		float deltaX = newX - getPosition().getX();
		move(Dir.EAST, deltaX);

		// TODO: I think this probably suffers from rounding errors at low frame
		// rates
		// TODO: Definitely some odd behaviour
		// might have to evaluate it from using a sine function rather than
		// using acceleration/velocity
	}

}
