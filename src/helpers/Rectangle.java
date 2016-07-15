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
	 *            The point to test.
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
}
