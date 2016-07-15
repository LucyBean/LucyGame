package helpers;

import org.newdawn.slick.Image;

import worlds.ImageLibrary;

public class Rectangle {
	Point topLeft;
	float width;
	float height;
	Image image;

	public Rectangle(Point topLeft, float width, float height) {
		this.topLeft = topLeft;
		this.width = width;
		this.height = height;
	}

	public Point getTopLeft() {
		return topLeft;
	}

	public Point getTopRight() {
		return topLeft.move(Dir.EAST.asPoint().scale(width));
	}

	public Point getBottomLeft() {
		return topLeft.move(Dir.SOUTH.asPoint().scale(height));
	}

	public Point getBottomRight() {
		return getTopRight().move(Dir.SOUTH.asPoint().scale(height));
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public Image getImage() {
		if (image == null) {
			image = ImageLibrary.makeColliderImage(this);
		}
		return image;
	}

	/**
	 * @param p
	 *            The Point to test.
	 * @return Whether the Point is within the Rectangle.
	 */
	public boolean contains(Point p) {
		float x = p.getX();
		float y = p.getY();

		if (x < topLeft.getX() || x >= topLeft.getX() + width)
			return false;
		if (y < topLeft.getY() || y >= topLeft.getY() + height)
			return false;

		return true;
	}

	/**
	 * 
	 * @param r
	 *            The Rectangle to test.
	 * @return Whether the two Rectangles overlap.
	 */
	public boolean overlaps(Rectangle r) {
		// The rectangles overlap if one rectangle's corner is within the other. When the point is
		// on the boundary it will not be counted as contained.

		// Check if this.bottom-edge and r.top-edge overlap
		if (getBottomLeft().getY() != r.getTopLeft().getY()) {
			// Check if this.right-edge and r.left-edge overlap
			if (getTopRight().getX() != r.getTopLeft().getX())
				if (contains(r.getTopLeft()) || r.contains(getBottomLeft()))
					return true;
			// Check if this.left-edge and r.right-edge overlap
			if (getTopLeft().getX() != r.getTopRight().getX())
				if (contains(r.getTopRight()) || r.contains(getBottomLeft()))
					return true;
		}

		// Check if this.top-edge and r.bottom-edge overlap
		if (getTopLeft().getY() != r.getBottomLeft().getY()) {
			// Check if this.right-edge and r.left-edge overlap
			if (getTopRight().getX() != r.getTopLeft().getX())
				if (contains(r.getBottomLeft()) || r.contains(getTopRight()))
					return true;
			// Check if this.left-edge and r.right-edge overlap
			if (getTopLeft().getX() != r.getTopRight().getX())
				if (r.contains(getTopLeft()) || contains(r.getBottomRight()))
					return true;
		}

		// Final case for this overlap:
		// ___
		// | |
		// ___|___|___
		// | | | |
		// |___|___|___|
		// | |
		// |___|
		if (getTopLeft().getX() < r.getTopLeft().getX()
				&& getTopLeft().getY() > r.getTopLeft().getY()
				&& getBottomRight().getX() > r.getBottomRight().getX()
				&& getBottomRight().getY() < r.getBottomRight().getY())
			return true;
		if (r.getTopLeft().getX() < getTopLeft().getX()
				&& r.getTopLeft().getY() > getTopLeft().getY()
				&& r.getBottomRight().getX() > getBottomRight().getX()
				&& r.getBottomRight().getY() < getBottomRight().getY())
			return true;

		return false;
	}

	/**
	 * Translates the rectangle according to an offset.
	 * 
	 * @param offset
	 */
	public Rectangle translate(Point offset) {
		return new Rectangle(topLeft.move(offset), width, height);
	}

	@Override
	public String toString() {
		return "[" + topLeft + " " + width + " " + height + "]";
	}
}
