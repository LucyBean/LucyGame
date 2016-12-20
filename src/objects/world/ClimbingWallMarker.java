package objects.world;

import helpers.Point;
import objects.attachments.Collider;
import worlds.WorldLayer;

public class ClimbingWallMarker extends Static {
	private static final float width = 0.3f;

	public ClimbingWallMarker(Point top, float length) {
		super(top, WorldLayer.WORLD, ItemType.CLIMBING_WALL_MARKER, null,
				new Collider(new Point(-width/2.0f, 0), width, length),
				null);
	}

	@Override
	protected void resetStaticState() {
		// TODO Auto-generated method stub

	}
}
