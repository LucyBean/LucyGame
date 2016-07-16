package objects;

import org.newdawn.slick.Image;

import helpers.Point;
import helpers.Rectangle;

/**
 * Sprite class for displaying objects on the world. Can handle static images or
 * animations.
 * 
 * @author Lucy
 *
 */
public class Sprite {
	Image[] image;
	Rectangle boundingRectangle;

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
		boundingRectangle = new Rectangle(origin, img.getWidth(), img.getHeight());
	}

	public Sprite(Image img) {
		this(img, Point.ZERO);
	}

	public Point getOrigin() {
		return boundingRectangle.getTopLeft();
	}
	
	public Image getImage() {
		return image[0];
	}
	
	public Rectangle getBoundingRectangle() {
		return boundingRectangle;
	}
	
	// TODO
	// Make this check if the Image is visible.
	public boolean isVisible() {
		return true;
	}
}
