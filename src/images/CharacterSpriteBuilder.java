package images;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import objects.ActorState;
import options.GlobalOptions;

public class CharacterSpriteBuilder {
	private final static int GRID_SIZE = GlobalOptions.GRID_SIZE;
	private static Map<Integer, LayeredImage> beanAnims;

	public static void initSpriteSheets() {
		beanAnims = new HashMap<>();
		for (ActorState a : ActorState.values()) {
			try {
				File f = new File(
						"data/char spritesheets/BEAN-" + a.toString() + ".png");
				if (f.exists()) {
					Image i = new Image(f.getPath());
					int frameWidth = i.getWidth() / 24;
					SpriteSheet s = new SpriteSheet(i, frameWidth,
							i.getHeight());
					LayeredImage limg = new LayeredImage(new AnimatedImage(s));
					beanAnims.put(a.ordinal(), limg);
				} else if (a == ActorState.IDLE) {
					System.err.println("No idle animation for BEAN.");
				}
			} catch (SlickException se) {
				se.printStackTrace();
			}
		}
	}

	public static Sprite getBeanSprite() {
		StatedSprite ss = new StatedSprite(beanAnims.get(ActorState.IDLE.ordinal()),
				ActorState.IDLE.ordinal(), GRID_SIZE);
		ss.setImages(beanAnims);
		return ss;
	}
}
