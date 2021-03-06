package objects.world.lib;

import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;
import objects.attachments.ActorSticker;
import objects.world.Actor;
import objects.world.INeedSpecialBuildInfo;
import objects.world.ItemType;
import worlds.WorldLayer;

public class MovingPlatform extends Actor implements INeedSpecialBuildInfo {
	private Point center;
	private Point amplitude;
	private int period;
	private double omega;
	private int t = 0;
	
	// Needed for rebuilding
	private Point start;
	private Point end;

	public MovingPlatform(Point start, Point end, int period) {
		super(start, WorldLayer.WORLD, ItemType.MOVING_PLATFORM);
		setColliderFromSprite();
		getCollider().get().setSolid(true);
		useGravity(false);
		amplitude = start.move(end.neg()).scale(0.5f); // amplitude = (end -
														// start) / 2
		center = end.move(amplitude);
		setPeriod(period);
		this.start = start;
		this.end = end;

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
		// Ignore small amplitude oscillations
		if (Math.abs(amplitude.getX()) > 0.1f) {
			float newPosX = (float) (amplitude.getX() * cosT) + center.getX();
			float deltaX = newPosX - getPosition().getX();
			move(Dir.EAST, deltaX);
		}
		if (Math.abs(amplitude.getY()) > 0.1f) {
			float newPosY = (float) (amplitude.getY() * cosT) + center.getY();
			float deltaY = newPosY - getPosition().getY();
			move(Dir.SOUTH, deltaY);
		}
	}
	
	protected void setPeriod(int period) {
		this.period = period;
		omega = 2 * Math.PI / period;
	}

	@Override
	public Point getFirstPoint() {
		return start;
	}
	
	@Override
	public Point getSecondPoint() {
		return end;
	}
	
	@Override
	public int getExtraInt() {
		return period;
	}

}
