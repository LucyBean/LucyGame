package objects.attachments;

import helpers.Point;
import helpers.Rectangle;
import objects.CoOrdTranslator;
import objects.GameObject;
import objects.images.LayeredImage;

public abstract class Attachment {
	private Rectangle rect;
	private GameObject myObject;

	public Attachment(Point topLeft, float width, float height,
			GameObject myObject) {
		this(new Rectangle(topLeft, width, height), myObject);
	}
	
	public Attachment(Point topLeft, float width, float height) {
		this(topLeft, width, height, null);
	}

	public Attachment(Rectangle rect, GameObject myObject) {
		this.rect = rect;
		if (myObject != null) {
			this.myObject = myObject;
		}
	}
	
	public Attachment(Rectangle rect) {
		this(rect, null);
	}

	protected Attachment(GameObject myObject) {
		this(null, myObject);
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

	public void draw() {
		CoOrdTranslator cot = getObject().getCoOrdTranslator();
		Point newOrigin = cot.objectToScreenCoOrds(rect.getTopLeft());
		float drawScale = cot.getDrawScale();
		getImage().draw(newOrigin.getX(), newOrigin.getY(), drawScale);
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

	public void setObject(GameObject go) {
		myObject = go;
	}

	/**
	 * Gets the GameObject to which this Attachment is attached.
	 * 
	 * @return
	 */
	protected GameObject getObject() {
		return myObject;
	}
	
	@Override
	public String toString() {
		return rect.toString();
	}
}
