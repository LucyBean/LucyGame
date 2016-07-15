package worlds;

import org.newdawn.slick.Image;

import helpers.Point;

/**
 * Sprite class for displaying objects on the world. Can handle static images or
 * animations.
 * 
 * @author Lucy
 *
 */
public class Sprite {
	Point origin;
	Image[] image;

	/**
	 * Creates a new Sprite object.
	 * 
	 * @param img
	 *            Image to be drawn.
	 * @param origin
	 *            Position of the top-left point of the image to be drawn. When
	 *            attaching to a GameObject, this should be relative to the
	 *            GameObject's center.
	 */
	public Sprite(Image img, Point origin) {
		image = new Image[1];
		image[0] = img;
		this.origin = origin;
	}

	public Sprite(Image img) {
		this(img, Point.ZERO);
	}

	public Point getOrigin() {
		return origin;
	}
	
	public Image getImage() {
		return image[0];
	}
}
