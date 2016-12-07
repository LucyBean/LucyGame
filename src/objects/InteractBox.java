package objects;

import helpers.Point;
import helpers.Rectangle;
import images.ImageBuilder;
import images.LayeredImage;

public class InteractBox extends Attachment {
	private LayeredImage image;
	
	/**
	 * Creates a new InteractBox to be attached to a GameObject.
	 * 
	 * @param topLeft
	 *            Co-ordinates of the InteractBox relative to the GameObject's
	 *            origin.
	 * @param width
	 *            Width of the InteractBox.
	 * @param height
	 *            Height of the InteractBox.
	 */
	public InteractBox(Point topLeft, float width, float height) {
		this(new Rectangle(topLeft, width, height));
	}

	/**
	 * Creates a new InteractBox using the specified Rectangle.
	 * 
	 * @param rect
	 */
	public InteractBox(Rectangle rect) {
		super(rect);
	}

	@Override
	public LayeredImage getImage() {
		if (image == null) {
			image = ImageBuilder.makeInteractBoxImage(getRectangle());
		}

		return image;
	}
}
