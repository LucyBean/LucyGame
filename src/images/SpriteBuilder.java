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

	/**
	 * Creates a new rectangular sprite of the given colour.
	 * 
	 * @param r
	 * @param scale
	 * @param fill
	 * @param border
	 * @return
	 */
	public static SingleSprite createRectangle(Rectangle r, int scale, Color fill,
			Color border) {
		int width = (int) (r.getWidth() * scale);
		int height = (int) (r.getHeight() * scale);
		LucyImage img = ImageBuilder.makeRectangle(width, height, fill, border);
		return new SingleSprite(img, r.getTopLeft(), scale);
	}

	public static SingleSprite createRectangle(Rectangle r, int scale, Color fill) {
		return createRectangle(r, scale, fill, fill.darker(0.5f));
	}

	public static SingleSprite drawWall(int width, int height) {
		LucyImage img = ImageBuilder.getItemImage(ItemType.WALL);
		return new SingleSprite(img, Point.ZERO, width, height, GRID_SIZE);
	}

	public static SingleSprite interfaceElement(LucyImage background, String text) {
		LayeredImage limg = new LayeredImage(background);
		limg.addLayers(1);
		limg.setTextCentered(1, text);

		return new SingleSprite(limg, Point.ZERO, 1);
	}

	public static SingleSprite interfaceElement(int width, int height) {
		return new SingleSprite(new LayeredImage(width, height, 2), Point.ZERO, 1);
	}

	private static SingleSprite getDoorSprite() {
		LayeredImage limg = new LayeredImage(32, 64, 2);
		LucyImage top = ImageBuilder.getItemImage(ItemType.DOOR_TOP);
		LucyImage btm = ImageBuilder.getItemImage(ItemType.DOOR_BTM);
		limg.setLayer(0, top);
		limg.setLayer(1, new PositionedImage(new Point(0, 32), btm));

		return new SingleSprite(limg, GRID_SIZE);
	}

	/**
	 * Returns the character sprite for the given ID. This will have an origin
	 * of (0,0).
	 * 
	 * @param id
	 * @return
	 */
	public static SingleSprite getCharacterSprite(int id) {
		return new SingleSprite(ImageBuilder.getCharacterImg(id), GRID_SIZE);
	}

	public static SingleSprite getWorldItem(ItemType it) {
		if (it == ItemType.DOOR_TOP) {
			return getDoorSprite();
		}
		if (it.isPaintable()) {
			LucyImage img = ImageBuilder.getItemImage(it);
			return new SingleSprite(img, Point.ZERO, GRID_SIZE);
		} else {
			return getWorldItem(it.getParent());
		}
	}

	public static SingleSprite makeMenuButton(String text) {
		LucyImage bg = ImageBuilder.getMenuButtonBackground();
		LayeredImage limg = new LayeredImage(bg);
		limg.addLayers(1);
		limg.setTextCentered(limg.getTopLayerNumber(), text);

		return new SingleSprite(limg, 1);
	}

	public static SingleSprite makePaletteBlock() {
		LucyImage bg = ImageBuilder.getPaletteBlockBackground();
		LayeredImage limg = new LayeredImage(bg);
		limg.addLayers(1);

		return new SingleSprite(limg, 1);
	}
}
