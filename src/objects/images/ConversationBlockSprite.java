package objects.images;

import helpers.Point;
import helpers.Rectangle;
import objects.world.characters.ConversationCharacter;
import options.GlobalOptions;

public class ConversationBlockSprite extends SingleSprite {
	private static final int spriteSize = 64;
	private static final int border = 10;
	private static final int padding = 4;
	private static final int nameHeight = 20;

	private static final int height = border * 2 + padding + spriteSize
			+ nameHeight;
	private static final int width = GlobalOptions.WINDOW_WIDTH - 100;

	private static final int textBoxWidth = width
			- (border * 2 + padding + spriteSize);
	private static final int textBoxHeight = padding + spriteSize + nameHeight;

	private static LayeredImage makeImage() {

		LayeredImage img = new LayeredImage(width, height, 4);

		// Set the background
		img.setLayer(0, ImageBuilder.getColouredRectangle(width, height, 5));

		// Set the sprite square
		img.setLayerPosition(1, new Point(border, border));

		// Set the name square
		img.setLayer(2,
				new PositionedImage(
						new Point(border, border + spriteSize + padding),
						new TextImage("Name")));

		// Set the text square
		img.setLayerPosition(3,
				new Point(border + spriteSize + padding, border));

		return img;
	}

	/**
	 * Sets the character image to display on the left
	 */
	private void setLeftDisplay() {
		// Sprite square
		getImage().setLayerPosition(1, new Point(border, border));

		// Name square
		getImage().setLayerPosition(2,
				new Point(border, border + spriteSize + padding));
		TextImage timg = (TextImage) getImage().getLayer(2).getImage();
		// Left align the name
		timg.setAlignment(0, 0);

		// Text square
		getImage().setLayerPosition(3,
				new Point(border + spriteSize + padding, border));
	}

	/**
	 * Sets the character image to display on the right
	 */
	private void setRightDisplay() {
		// Sprite square
		getImage().setLayerPosition(1,
				new Point(border + textBoxWidth + padding, border));

		// Name square
		getImage().setLayerPosition(2,
				new Point(border + textBoxWidth + padding + spriteSize,
						border + spriteSize + padding));
		TextImage timg = (TextImage) getImage().getLayer(2).getImage();
		// Right align the name
		timg.setAlignment(2, 0);

		// Text square
		getImage().setLayerPosition(3, new Point(border, border));
	}
	
	public static int getImageHeight() {
		return height;
	}

	public ConversationBlockSprite() {
		super(makeImage(), 1);
	}

	public Rectangle getTextBoxSize() {
		return new Rectangle(Point.ZERO, textBoxWidth, textBoxHeight);
	}

	public void setText(TextImage timg) {
		getImage().setLayer(3, timg);
	}

	public void setCharacter(ConversationCharacter cc) {
		String name;
		LucyImage img;
		if (cc == null) {
			img = null;
			name = "Unknown";
		} else {
			img = cc.getImage();
			name = cc.getName();
		}
		if (cc == ConversationCharacter.LUCY) {
			setLeftDisplay();
		} else {
			setRightDisplay();
		}

		// Set the image
		if (img != null) {
			getImage().setLayer(1, cc.getImage());
		} else {
			getImage().clear(1);
		}

		// Set the name
		TextImage timg = (TextImage) getImage().getLayer(2).getImage();
		timg.setText(name);
	}

}
