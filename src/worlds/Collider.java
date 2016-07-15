package worlds;

import org.newdawn.slick.Image;

import helpers.Point;
import helpers.Rectangle;

public class Collider {
	Image image;
	Rectangle rect;

	/**
	 * Creates a new Collider object.
	 * 
	 * @param topLeft
	 *            Co-ordinates of the Collider relative to the GameObject's
	 *            origin.
	 * @param width
	 *            Width of the Collider.
	 * @param height
	 *            Height of the Collider.
	 */
	public Collider(Point topLeft, float width, float height) {
		rect = new Rectangle(topLeft, width, height);
	}
	
	/**
	 * Creates a new Collider using the specified Rectangle.
	 * 
	 * @param rect
	 */
	public Collider(Rectangle rect) {
		this.rect = rect;
	}

	public Image getImage() {
		if (image == null) {
			image = ImageLibrary.makeColliderImage(rect);
		}

		return image;
	}

	public Point getTopLeft() {
		return rect.getTopLeft();
	}
}
