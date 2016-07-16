package objects;

import org.newdawn.slick.Image;

import helpers.Point;
import helpers.Rectangle;
import objectLibs.SpriteLibrary;

public class Collider {
	Sprite sprite;
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
		if (sprite == null) {
			sprite = SpriteLibrary.makeColliderImage(rect);
		}

		return sprite.getImage();
	}

	public Point getTopLeft() {
		return rect.getTopLeft();
	}
	
	public Point getTopRight() {
		return rect.getTopRight();
	}
	
	public Point getBottomLeft() {
		return rect.getBottomLeft();
	}
	
	public Point getBottomRight() {
		return rect.getBottomRight();
	}
	
	public float getWidth() {
		return rect.getWidth();
	}
	
	public float getHeight() {
		return rect.getHeight();
	}
	
	public Rectangle getRectangle() {
		return rect;
	}
	
	@Override
	public String toString() {
		return rect.toString();
	}
}
