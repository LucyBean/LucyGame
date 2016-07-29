package objects;

import org.newdawn.slick.Image;

import helpers.Point;
import helpers.Rectangle;

public abstract class WOAttachment {
	Sprite sprite;
	Rectangle rect;
	
	public WOAttachment(Point topLeft, float width, float height) {
		rect = new Rectangle(topLeft, width, height);
	}
	
	public WOAttachment(Rectangle rect) {
		this.rect = rect;
	}

	public abstract Image getImage();

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
