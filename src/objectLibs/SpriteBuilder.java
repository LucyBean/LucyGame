package objectLibs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import helpers.Point;
import helpers.Rectangle;
import objects.LayeredImage;
import objects.Sprite;
import options.GlobalOptions;

/**
 * Class for holding and loading image sprites.
 * 
 * @author Lucy
 *
 */
public class SpriteBuilder {
	private final static int GRID_SIZE = GlobalOptions.GRID_SIZE;

	private static RectangleSpriteStore colliderImages = new RectangleSpriteStore();
	private static RectangleSpriteStore interactBoxImages = new RectangleSpriteStore();

	private static Sprite DOOR;

	private static SpriteSheet keysAndLocks;

	public static void initSpriteSheets() throws SlickException {
		keysAndLocks = new SpriteSheet("data/keys.png", 32, 32);
	}

	/**
	 * Creates a new rectangular sprite of the given colour.
	 * 
	 * @param r
	 * @param scale
	 * @param fill
	 * @param border
	 * @return
	 */
	public static Sprite createRectangle(Rectangle r, int scale, Color fill,
			Color border) {
		try {
			int width = (int) (r.getWidth() * scale);
			int height = (int) (r.getHeight() * scale);
			Image img = new Image(width, height);
			Graphics g = img.getGraphics();
			g.setColor(fill);
			g.fillRect(0, 0, width, height);
			g.setColor(border);
			g.drawRect(0, 0, width - 1, height - 1);
			g.drawRect(1, 1, width - 3, height - 3);
			g.flush();

			return new Sprite(img, r.getTopLeft(), scale);
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
		// check if a collider of this size has been made already
		int w = (int) (collider.getWidth() * GRID_SIZE);
		int h = (int) (collider.getHeight() * GRID_SIZE);
		Sprite s = colliderImages.get(w, h);

		if (s == null) {
			// Create a new image
			Color fill = new Color(50, 135, 220, 130);
			Color border = fill.darker(0.5f);
			border.a = 220;
			s = createRectangle(collider, GRID_SIZE, fill, border);
			colliderImages.store(s);
		}

		return s;
	}

	/**
	 * Creates a new image for an interact box for the given Rectangle.
	 * 
	 * @param interactBox
	 * @return
	 */
	public static Sprite makeInteractBoxImage(Rectangle interactBox) {
		// check if a collider of this size has been made already
		int w = (int) (interactBox.getWidth() * GRID_SIZE);
		int h = (int) (interactBox.getHeight() * GRID_SIZE);
		Sprite s = interactBoxImages.get(w, h);

		if (s == null) {

			Color fill = new Color(50, 220, 135, 130);
			Color border = fill.darker(0.5f);
			border.a = 220;
			s = createRectangle(interactBox, GRID_SIZE, fill, border);
			interactBoxImages.store(s);
		}

		return s;
	}

	public static Sprite drawWall(int width, int height) {
		return new Sprite(WALL.getImage(), Point.ZERO, width, height,
				GRID_SIZE);
	}

	public static Sprite interfaceElement(Image background,
			String text) {
		LayeredImage limg = new LayeredImage(background);
		limg.addLayers(1);
		limg.setTextCentered(1, text);

		return new Sprite(limg, Point.ZERO, 1);
	}

	public static Sprite interfaceElement(int width, int height) {
		return new Sprite(new LayeredImage(width, height, 2), Point.ZERO, 1);
	}

	public static Sprite getKeyImg(int keyID) {
		if (keyID <= 0 || keyID > 4) {
			keyID = 1;
		}
		Image img = keysAndLocks.getSubImage(0, keyID - 1);
		return new Sprite(img, Point.ZERO, GRID_SIZE);
	}

	public static Sprite getLockImg(int keyID) {
		if (keyID <= 0 || keyID > 4) {
			keyID = 1;
		}
		Image img = keysAndLocks.getSubImage(1,  keyID - 1);
		return new Sprite(img, Point.ZERO, GRID_SIZE);
	}

	public static Sprite getDoorImg() {
		if (DOOR == null) {
			LayeredImage limg = new LayeredImage(32, 64, 2);
			limg.fillLayer(0, new Color(20, 20, 100));
			limg.setTextCentered(1, "D");

			DOOR = new Sprite(limg, Point.ZERO, GRID_SIZE);
		}

		return DOOR;
	}

	private final static Sprite WALL = createRectangle(
			new Rectangle(Point.ZERO, 1, 1), GRID_SIZE,
			new Color(240, 240, 40));
	public final static Sprite PLAYER = createRectangle(
			new Rectangle(Point.ZERO, 0.8f, 1.6f), GRID_SIZE,
			new Color(240, 40, 240));
	public final static Sprite BUTTON = createRectangle(
			new Rectangle(Point.ZERO, 6, 1), 1, new Color(230, 130, 230));
	public final static Sprite HIDDEN_SQUARE = createRectangle(
			new Rectangle(Point.ZERO, 2, 2), GRID_SIZE,
			new Color(190, 60, 190));
	public final static Sprite COLLIDER = createRectangle(
			new Rectangle(Point.ZERO, 1, 1), GRID_SIZE,
			new Color(50, 135, 220, 130));
	public final static Sprite PICK_UP_ITEM = createRectangle(
			new Rectangle(Point.ZERO, 1, 1), GRID_SIZE, new Color(80, 250, 80));
}
