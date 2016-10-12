package images;

import java.util.HashMap;
import java.util.Map;

import objects.ActorState;

public class StatedSprite extends Sprite {
	int currentState;
	LayeredImage defaultImage;
	LayeredImage currentImage;
	Map<Integer, LayeredImage> images;
	boolean mirrored;

	public StatedSprite(LayeredImage defaultImage, int defaultState,
			int gridSize) {
		super(defaultImage.getRectangle(), gridSize);
		this.defaultImage = defaultImage;
		currentImage = defaultImage;
		currentState = defaultState;
		images = new HashMap<>();
		images.put(defaultState, defaultImage);
	}

	public void setImage(LayeredImage image, int state) {
		images.put(state, image);
	}
	
	public void setImages(Map<Integer, LayeredImage> toAdd) {
		toAdd.entrySet().stream().forEach(i -> images.put(i.getKey(), i.getValue()));
	}

	public void setState(int newState) {
		if (needToChangeSprite(newState)) {
			getImage().resetAnimations();
			currentImage = images.get(newState);
			setRectangle(getImage().getRectangle());
			getImage().setMirrored(mirrored);
		}
		currentState = newState;
	}
	
	private boolean needToChangeSprite(int newState) {
		if (currentState == ActorState.JUMP.ordinal() && newState == ActorState.FALL.ordinal()) {
			return false;
		}
		
		return (currentState != newState);
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
