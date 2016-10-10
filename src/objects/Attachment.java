package objects;

import helpers.Point;
import helpers.Rectangle;
import images.LayeredImage;

public abstract class Attachment {
	private Rectangle rect;
	
	public Attachment(Point topLeft, float width, float height) {
		rect = new Rectangle(topLeft, width, height);
	}
	
	public Attachment(Rectangle rect) {
		this.rect = rect;
	}
	
	protected Attachment() {
		
	}
	
	protected void setRectangle(Rectangle rect) {
		this.rect = rect;
	}
	
	public void setOrigin(Point newOrigin) {
		rect = new Rectangle(newOrigin, rect.getWidth(), rect.getHeight());
	}

	public abstract LayeredImage getImage();
	
	public void draw(CoOrdTranslator cot) {
		Point newOrigin = cot.objectToScreenCoOrds(rect.getTopLeft());
		float drawScale = cot.getDrawScale();
		getImage().draw(newOrigin.getX(), newOrigin.getY(),drawScale);
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
