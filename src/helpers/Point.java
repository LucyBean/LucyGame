package helpers;


public class Point {
	public static final Point ZERO = new Point(0,0);
	
	float x;
	float y;
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Adds the x- and y-components of offset to the x- and y-components
	 * of this point.
	 * 
	 * @param offset The amount to move the point by, relative to the current location
	 * @return The original point translated by the offset.
	 */
	public Point move(Point offset) {
		return new Point(x + offset.getX(), y + offset.getY());
	}
	
	/**
	 * Adds the x- and y-components of offset to the x- and y-components
	 * of this point.
	 * 
	 * @param x Amount to move x-component of point by.
	 * @param y Amount to move y-component of point by.
	 * @return The original point translated by the offset.
	 */
	public Point move(float x, float y) {
		return move(new Point(x,y));
	}
	
	/**
	 * Moves the point in the given direction by the given amount.
	 * 
	 * @param d Direction in which to move the point.
	 * @param amount Amount to move the point by.
	 * @return The moved point.
	 */
	public Point move(Dir d, float amount) {
		return move(d.asPoint().scale(amount));
	}
	
	/**
	 * Returns the negative of the point. If the point is (x,y), then (-x, -y) will be returned.
	 * 
	 * @return Negative of this point.
	 */
	public Point neg() {
		return new Point(-x, -y);
	}
	
	/**
	 * Uniformly scales the x- and y-components by the given scale.
	 * 
	 * @param s Scale for x- and y-components.
	 * @return Scaled point.
	 */
	public Point scale(float s) {
		return new Point(s*x, s*y);
	}
	
	public Point scale(float sx, float sy) {
		return new Point(sx*x, sy*y);
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "(" + getX() + "," + getY() + ")";
	}
}