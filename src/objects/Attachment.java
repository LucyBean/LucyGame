package objects;

import helpers.Point;
import helpers.Rectangle;
import images.LayeredImage;
import worlds.World;

public abstract class Attachment {
	private Rectangle rect;
	private World world;
	private CoOrdTranslator cot;
	private GameObject myObject;

	public Attachment(Point topLeft, float width, float height,
			GameObject myObject) {
		this(new Rectangle(topLeft, width, height), myObject);
	}

	public Attachment(Rectangle rect, GameObject myObject) {
		this.rect = rect;
		if (myObject != null) {
			this.myObject = myObject;
			cot = myObject.getCoOrdTranslator();
		}
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

	public void draw(CoOrdTranslator cot) {
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

	public World getWorld() {
		return world;
	}

	public void setWorld(World w) {
		world = w;
	}

	public void setObject(GameObject go) {
		myObject = go;
		setWorld(go.getWorld());
		cot = go.getCoOrdTranslator();
	}

	/**
	 * Gets the GameObject to which this Attachment is attached.
	 * 
	 * @return
	 */
	protected GameObject getObject() {
		return myObject;
	}

	protected CoOrdTranslator getCoOrdTranslator() {
		return cot;
	}

	@Override
	public String toString() {
		return rect.toString();
	}
}
