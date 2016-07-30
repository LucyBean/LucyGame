package objects;

import org.newdawn.slick.Image;

import helpers.Point;
import helpers.Rectangle;

/**
 * Sprite class for displaying objects on the world. Can handle static images or
 * animations.
 * 
 * @author Lucy
 *
 */
public class Sprite {
	Image[] image;
	Rectangle boundingRectangle;
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
	 */
	public Sprite(Image img, Point origin, int tileX, int tileY, int gridSize) {
		image = new Image[1];
		image[0] = img;
		boundingRectangle = new Rectangle(origin,
				((float) img.getWidth()) / gridSize,
				((float) img.getHeight()) / gridSize);
		this.tileX = tileX;
		this.tileY = tileY;
	}

	public Sprite(Image img, int gridSize) {
		this(img, Point.ZERO, 1, 1, gridSize);
	}

	public Point getOrigin() {
		return boundingRectangle.getTopLeft();
	}
	
	public Image getImage() {
		return image[0];
	}

//	public void draw(Point position, float scale) {
//		for (int y = 0; y < tileY; y++) {
//			for (int x = 0; x < tileX; x++) {
//				getImage().draw(
//						imageCoOrds.getX() + offset.getWidth() * x,
//						imageCoOrds.getY() + offset.getHeight() * y,
//						getDrawScale(camera));
//			}
//		}
//	}

	public Rectangle getBoundingRectangle() {
		return boundingRectangle;
	}
}
