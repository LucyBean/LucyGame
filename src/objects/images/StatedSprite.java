package objects.images;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import helpers.Point;
import objects.world.ActorState;

public class StatedSprite extends Sprite {
	private int currentState;
	private LayeredImage defaultImage;
	private LayeredImage currentImage;
	private LayeredImage queuedImage;
	private Map<Integer, LayeredImage> images;
	private boolean mirrored;
	private float alpha = 1.0f;
	private Optional<Point> alignmentPoint = Optional.empty();

	public StatedSprite(LayeredImage defaultImage, int defaultState,
			int gridSize) {
		super(defaultImage.getRectangle(), gridSize);
		this.defaultImage = defaultImage;
		currentImage = defaultImage;
		currentState = defaultState;
		images = new HashMap<>();
		images.put(defaultState, defaultImage);
	}
	
	public void setAlignmentPoint(Point p) {
		alignmentPoint = Optional.of(p);
	}

	public void setImage(LayeredImage image, int state) {
		images.put(state, image);
	}

	public void setImages(Map<Integer, LayeredImage> toAdd) {
		toAdd.entrySet().stream().forEach(
				i -> images.put(i.getKey(), i.getValue()));
	}

	public void setState(int newState) {
		if (needToChangeSprite(newState)) {
			LayeredImage newImage = images.getOrDefault(newState, defaultImage);
			changeImage(newImage);
		} else if (needToQueueSpriteChange(newState)) {
			queuedImage = images.get(newState);
		}
		currentState = newState;
	}

	private void changeImage(LayeredImage newImage) {
		assert newImage != null;
		getImage().resetAnimations();
		currentImage = newImage;
		setRectangle(getImage().getRectangle());
		// Set alignment point
		Optional<Point> imageAlign = newImage.getAlignmentPoint();
		if (alignmentPoint.isPresent() && imageAlign.isPresent()) {
			Point ap = alignmentPoint.get();
			Point ia = imageAlign.get().scale(1/((float) getGridSize()));
			Point newOrigin = ap.move(ia.neg());
			setOrigin(newOrigin);
		} else {
			setOrigin(Point.ZERO);
		}
		getImage().setMirrored(mirrored);
		getImage().setAlpha(alpha);
		if (getObject() != null) {
			getObject().statedSpriteImageChange();
		}
	}

	private boolean needToChangeSprite(int newState) {
		if (currentState == ActorState.JUMP.ordinal()
				&& newState == ActorState.FALL.ordinal()) {
			return false;
		}

		return (currentState != newState);
	}

	private boolean needToQueueSpriteChange(int newState) {
		if (currentState == ActorState.JUMP.ordinal()
				&& newState == ActorState.FALL.ordinal()) {
			return true;
		}

		return false;
	}

	@Override
	public void update(int delta) {
		getImage().update(delta);

		LucyImage img = getImage().getLayer(0).getImage();
		if (img instanceof AnimatedImage) {
			AnimatedImage ai = (AnimatedImage) img;
			if (ai.isFinished() && queuedImage != null) {
				changeImage(queuedImage);
				queuedImage = null;
			}
		}
	}
	
	public int getState() {
		return currentState;
	}

	@Override
	protected void mirrorImage(boolean mirrored) {
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

	@Override
	public void setAlpha(float alpha) {
		this.alpha = alpha;
		getImage().setAlpha(alpha);
	}
	@Override
	public float getAlpha() {
		return alpha;
	}

}
