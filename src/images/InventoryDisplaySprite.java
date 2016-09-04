package images;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import helpers.Dir;
import helpers.Point;
import player.InventoryItem;

public class InventoryDisplaySprite extends Sprite {
	private final static Point vTextAlign = new Point(0, 8);

	private static LayeredImage makeImage() {
		final int iconSize = 32;
		final int border = 6;
		final int padding = 6;
		final int textBoxWidth = 70;
		final int digitWidth = 10;

		final int width = border * 2 + padding * 2 + iconSize + textBoxWidth
				+ digitWidth * 2;
		final int height = border * 2 + iconSize;

		LayeredImage img = new LayeredImage(width, height, 5);

		// Set background
		img.setLayer(0, ImageBuilder.makeRectangle(width, height));

		Point p = new Point(border, border);

		// Icon
		Image icon = ImageBuilder.makeRectangle(iconSize, iconSize);
		img.setLayer(1, new PositionedImage(p, icon));
		p = p.move(Dir.EAST, iconSize + padding);

		// Text
		Image textBox = ImageBuilder.makeRectangle(textBoxWidth, iconSize);
		img.setLayer(2, new PositionedImage(p, textBox));
		p = p.move(Dir.EAST, textBoxWidth + padding);

		// Digit one
		Image digit1 = ImageBuilder.makeRectangle(digitWidth, iconSize);
		img.setLayer(3, new PositionedImage(p, digit1));
		p = p.move(Dir.EAST, digitWidth);

		Image digit2 = ImageBuilder.makeRectangle(digitWidth, iconSize);
		img.setLayer(4, new PositionedImage(p, digit2));

		return img;
	}

	public InventoryDisplaySprite() {
		super(makeImage(), 1);
	}

	private void setDeselectedBackground() {
		getImage().fillLayer(0, new Color(200, 200, 200));
	}

	private void setIcon(Image i) {
		getImage().setLayer(1, i);
	}

	private void setName(String name) {
		getImage().setText(2, name, vTextAlign);
	}

	private void setQuantity(int quantity) {
		// Set quantity on layers 3 and 4
		if (quantity >= 10) {
			int digitTens = quantity / 10;
			getImage().setText(3, "" + digitTens, vTextAlign);
		} else {
			getImage().clear(3);
		}
		int digitOnes = quantity % 10;
		getImage().setText(4, "" + digitOnes, vTextAlign);
	}
	
	public void setTo(InventoryItem ii, int quantity) {
		Image img = ii.getImage();
		String name = ii.getName();
		
		setDeselectedBackground();
		setIcon(img);
		setName(name);
		setQuantity(quantity);
	}
	
	public void setBlank() {
		setDeselectedBackground();
		setIcon(null);
		for (int i = 2; i <= 4; i++) {
			getImage().clear(i);
		}
	}

}
