package objects.world;

import java.util.Optional;

import helpers.Point;
import objects.attachments.Collider;
import worlds.WorldLayer;

public class ClimbingWallMarker extends Static implements INeedSpecialBuildInfo {
	private static final float width = 0.3f;
	private float height;

	public ClimbingWallMarker(Point top, float length) {
		super(top, WorldLayer.WORLD, ItemType.CLIMBING_WALL_MARKER, null,
				Optional.of(new Collider(new Point(-width/2.0f, 0), width, length)),
				Optional.empty());
		this.height = length;
	}

	@Override
	protected void resetStaticState() {
		// TODO Auto-generated method stub

	}

	@Override
	public Point getFirstPoint() {
		return getPosition();
	}
	
	@Override
	public float getExtraFloat() {
		return height;
	}
}
