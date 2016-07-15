package worlds;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Point;
import helpers.Rectangle;

/**
 * Class for holding and loading image sprites.
 * 
 * @author Lucy
 *
 */
public class ImageLibrary {
	public static Image createRectangle(Rectangle r, Color fill, Color border) {
		try {
			int width = (int) r.getWidth();
			int height = (int) r.getHeight();
			Image img = new Image(width, height);
			Graphics g = img.getGraphics();
			g.setColor(fill);
			g.fillRect(0, 0, width-1, height-1);
			g.setColor(border);
			g.drawRect(0, 0, width-1, height-1);
			g.drawRect(1, 1, width-3, height-3);
			g.flush();

			return img;
		} catch (SlickException se) {
			System.err.println("Unable to draw rectangle " + r);
			se.printStackTrace();
			return null;
		}
	}
	
	public static Image createRectangle(Rectangle r, Color fill) {
		return createRectangle(r, fill, fill.darker(0.5f));
	}

	/**
	 * Creates a new image for a collider for the given Rectangle.
	 * 
	 * @param collider
	 * @return
	 */
	public static Image makeColliderImage(Rectangle collider) {
		Color fill = new Color(50, 135, 220, 130);
		Color border = fill.darker(0.5f);
		border.a = 220;
		return createRectangle(collider, fill, border);
	}

	/**
	 * Creates a new image for an interact box for the given Rectangle.
	 * 
	 * @param interactBox
	 * @return
	 */
	public static Image makeInteractBoxImage(Rectangle interactBox) {
		Color fill = new Color(50, 220, 135, 130);
		Color border = fill.darker(0.5f);
		border.a = 220;
		return createRectangle(interactBox, fill, border);
	}
	
	public final static Image WALL_IMAGE = createRectangle(new Rectangle(Point.ZERO, 50, 50), new Color(240, 240, 40));
	public final static Image PLAYER_IMAGE = createRectangle(new Rectangle(Point.ZERO, 40, 80), new Color(240, 40, 240));
}
