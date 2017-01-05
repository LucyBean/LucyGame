package objects.images;

import org.newdawn.slick.Color;

import helpers.Point;
import helpers.Rectangle;
import objects.world.ItemType;
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
	public static Sprite createRectangle(Rectangle r, int scale, Color fill,
			Color border) {
		int width = (int) (r.getWidth() * scale);
		int height = (int) (r.getHeight() * scale);
		LucyImage img = ImageBuilder.makeRectangle(width, height, fill, border);
		return new SingleSprite(new LayeredImage(img), r.getTopLeft(), scale);
	}

	public static Sprite createRectangle(Rectangle r, int scale, Color fill) {
		return createRectangle(r, scale, fill, fill.darker(0.5f));
	}

	public static Sprite drawWall(int width, int height) {
		LucyImage img = ImageBuilder.getItemImage(ItemType.WALL);
		return new TiledSprite(new LayeredImage(img), Point.ZERO, width, height,
				GRID_SIZE);
	}

	public static Sprite interfaceElement(LucyImage background, String text) {
		LayeredImage limg = new LayeredImage(background);
		limg.addLayers(1);
		limg.setTextCentered(1, text);

		return new SingleSprite(limg, Point.ZERO, 1);
	}

	public static Sprite interfaceElement(int width, int height) {
		return new SingleSprite(new LayeredImage(width, height, 2), Point.ZERO,
				1);
	}

	/**
	 * Returns the character sprite for the given ID. This will have an origin
	 * of (0,0).
	 * 
	 * @param id
	 * @return
	 */
	public static Sprite getCharacterSprite(int id) {
		if (id == 0) {
			return CharacterSpriteBuilder.getBeanSprite();
		}
		return new SingleSprite(ImageBuilder.getCharacterImg(id), GRID_SIZE);
	}
	
	public static Sprite getWorldItem(int imageID) {
		LayeredImage img = new LayeredImage(ImageBuilder.getItemImage(imageID));
		return new SingleSprite(img, Point.ZERO, GRID_SIZE);
	}

	public static Sprite getDoorSprite() {
		LayeredImage limg = new LayeredImage(32, 64, 2);
		LucyImage top = ImageBuilder.getItemImage(10);
		LucyImage btm = ImageBuilder.getItemImage(18);
		limg.setLayer(0, new PositionedImage(new Point(0, 32), btm));
		limg.setLayer(1, top);

		return new SingleSprite(limg, GRID_SIZE);
	}
	
	public static Sprite getTrampolineSprite() {
		LayeredImage limg = new LayeredImage(64, 32, 2);
		LucyImage left = ImageBuilder.getItemImage(16);
		LucyImage right = ImageBuilder.getItemImage(17);
		limg.setLayer(0, left);
		limg.setLayer(1, new PositionedImage(new Point(32,0), right));
		
		return new SingleSprite(limg, GRID_SIZE);
	}

	public static Sprite makeMenuButton(String text) {
		LucyImage bg = ImageBuilder.getMenuButtonBackground();
		LayeredImage limg = new LayeredImage(bg);
		limg.addLayers(1);
		limg.setTextCentered(limg.getTopLayerNumber(), text);

		return new SingleSprite(limg, 1);
	}

	public static Sprite makePaletteBlock() {
		LucyImage bg = ImageBuilder.getPaletteBlockBackground();
		LayeredImage limg = new LayeredImage(bg);
		limg.addLayers(1);

		return new SingleSprite(limg, 1);
	}
	
	public static Sprite makeUpDownControl() {
		LucyImage up = ImageBuilder.getItemImage(12);
		LucyImage dw = ImageBuilder.getItemImage(13);
		LayeredImage limg = new LayeredImage(32, 72, 2);
		limg.setLayer(0, up);
		limg.setLayer(1, new PositionedImage(new Point(0,40), dw));
		
		return new SingleSprite(limg, 1);
	}
}
