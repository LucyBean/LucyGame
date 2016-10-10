package images;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class AnimatedImage implements LucyImage {
	private SpriteSheet sprites;
	private int animationInterval;
	private int currentImage = 0;
	private int currentDisplayDuration = 0;
	private boolean mirrored;

	public AnimatedImage(SpriteSheet sprites, int animationInterval) {
		this.sprites = sprites;
		this.animationInterval = animationInterval;
	}

	public void update(int delta) {
		currentDisplayDuration += delta;
		if (currentDisplayDuration > animationInterval) {
			currentImage++;
			currentDisplayDuration -= animationInterval;

			if (currentImage >= sprites.getHorizontalCount()) {
				currentImage = 0;
			}
		}
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

}
