package objects.images;

import helpers.Point;
import helpers.Rectangle;
import objects.CoOrdTranslator;
import objects.attachments.Attachment;

public abstract class Sprite extends Attachment {
	private int gridSize;
	private float drawScale = 1.0f;

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
		if (r != null) {
			super.setRectangle(r.scale(1.0f / gridSize));
		}
	}

	public void draw() {
		if (getObject() != null) {
			if (getImage() != null) {
				CoOrdTranslator cot = getObject().getCoOrdTranslator();
				Point imageCoOrds = cot.objectToScreenCoOrds(getTopLeft());
				getImage().draw(imageCoOrds.getX(), imageCoOrds.getY(),
						cot.getDrawScale() * drawScale);
			}
		} else {
			System.err.println("Object is null for " + this);
		}
	}

	/**
	 * Changes the draw scale of this object. This changes the size that the
	 * images will be drawn. It does not affect the position of the origin.
	 * 
	 * @param scale
	 */
	public void setDrawScale(float scale) {
		drawScale = scale;
	}

	/**
	 * Must be called to update animated sprites.
	 * 
	 * @param delta
	 */
	public abstract void update(int delta);

	/**
	 * Sets the image to be mirrored. This will set the alpha correctly after
	 * mirroring.
	 * 
	 * @param mirrored
	 */
	public void setMirrored(boolean mirrored) {
		mirrorImage(mirrored);
		setAlpha(getAlpha());
	}

	/**
	 * Returns the grid size of the image. Can be used to convert from image to
	 * object co-ordinates.
	 * 
	 * @return
	 */
	protected int getGridSize() {
		return gridSize;
	}

	/**
	 * Sets the imaged to be mirrored.
	 * 
	 * @param mirrored
	 */
	protected abstract void mirrorImage(boolean mirrored);

	public abstract void setAlpha(float alpha);

	public abstract float getAlpha();
}
