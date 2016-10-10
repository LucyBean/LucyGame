package helpers;

/**
 * An enum to represent the directions available. Will be used heavily in
 * grid-based games for movement, interactions, etc.
 * 
 * @author Lucy
 *
 */
public enum Dir {
	NORTH(new Point(0, -1)), SOUTH(new Point(0, 1)), EAST(new Point(1, 0)), WEST(
			new Point(-1, 0));

	Point direction;

	Dir(Point direction) {
		this.direction = direction;
	}
	
	public Dir neg() {
		switch(this) {
			case NORTH:
				return SOUTH;
			case SOUTH:
				return NORTH;
			case EAST:
				return WEST;
			case WEST:
				return EAST;
		}
		return null;
	}

	/**
	 * @return The direction represented as a (floating-point) Point.
	 */
	public Point asPoint() {
		return direction;
	}
	
	public String toString() {
		return asPoint().toString();
	}
}
