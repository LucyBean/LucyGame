package objects.images;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class AnimatedImage implements LucyImage {
	private SpriteSheet sprites;
	private int animationInterval;
	private int currentImage = 0;
	private int currentDisplayDuration = 0;
	private boolean mirrored = false;
	private boolean loop = false;
	private boolean reversed = false;
	private float alpha = 1.0f;

	public AnimatedImage(SpriteSheet sprites, int animationInterval, boolean loop) {
		this.sprites = sprites;
		this.animationInterval = animationInterval;
		this.loop = loop;
	}

	public void update(int delta) {
		currentDisplayDuration += delta;
		if (currentDisplayDuration > animationInterval) {
			if (!reversed && currentImage < sprites.getHorizontalCount() - 1 || reversed && currentImage > 0) {
				currentImage += (reversed) ? -1 : 1;
			} else if (loop && currentImage == sprites.getHorizontalCount() - 1) {
				currentImage = 0;
			} else if (reversed && loop && currentImage <= 0) {
				currentImage = sprites.getHorizontalCount() - 1;
			}
			currentDisplayDuration -= animationInterval;
		}
	}
	
	public boolean isLooping() {
		return loop;
	}
	
	public void setReversed(boolean reversed) {
		this.reversed = reversed;
	}

	/**
	 * Indicates whether the image has finished its loop. This will never be
	 * true for animations that have been set to loop.
	 * 
	 * @return
	 */
	public boolean isFinished() {
		return (!loop && currentImage == sprites.getHorizontalCount() - 1);
	}

	public void setMirrored(boolean mirrored) {
		this.mirrored = mirrored;
	}

	@Override
	public void draw(float x, float y, float scale) {
		Image img = sprites.getSubImage(currentImage, 0);
		if (mirrored) {
			img = img.getFlippedCopy(true, false);
		}
		img.setAlpha(alpha);
		img.draw(x, y, scale);
	}

	@Override
	public int getWidth() {
		return sprites.getWidth() / sprites.getHorizontalCount();
	}

	@Override
	public int getHeight() {
		return sprites.getHeight() / sprites.getVerticalCount();
	}
	
	@Override
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public void resetAnimation() {
		currentImage = 0;
		currentDisplayDuration = 0;
	}

}
