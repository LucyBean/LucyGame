package objectLibrary;

import helpers.Point;
import images.SpriteBuilder;
import objects.Collider;
import objects.Lockable;
import objects.Static;
import worlds.WorldLayer;

public class Door extends Static implements Lockable {

	public Door(Point origin) {
		super(origin, WorldLayer.WORLD, SpriteBuilder.getDoorImg(),
				new Collider(SpriteBuilder.getDoorImg().getBoundingRectangle()),
				null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void lock() {
		enable();
	}

	@Override
	public void unlock() {
		disable();
	}

	@Override
	protected void resetStaticState() {
		
	}

}
