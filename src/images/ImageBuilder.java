package images;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import helpers.Rectangle;
import objects.ItemType;
import options.GlobalOptions;

public class ImageBuilder {
	private static StaticImage menuButtonBackground;
	private static StaticImage menuButtonSelectedBackground;
	private static StaticImage worldLoaderButtonBackground;
	private static StaticImage paletteBlockBackground;
	private static SpriteSheet conversationCharacters;
	private static SpriteSheet characters;
	private static SpriteSheet worldObjects;
	private static SpriteSheet beanSpriteSheet;

	private static RectangleImageStore colliderImages = new RectangleImageStore();
	private static RectangleImageStore interactBoxImages = new RectangleImageStore();
	private final static int GRID_SIZE = GlobalOptions.GRID_SIZE;

	public static void initSpriteSheets() throws SlickException {
		conversationCharacters = new SpriteSheet("data/characterFaces.png", 64,
				64);
		characters = new SpriteSheet("data/characters.png", 40, 80);
		worldObjects = new SpriteSheet("data/worldSprites.png", 32, 32);
		beanSpriteSheet = new SpriteSheet("data/char spritesheets/bean.png", 48,
				71);
	}

	public static StaticImage makeRectangle(int width, int height, Color fill,
			Color border) {
		try {
			StaticImage img = new StaticImage(width, height);
			Graphics g = img.getGraphics();
			g.setColor(fill);
			g.fillRect(0, 0, width, height);
			g.setColor(border);
			g.drawRect(0, 0, width - 1, height - 1);
			g.drawRect(1, 1, width - 3, height - 3);
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

	public static StaticImage makeRectangle(int width, int height, Color fill) {
		return ImageBuilder.makeRectangle(width, height, fill, fill);
	}

	/**
	 * Creates a new blank rectangular image.
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static StaticImage makeRectangle(int width, int height) {
		return ImageBuilder.makeRectangle(width, height, new Color(0, 0, 0, 0));
	}

	public static StaticImage getMenuButtonBackground() {
		if (menuButtonBackground == null) {
			menuButtonBackground = makeRectangle(280, 32,
					new Color(240, 120, 180));
		}

		return menuButtonBackground;
	}

	public static StaticImage getMenuButtonSelectedBackground() {
		if (menuButtonSelectedBackground == null) {
			menuButtonSelectedBackground = makeRectangle(280, 32,
					new Color(240, 240, 180));
		}

		return menuButtonSelectedBackground;
	}

	public static StaticImage getWorldButtonBackground() {
		if (worldLoaderButtonBackground == null) {
			worldLoaderButtonBackground = makeRectangle(400, 32,
					new Color(200, 100, 200));
		}

		return worldLoaderButtonBackground;
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

	public static LucyImage getCharacterImg(int id) {
		if (id == 0) {
			return new AnimatedImage(beanSpriteSheet);
		} else if (id > 0 && id < 4) {
			return new StaticImage(characters.getSprite(id, 0));
		} else {
			return null;
		}
	}

	public static StaticImage getPaletteBlockBackground() {
		if (paletteBlockBackground == null) {
			paletteBlockBackground = ImageBuilder.makeRectangle(32, 32);
		}

		return paletteBlockBackground;
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
		// check if a collider of this size has been made already
		int w = (int) (collider.getWidth() * GRID_SIZE);
		int h = (int) (collider.getHeight() * GRID_SIZE);
		LucyImage s = colliderImages.get(w, h);

		if (s == null) {
			// Create a new image
			Color fill = new Color(50, 135, 220, 130);
			Color border = fill.darker(0.5f);
			border.a = 220;
			s = ImageBuilder.makeRectangle(w, h, fill, border);
			colliderImages.store(s);
		}

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

		if (s == null) {

			Color fill = new Color(50, 220, 135, 130);
			Color border = fill.darker(0.5f);
			border.a = 220;
			s = ImageBuilder.makeRectangle(w, h, fill, border);
			interactBoxImages.store(s);
		}

		return new LayeredImage(s);
	}
}
