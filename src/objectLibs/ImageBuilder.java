package objectLibs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import options.GlobalOptions;

public class ImageBuilder {
	private static Image menuButtonBackground;
	private static Image worldLoaderButtonBackground;
	private static Image bby;

	private static Image makeRectangle(int width, int height, Color c) {
		try {
			Image img = new Image(width, height);
			Graphics g = img.getGraphics();
			g.setColor(c);
			g.fillRect(0, 0, width, height);
			return img;
		} catch (SlickException se) {
			System.err.println("Error while creating image.");
			if (GlobalOptions.debug()) {
				se.printStackTrace();
			}
		}

		return null;
	}

	public static Image getMenuButtonBackground() {
		if (menuButtonBackground == null) {
			menuButtonBackground = makeRectangle(360, 32,
					new Color(240, 120, 180));
		}

		return menuButtonBackground;
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
}
