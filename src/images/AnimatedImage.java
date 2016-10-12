package images;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class AnimatedImage implements LucyImage {
	private SpriteSheet sprites;
	private int animationInterval;
	private int currentImage = 0;
	private int currentDisplayDuration = 0;
	private boolean mirrored;
	private boolean loop;

	public AnimatedImage(SpriteSheet sprites, int animationInterval, boolean loop) {
		this.sprites = sprites;
		this.animationInterval = animationInterval;
		this.loop = loop;
	}

	public void update(int delta) {
		currentDisplayDuration += delta;
		if (currentDisplayDuration > animationInterval) {
			if (currentImage < sprites.getHorizontalCount() - 1) {
				currentImage++;
			} else if (loop && currentImage == sprites.getHorizontalCount() - 1) {
				currentImage = 0;
			}
			currentDisplayDuration -= animationInterval;
		}
	}
	
	public boolean isLooping() {
		return loop;
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

	public void resetAnimation() {
		currentImage = 0;
		currentDisplayDuration = 0;
	}

}
