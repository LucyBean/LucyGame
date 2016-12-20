package objects.attachments;

import helpers.Point;
import helpers.Rectangle;
import objects.GameObject;
import objects.images.ImageBuilder;
import objects.images.LayeredImage;

public class InteractBox extends Attachment {
	private LayeredImage image;
	
	/**
	 * Creates a new InteractBox to be attached to a GameObject. The GameObject should be
	 * set later using the setObject method.
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
	
	public InteractBox(Point topLeft, float width, float height, GameObject myObject) {
		this(new Rectangle(topLeft, width, height), myObject);
	}

	/**
	 * Creates a new InteractBox using the specified Rectangle. The GameObject
	 * should be set later using the setObject method.
	 * 
	 * @param rect
	 */
	public InteractBox(Rectangle rect) {
		this(rect, null);
	}
	
	public InteractBox(Rectangle rect, GameObject myObject) {
		super(rect, myObject);
	}

	@Override
	public LayeredImage getImage() {
		if (image == null) {
			image = ImageBuilder.makeInteractBoxImage(getRectangle());
		}

		return image;
	}
}
