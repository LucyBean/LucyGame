package objects.images;

import helpers.Point;
import helpers.Rectangle;

public class PositionedImage {
	Point origin;
	LucyImage image;

	public PositionedImage(Point origin, LucyImage image) {
		this.origin = origin;
		this.image = image;
	}

	/**
	 * Returns the width of the Positioned Image (the width of the Image plus
	 * the x-co-ordinate of the origin.)
	 * 
	 * @return
	 */
	public float getWidth() {
		if (image != null && origin != null) {
			return image.getWidth() + origin.getX();
		} else {
			return 0;
		}
	}

	/**
	 * Returns the height of the Positioned Image (the height of the Image plus
	 * the y-co-ordinate of the origin.)
	 * 
	 * @return
	 */
	public float getHeight() {
		if (image != null && origin != null) {
			return image.getHeight() + origin.getY();
		} else {
			return 0;
		}
	}

	public Point getOrigin() {
		return origin;
	}

	/**
	 * Gets the Rectangle that describes the position and size of the contained
	 * LucyImage.
	 * 
	 * @return
	 */
	public Rectangle getRectangle() {
		return new Rectangle(getOrigin(), image.getWidth(), image.getHeight());
	}

	public LucyImage getImage() {
		return image;
	}

	public void setImage(LucyImage image) {
		this.image = image;
	}

	@Override
	public String toString() {
		if (image != null) {
			return "PositionedImage " + image.getWidth() + "x"
					+ image.getHeight() + " at " + origin;
		} else {
			return "PositionedImage at " + origin;
		}
	}

	public void setPosition(Point p) {
		origin = p;
	}

	public void setAlpha(float alpha) {
		if (getImage() != null) {
			getImage().setAlpha(alpha);
		}
	}
}
