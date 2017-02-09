package objects.world;

import helpers.Point;

/**
 * Marker interface for classes that require special build info
 * @author Lucy
 *
 */
public interface INeedSpecialBuildInfo {
	public default int getExtraInt() {
		return 0;
	}
	
	public default float getExtraFloat() {
		return 0;
	}
	
	public Point getFirstPoint();
	
	public default Point getSecondPoint() {
		return Point.ZERO;
	}
}
