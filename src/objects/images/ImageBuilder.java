package objects.images;

import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.PNGImageData;
import org.newdawn.slick.opengl.renderer.SGL;

import helpers.Rectangle;
import io.CharacterSpriteBuilder;
import objects.world.ItemType;
import options.GlobalOptions;

public class ImageBuilder {
	private static SpriteSheet conversationCharacters;
	private static SpriteSheet characters;
	private static SpriteSheet worldObjects;
	private static SpriteSheet uiColourPalette;

	private static PNGImageData conversationCharactersData;
	private static PNGImageData charactersData;
	private static PNGImageData worldObjectsData;
	private static PNGImageData uiColourPaletteData;

	private static Font font;

	private static boolean spriteSheetsInitialised = false;
	private static boolean imageDataLoaded = false;

	private static RectangleImageStore colliderImages = new RectangleImageStore(
			new Color(50, 135, 220, 130));
	private static RectangleImageStore interactBoxImages = new RectangleImageStore(
			new Color(50, 220, 135, 130));
	private static RectangleImageStore sensorImages = new RectangleImageStore(
			new Color(220, 220, 40, 130));
	private static RectangleImageStore attackBoxImages = new RectangleImageStore(
			new Color(220, 40, 40, 130));
	private final static int GRID_SIZE = GlobalOptions.GRID_SIZE;

	public static boolean spriteSheetsInitialised() {
		return spriteSheetsInitialised;
	}

	public static void initFont() {
		java.awt.Font awtFont = new java.awt.Font("Arial Rounded MT Bold",
				java.awt.Font.PLAIN, 18);
		font = new TrueTypeFont(awtFont, true); // base Font, anti-aliasing
												// true/false
	}

	public static void loadImageData() throws SlickException {
		conversationCharactersData = new PNGImageData();
		charactersData = new PNGImageData();
		worldObjectsData = new PNGImageData();
		uiColourPaletteData = new PNGImageData();

		try {
			conversationCharactersData.loadImage(
					new FileInputStream("data/characterFaces.png"));
			charactersData.loadImage(
					new FileInputStream("data/characters.png"));
			worldObjectsData.loadImage(
					new FileInputStream("data/worldSprites.png"));
			uiColourPaletteData.loadImage(
					new FileInputStream("data/uiColourPalette.png"));

			imageDataLoaded = true;
		} catch (IOException ioe) {
			throw new SlickException(ioe.getMessage());
		}
	}

	public static void initSpriteSheets() throws SlickException {
		if (!imageDataLoaded) {
			loadImageData();
		}

		conversationCharacters = new SpriteSheet(
				new Image(conversationCharactersData), 64, 64);
		characters = new SpriteSheet(new Image(charactersData), 40, 80);
		worldObjects = new SpriteSheet(new Image(worldObjectsData), 32, 32);
		uiColourPalette = new SpriteSheet(new Image(uiColourPaletteData), 1, 1);

		// Use nearest neighbour interpolation for uiColourPalette
		// This means that the 1x1 pixels will be stretched into large images
		// of block colour
		uiColourPalette.getTexture().setTextureFilter(SGL.GL_NEAREST);

		CharacterSpriteBuilder.initSpriteSheets();
		spriteSheetsInitialised = true;
	}

	public static Font getFont() {
		return font;
	}

	public static StaticImage makeRectangle(int width, int height, Color fill,
			Color border) {
		try {
			StaticImage img = new StaticImage(width, height);
			Graphics g = img.getGraphics();
			if (width > 2 && height > 2) {
				g.setColor(fill);
				g.fillRect(0, 0, width, height);
				g.setColor(border);
				g.drawRect(0, 0, width - 1, height - 1);
				if (width > 5 && height > 5) {
					g.drawRect(1, 1, width - 3, height - 3);
				}
			} else {
				g.setColor(border);
				g.fillRect(0, 0, width, height);
			}
			g.flush();
			return img;
		} catch (SlickException se) {
			System.err.println("Error while creating image.");
			if (GlobalOptions.debug()) {
				se.printStackTrace();
			}
		}

		return null;
	}

