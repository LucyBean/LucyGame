package images;

import org.newdawn.slick.Image;

import helpers.Point;
import options.GlobalOptions;

public class PositionedImage {
	final Point origin;
	Image image;

	public PositionedImage(Point origin, Image image) {
		this.origin = origin;
		this.image = image;

		if (GlobalOptions.debug() && (origin.getX() < 0 || origin.getY() < 0)) {
			System.err.println(
					"Warning: Trying to create a positioned image with "
							+ "an origin with negative x- or y-co-ordinate.");
		}
	}

	/**
	 * Returns the width of the Positioned Image (the width of the Image plus
	 * the x-co-ordinate of the origin.)
	 * 
	 * @return
	 */
	public float getWidth() {
		return image.getWidth() + origin.getX();
	}
	
	public float getHeight() {
		return image.getHeight() + origin.getY();
	}

	public Point getOrigin() {
		return origin;
	}

	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	@Override
	public String toString() {
		return "PositionedImage " + image.getWidth() + "x" + image.getHeight() + " at " + origin;
	}
}
