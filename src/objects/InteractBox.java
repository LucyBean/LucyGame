package objects;

import org.newdawn.slick.Image;

import helpers.Point;
import helpers.Rectangle;
import objectLibs.SpriteLibrary;

public class InteractBox extends WOAttachment {
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
		super(topLeft, width, height);
	}

	/**
	 * Creates a new InteractBox using the specified Rectangle.
	 * 
	 * @param rect
	 */
	public InteractBox(Rectangle rect) {
		super(rect);
	}

	public Image getImage() {
		if (sprite == null) {
			sprite = SpriteLibrary.makeInteractBoxImage(rect);
		}

		return sprite.getImage();
	}
}
