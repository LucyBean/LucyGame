package helpers;

import org.newdawn.slick.Image;

import objectLibs.SpriteLibrary;
import objects.Sprite;

public class Rectangle {
	Point topLeft;
	float width;
	float height;
	Sprite sprite;

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
		if (sprite == null) {
			sprite = SpriteLibrary.makeColliderImage(this);
		}
		return sprite.getImage();
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
	 * Detects whether two Rectangles overlap. Shared boundaries will NOT count as an overlap.
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

		// Checks for '+' and 'T' overlap
		if (getTopLeft().getY() <= r.getTopLeft().getY() && getBottomLeft().getY() >= r.getBottomLeft().getY())
			if (getTopLeft().getX() >= r.getTopLeft().getX() && getTopRight().getX() <= r.getTopRight().getX())
				return true;
		if (r.getTopLeft().getY() <= getTopLeft().getY() && r.getBottomLeft().getY() >= getBottomLeft().getY())
			if (r.getTopLeft().getX() >= getTopLeft().getX() && r.getTopRight().getX() <= getTopRight().getX())
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

	/**
	 * Scales the rectangle about the origin. This will affect the origin AND width/height.
	 * 
	 * @param scale
	 * @return
	 */
	public Rectangle scaleAboutOrigin(float scale) {
		return new Rectangle(topLeft.scale(scale), width*scale, height*scale);
	}

	@Override
	public String toString() {
		return "[" + topLeft + " " + width + " " + height + "]";
	}
}
