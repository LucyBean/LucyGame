package images;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
		beanAnims = importAnimation("BEAN");
	}

	private static Map<Integer, LayeredImage> importAnimation(String name) {
		Map<Integer, LayeredImage> map = new HashMap<>();
		int[] timings = new int[ActorState.values().length];
		boolean[] looping = new boolean[ActorState.values().length];
		for (int i = 0; i < timings.length; i++) {
			timings[i] = 32;
			looping[i] = true;
		}
		try {
			File timingsFile = new File("data/chars/" + name + "/timings");
			if (timingsFile.exists()) {
				BufferedReader br = new BufferedReader(
						new FileReader(timingsFile));
				String timesLine = br.readLine();
				if (timesLine != null) {
					String[] times = timesLine.split(",");
					for (int i = 0; i < times.length && i < timings.length; i++) {
						if (times[i].matches("\\d+")) {
							Integer n = Integer.valueOf(times[i]);
							timings[i] = n;
						}
					}
				}
				String loopLine = br.readLine();
				if (loopLine != null) {
					String[] loops = loopLine.split(",");
					for (int i = 0; i < loops.length && i < looping.length; i++) {
						if (loops[i].matches("\\d+")) {
							Integer n = Integer.valueOf(loops[i]);
							looping[i] = (n == 1);
						}
					}
				}
				br.close();
			}
		} catch (IOException ioe) {
			System.err.println("Error while reading timings file.");
			ioe.printStackTrace();
		}
		for (int i = 0; i < ActorState.values().length; i++) {
			ActorState a = ActorState.values()[i];
			try {
				File f = new File(
						"data/chars/" + name + "/" + a.toString() + ".png");
				if (f.exists()) {
					Image img = new Image(f.getPath());
					int frameWidth = img.getWidth() / 24;
					SpriteSheet s = new SpriteSheet(img, frameWidth,
							img.getHeight());
					LayeredImage limg = new LayeredImage(
							new AnimatedImage(s, timings[i], looping[i]));
					map.put(a.ordinal(), limg);
				} else if (a == ActorState.IDLE) {
					System.err.println("No idle animation for " + name);
				}
			} catch (SlickException se) {
				se.printStackTrace();
			}
		}
		return map;
	}

	public static Sprite getBeanSprite() {
		StatedSprite ss = new StatedSprite(beanAnims.get(ActorState.IDLE.ordinal()), ActorState.IDLE.ordinal(),
				GRID_SIZE);
		ss.setImages(beanAnims);
		return ss;
	}
}
