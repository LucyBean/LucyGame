package images;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import options.GlobalOptions;

public class ImageBuilder {
	private static Image menuButtonBackground;
	private static Image menuButtonSelectedBackground;
	private static Image worldLoaderButtonBackground;
	private static Image bby;
	private static SpriteSheet keysAndLocks;
	private static SpriteSheet inventoryItems;

	public static void initSpriteSheets() throws SlickException {
		keysAndLocks = new SpriteSheet("data/keys.png", 32, 32);
		inventoryItems = new SpriteSheet("data/inventory_items.png", 32, 32);
	}

	public static Image makeRectangle(int width, int height, Color fill, Color border) {
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
	 * @param width
	 * @param height
	 * @return
	 */
	public static Image makeRectangle(int width, int height) {
		return ImageBuilder.makeRectangle(width,  height, new Color(0,0,0,0));
	}

	public static Image getMenuButtonBackground() {
		if (menuButtonBackground == null) {
			menuButtonBackground = makeRectangle(360, 32,
					new Color(240, 120, 180));
		}

		return menuButtonBackground;
	}
	
	public static Image getMenuButtonSelectedBackground() {
		if (menuButtonSelectedBackground == null) {
			menuButtonSelectedBackground = makeRectangle(360, 32, new Color(240, 240, 180));
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

	public static Image getBbyImage() {
		if (bby == null) {
			try {
				bby = new Image("data/Bby.png");
			} catch (SlickException e) {
				System.err.println("Error importing Bby image.");
				if (GlobalOptions.debug()) {
					e.printStackTrace();
				}
			}
		}
		
		return bby;
	}
	
	public static Image getKeyImg(int keyID) {
		if (keyID <= 0 || keyID > 4) {
			keyID = 1;
		}
		return keysAndLocks.getSubImage(0, keyID - 1);
	}
	
	public static Image getLockImg(int keyID) {
		if (keyID <= 0 || keyID > 4) {
			keyID = 1;
		}
		return keysAndLocks.getSubImage(1, keyID - 1);
	}
	
	public static Image getGemImg() {
		return inventoryItems.getSprite(0, 0);
	}
}
