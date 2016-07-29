package objects;

import org.newdawn.slick.Image;

import helpers.Point;
import helpers.Rectangle;
import objectLibs.SpriteLibrary;

public class Collider extends WOAttachment {
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
	
	public Image getImage()  {
		if (sprite == null) {
			sprite = SpriteLibrary.makeColliderImage(rect);
		}

		return sprite.getImage();
	}
}
