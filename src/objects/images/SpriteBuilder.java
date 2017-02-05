package objects.images;

import org.newdawn.slick.Font;

import helpers.Point;
import helpers.Rectangle;
import io.CharacterSpriteBuilder;
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
	
	public static Sprite getColouredRectangle(Rectangle rect, int uiColour, int gridSize) {
		return getColouredRectangle(rect.getWidth(), rect.getHeight(), uiColour, gridSize);
	}
	
	public static Sprite getColouredRectangle(float width, float height, int uiColour, int gridSize) {
		int w = (int) (width * gridSize);
		int h = (int) (height * gridSize);
		LucyImage lui = ImageBuilder.getColouredRectangle(w, h, uiColour);
		return new SingleSprite(new LayeredImage(lui), gridSize);
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
		LucyImage bg = ImageBuilder.getColouredRectangle(240, 32, 0);
		LayeredImage limg = new LayeredImage(bg);
		limg.addLayers(1);
		limg.setTextCentered(limg.getTopLayerNumber(), text);

		return new SingleSprite(limg, 1);
	}

	public static Sprite makePaletteBlock() {
		LucyImage bg = ImageBuilder.getColouredRectangle(32,32,2);
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
	
	public static Sprite makeTextPrompt(int width, int border) {
		Font f = ImageBuilder.getFont();
		int height = f.getLineHeight();
		StaticImage bg = ImageBuilder.getColouredRectangle(width+2*border, height+2*border, 2);
		TextImage text = new TextImage("Test");
		StaticImage textbg = ImageBuilder.getColouredRectangle(width, height, 3);
		Point p = new Point(border, border);
		LayeredImage limg = new LayeredImage(bg);
		limg.addLayers(2);
		limg.setLayer(1, new PositionedImage(p, textbg));
		limg.setLayer(2, new PositionedImage(p, text));
		
		return new SingleSprite(limg, 1);
	}
}
