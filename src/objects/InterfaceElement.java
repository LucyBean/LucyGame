package objects;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import worlds.Camera;

public abstract class InterfaceElement extends GameObject {

	public InterfaceElement(Point position, Sprite sprite) {
		super(position, sprite);
	}

	@Override
	public Point objectToScreenCoOrds(Point point, Camera camera) {
		return point.move(getPosition());
	}

	@Override
	public Rectangle objectToScreenCoOrds(Rectangle rect, Camera camera) {
		return rect.translate(getPosition());
	}

	/**
	 * Translates a Point to screen co-ordinates.
	 * 
	 * @param point
	 * @return
	 */
	public Point objectToScreenCoOrds(Point point) {
		// Since this is an InterfaceElement, a Camera is not required to
		// translate to screen co-ords.
		return objectToScreenCoOrds(point, null);
	}

	public Rectangle objectToScreenCoOrds(Rectangle rect) {
		// Since this is an InterfaceElement, a Camera is not required to
		// translate to screen co-ords.
		return objectToScreenCoOrds(rect, null);
	}

	@Override
	protected float getDrawScale(Camera camera) {
		return 1.0f;
	}

	public void update(GameContainer gc, int delta) {

	}

	public void mousePressed(int button, Point clickPoint) {
		Rectangle rect = getSprite().getBoundingRectangle();
		rect = objectToScreenCoOrds(rect, null);
		if (rect.contains(clickPoint)) {
			onClick(button);
		}
	}

	public abstract void onClick(int button);
}
