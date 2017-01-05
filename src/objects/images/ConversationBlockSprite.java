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

	private boolean leftAligned = true;

	private static LayeredImage makeImage() {

		LayeredImage img = new LayeredImage(width, height, 4);

		// Set the background
		img.setLayer(0, ImageBuilder.getColouredRectangle(width, height, 5));

		// Set the sprite square
		img.setLayerPosition(1, new Point(border, border));

		// Set the name square
		img.setLayerPosition(2,
				new Point(border, border + spriteSize + padding));

		// Set the text square
		img.setLayerPosition(3,
				new Point(border + spriteSize + padding, border));

		return img;
	}

	/**
	 * Sets the character image to display on the left
	 */
	private void setLeftDisplay() {
		getImage().setLayerPosition(1, new Point(border, border));
		getImage().setLayerPosition(2,
				new Point(border, border + spriteSize + padding));
		getImage().setLayerPosition(3,
				new Point(border + spriteSize + padding, border));
		leftAligned = true;
	}

	/**
	 * Sets the character image to display on the right
	 */
	private void setRightDisplay() {
		getImage().setLayerPosition(1,
				new Point(border + textBoxWidth + padding, border));
		getImage().setLayerPosition(2,
				new Point(border + textBoxWidth + padding,
						border + spriteSize + padding));
		getImage().setLayerPosition(3, new Point(border, border));
		leftAligned = false;
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
		if (cc == ConversationCharacter.LUCY) {
			setLeftDisplay();
		} else {
			setRightDisplay();
		}

		if (cc.getImage() != null) {
			getImage().setLayer(1, cc.getImage());
		} else {
			getImage().clear(1);
		}

		if (leftAligned) {
			getImage().setText(2, cc.getName(), new Point(spriteSize, 0), 2, 0);
		} else {
			getImage().setText(2, cc.getName(), Point.ZERO);
		}
	}

}
