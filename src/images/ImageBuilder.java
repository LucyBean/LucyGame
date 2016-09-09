package images;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import objects.ItemType;
import options.GlobalOptions;

public class ImageBuilder {
	private static Image menuButtonBackground;
	private static Image menuButtonSelectedBackground;
	private static Image worldLoaderButtonBackground;
	private static Image paletteBlockBackground;
	private static SpriteSheet conversationCharacters;
	private static SpriteSheet characters;
	private static SpriteSheet worldObjects;

	public static void initSpriteSheets() throws SlickException {
		conversationCharacters = new SpriteSheet("data/characterFaces.png", 64,
				64);
		characters = new SpriteSheet("data/characters.png", 40, 80);
		worldObjects = new SpriteSheet("data/worldSprites.png", 32, 32);
	}

	public static Image makeRectangle(int width, int height, Color fill,
			Color border) {
		try {
			Image img = new Image(width, height);
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

	public static Image makeRectangle(int width, int height, Color fill) {
		return ImageBuilder.makeRectangle(width, height, fill, fill);
	}

	/**
	 * Creates a new blank rectangular image.
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static Image makeRectangle(int width, int height) {
		return ImageBuilder.makeRectangle(width, height, new Color(0, 0, 0, 0));
	}

	public static Image getMenuButtonBackground() {
		if (menuButtonBackground == null) {
			menuButtonBackground = makeRectangle(280, 32,
					new Color(240, 120, 180));
		}

		return menuButtonBackground;
	}

	public static Image getMenuButtonSelectedBackground() {
		if (menuButtonSelectedBackground == null) {
			menuButtonSelectedBackground = makeRectangle(280, 32,
					new Color(240, 240, 180));
		}

		return menuButtonSelectedBackground;
	}

	public static Image getWorldButtonBackground() {
		if (worldLoaderButtonBackground == null) {
			worldLoaderButtonBackground = makeRectangle(400, 32,
					new Color(200, 100, 200));
		}

		return worldLoaderButtonBackground;
	}

	public static Image getKeyImg(int keyID) {
		if (keyID > 0) {
			keyID = (keyID % 4) + 1;
		}
		int itemID = (keyID - 1) + ItemType.YELLOW_KEY.ordinal();
		return getItemImage(itemID);
	}

	public static Image getLockImg(int keyID) {
		if (keyID <= 0 || keyID > 4) {
			keyID = 1;
		}
		int itemID = (keyID - 1) + ItemType.YELLOW_LOCK.ordinal();
		return getItemImage(itemID);
	}

	public static Image getConversationCharacterImg(int id) {
		if (id >= 0 && id < 4) {
			return conversationCharacters.getSprite(id, 0);
		} else {
			return null;
		}
	}

	public static Image getCharacterImg(int id) {
		if (id >= 0 && id < 4) {
			return characters.getSprite(id, 0);
		} else {
			return null;
		}
	}

	public static Image getPaletteBlockBackground() {
		if (paletteBlockBackground == null) {
			paletteBlockBackground = ImageBuilder.makeRectangle(32, 32);
		}

		return paletteBlockBackground;
	}

	public static Image getItemImage(int id) {
		int col = id % 8;
		int row = id / 8;
		return worldObjects.getSprite(col, row);
	}

	public static Image getItemImage(ItemType it) {
		int id = it.ordinal();
		return getItemImage(id);
	}
}
