package objects;

import helpers.Point;
import objectLibrary.Player;
import worlds.WorldLayer;

public class PickUpItem extends Static {
	public PickUpItem(Point origin, Sprite sprite) {
		super(origin, WorldLayer.WORLD, sprite, null,
				new InteractBox(sprite.getBoundingRectangle()));
	}

	@Override
	protected void resetStaticState() {
		
	}
	
	@Override
	public void overlapStart(Actor a) {
		if (a instanceof Player) {
			// TODO: Add to inventory
			disable();
		}
	}

}
