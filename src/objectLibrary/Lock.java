package objectLibrary;

import helpers.Point;
import objectLibs.SpriteBuilder;
import objects.InteractBox;
import objects.Static;
import objects.WorldObject;
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
	
	@Override
	public void overlapStart(WorldObject wo) {
		if (wo instanceof Player) {
			Player p = (Player) wo;
			if (p.has(key)) {
				disable();
				p.removeFromInventory(key);
			}
		}
	}
	

}