	public static Image getSplash() {
		try {
			Image img = new Image("data/splash.png");
			return img;
		} catch (SlickException e) {
			System.out.println("Error loading splash");
			e.printStackTrace();
		}
		return null;
	}

	public static StaticImage getColouredRectangle(Rectangle rect,
			int uiColour) {
		int width = (int) rect.getWidth();
		int height = (int) rect.getHeight();
		return getColouredRectangle(width, height, uiColour);
	}

	public static StaticImage getColouredRectangle(int width, int height,
			int uiColour) {
		int x = uiColour % 4;
		int y = uiColour / 4;
		if (x >= 0 && x < uiColourPalette.getHorizontalCount() && y >= 0
				&& y < uiColourPalette.getVerticalCount()) {
			StaticImage simg = new StaticImage(uiColourPalette.getSprite(x, y));
			return simg.getScaledCopy(width, height);
		} else {
			return null;
		}
	}

	public static StaticImage getKeyImg(int keyID) {
		if (keyID > 4) {
			keyID = (keyID % 4) + 1;
		}
		int itemID = (keyID - 1) + ItemType.YELLOW_KEY.ordinal();
		return getItemImage(itemID);
	}

	public static StaticImage getLockImg(int keyID) {
		if (keyID <= 0 || keyID > 4) {
			keyID = 1;
		}
		int itemID = (keyID - 1) + ItemType.YELLOW_LOCK.ordinal();
		return getItemImage(itemID);
	}

	public static StaticImage getConversationCharacterImg(int id) {
		if (id >= 0 && id < 4) {
			return new StaticImage(conversationCharacters.getSprite(id, 0));
		} else {
			return null;
		}
	}

	public static LayeredImage getCharacterImg(int id) {
		if (id > 0 && id < 4) {
			return new LayeredImage(
					new StaticImage(characters.getSprite(id, 0)));
		} else {
			return null;
		}
	}

	public static StaticImage getItemImage(int id) {
		int col = id % 8;
		int row = id / 8;
		return new StaticImage(worldObjects.getSprite(col, row));
	}

	public static StaticImage getItemImage(ItemType it) {
		int id = it.ordinal();
		return getItemImage(id);
	}

	/**
	 * Creates a new image for a collider for the given Rectangle.
	 * 
	 * @param collider
	 * @return
	 */
	public static LayeredImage makeColliderImage(Rectangle collider) {
		int w = (int) (collider.getWidth() * GRID_SIZE);
		int h = (int) (collider.getHeight() * GRID_SIZE);
		LucyImage s = colliderImages.get(w, h);
		return new LayeredImage(s);
	}

	/**
	 * Creates a new image for an interact box for the given Rectangle.
	 * 
	 * @param interactBox
	 * @return
	 */
	public static LayeredImage makeInteractBoxImage(Rectangle interactBox) {
		// check if a collider of this size has been made already
		int w = (int) (interactBox.getWidth() * GRID_SIZE);
		int h = (int) (interactBox.getHeight() * GRID_SIZE);
		LucyImage s = interactBoxImages.get(w, h);
		return new LayeredImage(s);
	}

	public static LayeredImage makeSensorImage(Rectangle sensor) {
		// check if a sensor image of this size has been made already
		int w = (int) (sensor.getWidth() * GRID_SIZE);
		int h = (int) (sensor.getHeight() * GRID_SIZE);
		LucyImage s = sensorImages.get(w, h);
		return new LayeredImage(s);
	}

	public static LayeredImage makeAttackBoxImage(Rectangle attackBox) {
		// check if an attack box image of this size has been made already
		int w = (int) (attackBox.getWidth() * GRID_SIZE);
		int h = (int) (attackBox.getHeight() * GRID_SIZE);
		LucyImage s = attackBoxImages.get(w, h);
		return new LayeredImage(s);
	}
}
