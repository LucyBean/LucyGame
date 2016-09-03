package images;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import helpers.Point;
import options.GlobalOptions;

public class ConversationBlockSprite extends Sprite {
	private static LayeredImage makeImage() {
		final int spriteSize = 64;
		final int border = 10;
		final int padding = 4;
		final int nameHeight = 12;

		final int height = border * 2 + padding + spriteSize + nameHeight;
		final int width = GlobalOptions.WINDOW_WIDTH - 100;

		final int textBoxWidth = width - (border * 2 + padding + spriteSize);
		final int textBoxHeight = padding + spriteSize + nameHeight;

		LayeredImage img = new LayeredImage(width, height, 4);

		// Set the background
		img.setLayer(0, ImageBuilder.makeRectangle(width, height));

		// Set the sprite square
		img.setLayer(1, new PositionedImage(new Point(border, border),
				ImageBuilder.makeRectangle(spriteSize, spriteSize)));

		// Set the name square
		img.setLayer(2,
				new PositionedImage(
						new Point(border, border + spriteSize + padding),
						ImageBuilder.makeRectangle(spriteSize, nameHeight)));

		// Set the text square
		img.setLayer(3, new PositionedImage(
				new Point(border + spriteSize + padding, border),
				ImageBuilder.makeRectangle(textBoxWidth, textBoxHeight)));

		// Debugging colours
		img.fillLayer(0, new Color(120, 250, 250));
		img.fillLayer(1, new Color(250, 250, 120));
		img.fillLayer(2, new Color(250, 250, 120));
		img.fillLayer(3, new Color(250, 250, 120));

		return img;
	}

	public ConversationBlockSprite() {
		super(makeImage(), 1);
	}

	public Image getTextImage() {
		if (getImage() != null) {
			PositionedImage pi = getImage().getLayer(3);
			if (pi != null) {
				return pi.getImage();
			}
		}

		return null;
	}

}
