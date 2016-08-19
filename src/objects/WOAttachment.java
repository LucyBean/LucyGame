package objects;

import helpers.Point;
import helpers.Rectangle;
import images.Sprite;

public abstract class WOAttachment {
	Sprite sprite;
	private Rectangle rect;
	
	public WOAttachment(Point topLeft, float width, float height) {
		rect = new Rectangle(topLeft, width, height);
	}
	
	public WOAttachment(Rectangle rect) {
		this.rect = rect;
	}

	protected abstract Sprite getSprite();
	
	public void draw(CoOrdTranslator cot) {
		getSprite().draw(cot);
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
