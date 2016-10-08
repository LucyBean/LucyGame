package images;

import org.newdawn.slick.Color;

import helpers.Point;
import helpers.Rectangle;
import objects.ItemType;
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
		int width = (int) (r.getWidth() * scale);
		int height = (int) (r.getHeight() * scale);
		LucyImage img = ImageBuilder.makeRectangle(width, height, fill, border);
		return new Sprite(img, r.getTopLeft(), scale);
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
		LucyImage img = ImageBuilder.getItemImage(ItemType.WALL);
		return new Sprite(img, Point.ZERO, width, height, GRID_SIZE);
	}

	public static Sprite interfaceElement(LucyImage background, String text) {
		LayeredImage limg = new LayeredImage(background);
		limg.addLayers(1);
		limg.setTextCentered(1, text);

		return new Sprite(limg, Point.ZERO, 1);
	}

	public static Sprite interfaceElement(int width, int height) {
		return new Sprite(new LayeredImage(width, height, 2), Point.ZERO, 1);
	}

	private static Sprite getDoorSprite() {
		LayeredImage limg = new LayeredImage(32, 64, 2);
		LucyImage top = ImageBuilder.getItemImage(ItemType.DOOR_TOP);
		LucyImage btm = ImageBuilder.getItemImage(ItemType.DOOR_BTM);
		limg.setLayer(0, top);
		limg.setLayer(1, new PositionedImage(new Point(0, 32), btm));

		return new Sprite(limg, GRID_SIZE);
	}

	public static Sprite getCharacterSprite(int id) {
		return new Sprite(ImageBuilder.getCharacterImg(id),
				new Point(-0.25f, -0.5f), GRID_SIZE);
	}

	public static Sprite getWorldItem(ItemType it) {
		if (it == ItemType.DOOR_TOP) {
			return getDoorSprite();
		}
		if (it.isPaintable()) {
			LucyImage img = ImageBuilder.getItemImage(it);
			return new Sprite(img, Point.ZERO, GRID_SIZE);
		} else {
			return getWorldItem(it.getParent());
		}
	}

	public static Sprite makeMenuButton(String text) {
		LucyImage bg = ImageBuilder.getMenuButtonBackground();
		LayeredImage limg = new LayeredImage(bg);
		limg.addLayers(1);
		limg.setTextCentered(limg.getTopLayerNumber(), text);

		return new Sprite(limg, 1);
	}

	public static Sprite makePaletteBlock() {
		LucyImage bg = ImageBuilder.getPaletteBlockBackground();
		LayeredImage limg = new LayeredImage(bg);
		limg.addLayers(1);

		return new Sprite(limg, 1);
	}
}
