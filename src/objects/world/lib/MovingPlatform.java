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
	private Point center;
	private Point amplitude;
	private final int period;
	private final double omega;
	private int t = 0;

	public MovingPlatform(Point start, Point end, int period) {
		super(start, WorldLayer.WORLD, ItemType.MOVING_WALL,
				SpriteBuilder.createRectangle(new Rectangle(Point.ZERO, 2, 1),
						32, new Color(220, 30, 220)));
		setColliderFromSprite();
		getCollider().setSolid(true);
		useGravity(false);
		amplitude = start.move(end.neg()).scale(0.5f); // amplitude = (end - start) / 2
		center = end.move(amplitude);
		this.period = period;
		omega = 2 * Math.PI / period;

		ActorSticker as = new ActorSticker(new Point(0, -0.2f), 2, 0.2f);
		attach(as);
	}

	@Override
	protected void resetActorState() {

	}

	@Override
	public void act(GameContainer gc, int delta) {
		t = (t + delta) % period;
		double cosT = Math.cos(omega * t);
		float newPosX = (float) (amplitude.getX() * cosT) + center.getX();
		float newPosY = (float) (amplitude.getY() * cosT) + center.getY();
		Point newPos = new Point(newPosX, newPosY);
		Point deltaPos = newPos.move(getPosition().neg()); // Find difference in positions
		move(Dir.EAST, deltaPos.getX());
		move(Dir.SOUTH, deltaPos.getY());

		// TODO: I think this probably suffers from rounding errors at low frame
		// rates
		// TODO: Definitely some odd behaviour
		// might have to evaluate it from using a sine function rather than
		// using acceleration/velocity
	}

}
