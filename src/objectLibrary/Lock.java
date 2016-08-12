package objectLibrary;

import helpers.Point;
import objectLibs.SpriteBuilder;
import objects.InteractBox;
import objects.Static;
import worlds.WorldLayer;

public class Lock extends Static {
	Key key;

	public Lock(Point origin, Key key) {
		super(origin, WorldLayer.WORLD, SpriteBuilder.getLockImg(), null,
				new InteractBox(
						SpriteBuilder.getLockImg().getBoundingRectangle()));
		this.key = key;
	}

	@Override
	protected void resetStaticState() {
		
	}
	
	public void unlock() {
		disable();
	}
	
	public Key getKey() {
		return key;
	}
	

}
