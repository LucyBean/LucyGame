package objects;

import helpers.Point;
import helpers.Rectangle;
import images.ImageBuilder;
import images.LayeredImage;

public class Collider extends Attachment {
	private LayeredImage image;
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
		super(topLeft, width, height);
	}
	
	/**
	 * Creates a new Collider using the specified Rectangle.
	 * 
	 * @param rect
	 */
	public Collider(Rectangle rect) {
		super(rect);
	}
	
	@Override
	public LayeredImage getImage()  {
		if (image == null) {
			image = ImageBuilder.makeColliderImage(getRectangle());
		}

		return image;
	}
}
