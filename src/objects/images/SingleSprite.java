package objects.images;

import helpers.Point;
import helpers.Rectangle;

/**
 * Sprite class for displaying objects on the world. Can handle a single,
 * untiled image.
 * 
 * @author Lucy
 *
 */
public class SingleSprite extends Sprite {
	private LayeredImage image;
	private float alpha = 1.0f;

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
	public SingleSprite(LayeredImage img, Point origin, int gridSize) {
		super(img.getRectangle().translate(origin), gridSize);
		image = img;
	}

	public SingleSprite(LayeredImage img, int gridSize) {
		this(img, Point.ZERO, gridSize);
	}

	/**
	 * Creates a new SingleSprite with no image, but whose bounds are specified
	 * by the given rectangle.
	 * 
	 * @param r
	 * @param gridSize
	 */
	public SingleSprite(Rectangle r, int gridSize) {
		super(r, gridSize);
	}

	@Override
	public void mirrorImage(boolean mirrored) {
		image.setMirrored(mirrored);
	}

	/**
	 * Must be called to update animated sprites.
	 * 
	 * @param delta
	 */
	public void update(int delta) {
		if (image != null) {
			image.update(delta);
		}
	}

	@Override
	public LayeredImage getImage() {
		return image;
	}

	public void setImage(LayeredImage limg, Point origin) {
		image = limg;
		if (image != null) {
			setRectangle(image.getRectangle().translate(origin));
		}
	}

	@Override
	public void setAlpha(float alpha) {
		this.alpha = alpha;
		image.setAlpha(alpha);
	}
	
	@Override
	public float getAlpha() {
		return alpha;
	}
}
