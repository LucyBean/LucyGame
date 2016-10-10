package images;

import java.util.HashMap;
import java.util.Map;

public class StatedSprite extends Sprite {
	LayeredImage defaultImage;
	LayeredImage currentImage;
	Map<Integer, LayeredImage> images;
	boolean mirrored;

	public StatedSprite(LayeredImage defaultImage, int defaultState,
			int gridSize) {
		super(defaultImage.getRectangle(), gridSize);
		this.defaultImage = defaultImage;
		currentImage = defaultImage;
		images = new HashMap<>();
		images.put(defaultState, defaultImage);
	}

	public void setImage(LayeredImage image, int state) {
		images.put(state, image);
	}

	public void setState(int state) {
		LayeredImage newImage = images.get(state);
		if (currentImage != newImage) {
			// TODO: Reset animation if new state
			currentImage = images.get(state);
			setRectangle(getImage().getRectangle());
			getImage().setMirrored(mirrored);
		}
	}

	@Override
	public void update(int delta) {
		getImage().update(delta);
	}

	@Override
	public void setMirrored(boolean mirrored) {
		this.mirrored = mirrored;
		getImage().setMirrored(mirrored);
	}

	@Override
	public LayeredImage getImage() {
		if (currentImage != null) {
			return currentImage;
		} else {
			return defaultImage;
		}
	}

}
