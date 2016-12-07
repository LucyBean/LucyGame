package objects;

import helpers.Point;
import helpers.Rectangle;
import images.ImageBuilder;
import images.LayeredImage;

public class Collider extends Attachment {
	private LayeredImage image;
	private boolean solid = false;

	/**
	 * Creates a new Collider object. The GameObject should be set later using
	 * the setObject method.
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
		this(topLeft, width, height, null);
	}
	
	public Collider(Point topLeft, float width, float height, GameObject myObject) {
		this(new Rectangle(topLeft, width, height), myObject);
	}

	/**
	 * Creates a new Collider using the specified Rectangle. The GameObject
	 * should be set later using the setObject method.
	 * 
	 * @param rect
	 */
	public Collider(Rectangle rect) {
		this(rect, null);
	}
	
	public Collider(Rectangle rect, GameObject myObject) {
		super(rect, myObject);
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isSolid() {
		return solid;
	}

	@Override
	public LayeredImage getImage() {
		if (image == null) {
			image = ImageBuilder.makeColliderImage(getRectangle());
		}

		return image;
	}
}
