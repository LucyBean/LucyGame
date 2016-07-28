package objectLibs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Point;
import helpers.Rectangle;
import objects.Sprite;
import worlds.GlobalOptions;

/**
 * Class for holding and loading image sprites.
 * 
 * @author Lucy
 *
 */
public class SpriteLibrary {
	private final static int GRID_SIZE = GlobalOptions.GRID_SIZE;

	/**
	 * Creates a new rectangular sprite of the given colour.
	 * @param r 
	 * @param scale 
	 * @param fill
	 * @param border
	 * @return
	 */
	public static Sprite createRectangle(Rectangle r, int scale, Color fill,
			Color border) {
		try {
			int width = (int) r.getWidth() * scale;
			int height = (int) r.getHeight() * scale;
			Image img = new Image(width, height);
			Graphics g = img.getGraphics();
			g.setColor(fill);
			g.fillRect(0, 0, width - 1, height - 1);
			g.setColor(border);
			g.drawRect(0, 0, width - 1, height - 1);
			g.drawRect(1, 1, width - 3, height - 3);
			g.flush();

			return new Sprite(img);
		} catch (SlickException se) {
			System.err.println("Unable to draw rectangle " + r);
			se.printStackTrace();
			return null;
		}
	}

	public static Sprite createRectangle(Rectangle r, int scale, Color fill) {
		return createRectangle(r, scale, fill, fill.darker(0.5f));
	}

	/**
	 * Creates a new image for a collider for the given Rectangle.
	 * 
	 * @param collider
	 * @return
	 */
	public static Sprite makeColliderImage(Rectangle collider) {
		Color fill = new Color(50, 135, 220, 130);
		Color border = fill.darker(0.5f);
		border.a = 220;
		return createRectangle(collider, GRID_SIZE, fill, border);
	}

	/**
	 * Creates a new image for an interact box for the given Rectangle.
	 * 
	 * @param interactBox
	 * @return
	 */
	public static Sprite makeInteractBoxImage(Rectangle interactBox) {
		Color fill = new Color(50, 220, 135, 130);
		Color border = fill.darker(0.5f);
		border.a = 220;
		return createRectangle(interactBox, GRID_SIZE, fill, border);
	}

	public final static Sprite WALL = createRectangle(
			new Rectangle(Point.ZERO, 1, 1), GRID_SIZE,
			new Color(240, 240, 40));
	public final static Sprite PLAYER = createRectangle(
			new Rectangle(Point.ZERO, 1, 2), GRID_SIZE,
			new Color(240, 40, 240));
	public final static Sprite BUTTON = createRectangle(
			new Rectangle(Point.ZERO, 6, 1), 1, new Color(230, 130, 230));
	public final static Sprite HIDDEN_SQUARE = createRectangle(
			new Rectangle(Point.ZERO, 2, 2), GRID_SIZE,
			new Color(190, 60, 190));
}
