package objects.images;

import helpers.Point;
import helpers.Rectangle;
import objects.CoOrdTranslator;
import objects.attachments.Attachment;

public abstract class Sprite extends Attachment {
	private int gridSize;

	/**
	 * 
	 * 
	 * @param img
	 * 
	 * @param origin
	 * 
	 */
	/**
	 * Creates a new Sprite object.
	 * 
	 * @param img
	 *            Image to be drawn.
	 * @param origin
	 *            Position of the top-left point of the image to be drawn in
	 *            object co-ordinates.
	 * @param tileX
	 *            Amount of times to repeat the image in x direction.
	 * @param tileY
	 *            Amount of times to repeat the image in y direction.
	 * @param gridSize
	 *            Size of the image grid compared to the object co-ordinate
	 *            grid. Use 1 for interface objects and GRID_SIZE for world
	 *            objects.
	 * @param attachment
	 *            The GameObject to which the Sprite is attached. This is
	 *            required for drawing.
	 */
	public Sprite(Rectangle boundingRectangle, int gridSize) {
		this.gridSize = gridSize;
		setRectangle(boundingRectangle);
	}

	@Override
	public void setRectangle(Rectangle r) {
		super.setRectangle(r.scale(1.0f / gridSize));
	}

	public void draw() {
		if (getObject() != null) {
			CoOrdTranslator cot = getObject().getCoOrdTranslator();
			Point imageCoOrds = cot.objectToScreenCoOrds(getTopLeft());
			getImage().draw(imageCoOrds.getX(), imageCoOrds.getY(),
					cot.getDrawScale());
		} else {
			System.err.println("Object is null for " + this);
		}
	}

	/**
	 * Must be called to update animated sprites.
	 * 
	 * @param delta
	 */
	public abstract void update(int delta);

	public abstract void setMirrored(boolean mirrored);
}
