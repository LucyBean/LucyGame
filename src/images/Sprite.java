package images;

import helpers.Point;
import helpers.Rectangle;
import objects.CoOrdTranslator;

/**
 * Sprite class for displaying objects on the world. Can handle static images.
 * Will support animations in future.
 * 
 * @author Lucy
 *
 */
public class Sprite {
	LayeredImage image;
	Rectangle boundingRectangle;
	Rectangle imageBoundingRectangle;
	int tileX;
	int tileY;

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
	public Sprite(LayeredImage img, Point origin, int tileX, int tileY,
			int gridSize) {
		image = img;
		imageBoundingRectangle = new Rectangle(origin,
				((float) img.getWidth()) / gridSize,
				((float) img.getHeight()) / gridSize);
		boundingRectangle = imageBoundingRectangle.scale(tileX, tileY);
		this.tileX = tileX;
		this.tileY = tileY;
	}

	public Sprite(LayeredImage img, Point origin, int gridSize) {
		this(img, origin, 1, 1, gridSize);
	}
	
	public Sprite(LayeredImage img, int gridSize) {
		this(img, Point.ZERO, 1, 1, gridSize);
	}
	
	public Sprite(LucyImage img, Point origin, int tileX, int tileY, int gridSize) {
		this(new LayeredImage(img), origin, tileX, tileY, gridSize);
	}
	
	public Sprite(LucyImage img, Point origin, int gridSize) {
		this(img, origin, 1, 1, gridSize);
	}
	
	public Sprite(LucyImage img, int gridSize) {
		this(img, Point.ZERO, 1, 1, gridSize);
	}

	public Point getOrigin() {
		return boundingRectangle.getTopLeft();
	}

	public LayeredImage getImage() {
		return image;
	}

	public void draw(CoOrdTranslator cot) {
		Point imageCoOrds = cot.objectToScreenCoOrds(getOrigin());
		Rectangle offset = cot.objectToScreenCoOrds(imageBoundingRectangle);
		for (int y = 0; y < tileY; y++) {
			for (int x = 0; x < tileX; x++) {
				getImage().draw(imageCoOrds.getX() + offset.getWidth() * x,
						imageCoOrds.getY() + offset.getHeight() * y,
						cot.getDrawScale());
			}
		}
	}

	public Rectangle getBoundingRectangle() {
		return boundingRectangle;
	}
}
