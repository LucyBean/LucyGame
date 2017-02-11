package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import helpers.Pair;
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
			File propertiesFile = new File(
					"data/chars/" + name + "/properties.xml");
			if (propertiesFile.exists()) {
				BufferedReader br = new BufferedReader(
						new FileReader(propertiesFile));
				String nextLine;
				while ((nextLine = br.readLine())!= null) {
					Pattern animLine = Pattern.compile("<anim.*/>");
					Matcher m = animLine.matcher(nextLine);
					// If this line represents an animation
					if (m.find()) {
						Optional<Pair<LayeredImage, ActorState>> op = getImage(nextLine, name);
						op.ifPresent(p -> {
							LayeredImage limg = p.getFirst();
							ActorState as = p.getSecond();
							if (limg != null) {
								map.put(as.ordinal(), limg);
							} else if (as == ActorState.IDLE) {
								ErrorLogger.log(
										"Warning: No IDLE animation for "
												+ name, 3);
							}
						});
					}
				}
				br.close();
			}
		} catch (IOException | SlickException e) {
			ErrorLogger.log(e, "Error while reading timings file.", 3);
		}

		return map;
	}

	private static Optional<Pair<LayeredImage, ActorState>> getImage(String description,
			String charName) throws IOException, SlickException {
		// Try to convert this to an animation
		ActorState state = null;

		// Extract the properties into a map
		Pattern propertyPattern = Pattern.compile("([\\w_]+)\\s*=\\s*\"([\\w_]+)\"");
		Matcher propertyMatcher = propertyPattern.matcher(description);
		Map<String, String> properties = new HashMap<>();
		while (propertyMatcher.find()) {
			String propName = propertyMatcher.group(1).toLowerCase();
			String propVal = propertyMatcher.group(2).toLowerCase();
			properties.put(propName, propVal);
		}

		// Extract the name and get the state
		String animName = properties.get("name").toUpperCase();
		if (animName == null) {
			// No name specified
			ErrorLogger.log("Warning: no animation name specified "
					+ description + " for " + charName, 1);
			return Optional.empty();
		}

		try {
			state = ActorState.valueOf(animName);
		} catch (IllegalArgumentException iae) {
			ErrorLogger.log("Properties specified for " + charName
					+ " for unknown animation " + animName, 1);
		}

		// Find the file and check it exists
		File animFile = new File(
				"data/chars/" + charName + "/" + animName + ".png");
		if (!animFile.exists()) {
			ErrorLogger.log("Properties specified for " + animName + " for "
					+ charName + " but no matching image exists.", 1);
			return Optional.empty();
		}
		Image img = new Image(animFile.getPath());

		// Determine the type of this animation
		String type = properties.get("type");
		if (type == null) {
			// No type specified
			ErrorLogger.log(
					"No type specified for " + animName + " for " + charName, 1);
			return Optional.of(new Pair<>(null, state));
		} else if (type.equals("static")) {
			// This is a static sprite
			// No other properties need to be checked
			LayeredImage limg = new LayeredImage(new StaticImage(img));
			return Optional.of(new Pair<>(limg, state));
		} else if (type.equals("animated")) {
			// This is an animated sprite
			// Check for FRAMES, DELAY, and LOOP properties
			// Extract the other properties

			int numFrames = 24;
			int delay = 32;
			boolean looping = true;
			if (properties.containsKey("frames")) {
				numFrames = Integer.parseInt(properties.get("frames"));
			}
			if (properties.containsKey("delay")) {
				delay = Integer.parseInt(properties.get("delay"));
			}
			if (properties.containsKey("loop")) {
				looping = Boolean.parseBoolean(properties.get("loop"));
			}
			int frameWidth = img.getWidth() / numFrames;
			SpriteSheet s = new SpriteSheet(img, frameWidth, img.getHeight());
			LayeredImage limg = new LayeredImage(
					new AnimatedImage(s, delay, looping));
			return Optional.of(new Pair<>(limg, state));
		} else {
			ErrorLogger.log("Unknown image type " + type + " for " + animName
					+ " for " + charName, 1);
			return Optional.of(new Pair<>(null, state));
		}
	}

	public static Sprite getBeanSprite() {
		StatedSprite ss = new StatedSprite(
				beanAnims.get(ActorState.IDLE.ordinal()),
				ActorState.IDLE.ordinal(), GRID_SIZE);
		ss.setImages(beanAnims);
		return ss;
	}
}
