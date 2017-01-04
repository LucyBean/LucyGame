package objects.images;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import objects.world.ActorState;
import options.GlobalOptions;

public class CharacterSpriteBuilder {
	private final static int GRID_SIZE = GlobalOptions.GRID_SIZE;
	private static Map<Integer, LayeredImage> beanAnims;

	public static void initSpriteSheets() {
		beanAnims = importAnimation("BEAN");
	}

	private static Map<Integer, LayeredImage> importAnimation(String name) {
		Map<Integer, LayeredImage> map = new HashMap<>();
		try {
			InputStream propertiesFile = CharacterSpriteBuilder.class.getResourceAsStream(
					"/chars/" + name + "/properties");
			if (propertiesFile != null) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(propertiesFile));
				String nextLine = br.readLine();
				while (nextLine != null) {
					String[] parts = nextLine.split(":");
					// check if this is a line that declares an animation
					if (parts.length > 0 && parts[0].matches("[A-Z_]+")) {
						String animName = parts[0];
						try {
							ActorState as = ActorState.valueOf(animName);
							LayeredImage limg = getImage(name, as, br);
							if (limg != null) {
								map.put(as.ordinal(), limg);
							} else if (as == ActorState.IDLE) {
								System.err.println(
										"Warning: No IDLE animation for "
												+ name);
							}
						} catch (IllegalArgumentException iae) {
							System.err.println("Properties specified for "
									+ name + " for unknown animation "
									+ animName);
						}
					}
					nextLine = br.readLine();
				}
			}
		} catch (IOException | SlickException e) {
			System.err.println("Error while reading timings file.");
			e.printStackTrace();
		}

		return map;
	}

	private static LayeredImage getImage(String name, ActorState state,
			BufferedReader source) throws IOException, SlickException {
		// Try to convert this to an animation
		String animName = state.name();
		String animPath = "/chars/" + name + "/" + animName + ".png";
		URL u = CharacterSpriteBuilder.class.getResource(animPath);
		if (u != null) {
			Image img = new Image(u.getFile());

			// Determine the type of this animation
			Pattern typePattern = Pattern.compile("\tTYPE:(.+)");
			String typeLine = source.readLine();
			Matcher typeMatcher = typePattern.matcher(typeLine);

			if (typeMatcher.matches()) {
				String type = typeMatcher.group(1);
				type = type.toLowerCase();

				if (type.equals("static")) {
					// This is a static sprite
					// No properties need to be checked
					LayeredImage limg = new LayeredImage(new StaticImage(img));
					return limg;
				} else if (type.equals("animated")) {
					// This is an animated sprite
					// Check for FRAMES, DELAY, and LOOP properties
					// Extract the other properties

					String propLine = source.readLine();
					Pattern propPattern = Pattern.compile("\t(\\w+):(.+)");
					Matcher propMatcher = propPattern.matcher(propLine);
					Map<String, String> properties = new HashMap<>();
					while (propMatcher.matches()) {
						String prop = propMatcher.group(1).toUpperCase();
						String propVal = propMatcher.group(2);
						properties.put(prop, propVal);
						propLine = source.readLine();
						propMatcher = propPattern.matcher(propLine);
					}
					int numFrames = 24;
					int delay = 32;
					boolean looping = true;
					if (properties.containsKey("FRAMES")) {
						numFrames = Integer.parseInt(properties.get("FRAMES"));
					}
					if (properties.containsKey("DELAY")) {
						delay = Integer.parseInt(properties.get("DELAY"));
					}
					if (properties.containsKey("LOOP")) {
						looping = Boolean.parseBoolean(properties.get("LOOP"));
					}
					int frameWidth = img.getWidth() / numFrames;
					SpriteSheet s = new SpriteSheet(img, frameWidth,
							img.getHeight());
					LayeredImage limg = new LayeredImage(
							new AnimatedImage(s, delay, looping));
					return limg;
				} else {
					System.err.println("Unknown image type " + type + " for "
							+ animName + " for " + name);
				}
			} else {
				System.err.println("Invalid properties for " + animName);
			}
		} else {
			System.err.println("Properties specified for " + animName + " for "
					+ name + " but no matching image exists.");
		}

		return null;
	}

	public static Sprite getBeanSprite() {
		StatedSprite ss = new StatedSprite(
				beanAnims.get(ActorState.IDLE.ordinal()),
				ActorState.IDLE.ordinal(), GRID_SIZE);
		ss.setImages(beanAnims);
		return ss;
	}
}
