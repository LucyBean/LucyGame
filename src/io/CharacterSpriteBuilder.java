package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import objects.images.AnimatedImage;
import objects.images.LayeredImage;
import objects.images.Sprite;
import objects.images.StatedSprite;
import objects.images.StaticImage;
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
			File propertiesFile = new File("data/chars/" + name + "/properties");
			if (propertiesFile.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(propertiesFile));
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
		File animFile = new File("data/chars/" + name + "/" + animName + ".png");
		
		if (!animFile.exists()) {
			System.err.println("Properties specified for " + animName + " for "
					+ name + " but no matching image exists.");
		}else {
			Image img = new Image(animFile.getPath());

			// Determine the type of this animation
			Pattern typePattern = Pattern.compile("\tTYPE:(.+)");
			String typeLine = source.readLine();
			Matcher typeMatcher = typePattern.matcher(typeLine);

			if (!typeMatcher.matches()) {
				// The TYPE line is incorrect
				System.err.println("Invalid type line for " + animName + ": \"" + typeLine+"\"");
			} else {
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
			}
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
