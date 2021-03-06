package helpers;

public class Point {
	public static final Point ZERO = new Point(0, 0);

	final float x;
	final float y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Copies the Point.
	 * 
	 * @return A new Point with the same x- and y-components.
	 */
	public Point copy() {
		return new Point(x, y);
	}

	/**
	 * Adds the x- and y-components of offset to the x- and y-components of this
	 * point.
	 * 
	 * @param offset
	 *            The amount to move the point by, relative to the current
	 *            location
	 * @return The original point translated by the offset.
	 */
	public Point move(Point offset) {
		return new Point(x + offset.getX(), y + offset.getY());
	}

	/**
	 * Adds the x- and y-components of offset to the x- and y-components of this
	 * point.
	 * 
	 * @param x
	 *            Amount to move x-component of point by.
	 * @param y
	 *            Amount to move y-component of point by.
	 * @return The original point translated by the offset.
	 */
	public Point move(float x, float y) {
		return move(new Point(x, y));
	}

	/**
	 * Moves the point in the given direction by the given amount.
	 * 
	 * @param d
	 *            Direction in which to move the point.
	 * @param amount
	 *            Amount to move the point by.
	 * @return The moved point.
	 */
	public Point move(Dir d, float amount) {
		return move(d.asPoint().scale(amount));
	}

	/**
	 * Returns the negative of the point. If the point is (x,y), then (-x, -y)
	 * will be returned.
	 * 
	 * @return Negative of this point.
	 */
	public Point neg() {
		return new Point(-x, -y);
	}

	/**
	 * Uniformly scales the x- and y-components by the given scale.
	 * 
	 * @param s
	 *            Scale for x- and y-components.
	 * @return Scaled point.
	 */
	public Point scale(float s) {
		return new Point(s * x, s * y);
	}

	public Point scale(float sx, float sy) {
		return new Point(sx * x, sy * y);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public float getDir(Dir d) {
		switch(d) {
			case EAST:
				return getX();
			case NORTH:
				return -getY();
			case SOUTH:
				return getY();
			case WEST:
				return -getX();
			default:
				return 0.0f;
			
		}
	}

	@Override
	public String toString() {
		return "(" + String.format("%.2f", getX()) + ","
				+ String.format("%.2f", getY()) + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o == null) {
			return false;
		}
		
		if (o instanceof Point) {
			Point p = (Point) o;
			return (getX() == p.getX() && getY() == p.getY());
		}
		
		return false;
	}
}
