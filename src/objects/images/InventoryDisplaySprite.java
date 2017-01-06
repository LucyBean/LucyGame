package objects.images;

import helpers.Dir;
import helpers.Point;
import objects.world.characters.InventoryItem;

public class InventoryDisplaySprite extends SingleSprite {
	private static LayeredImage makeImage() {
		final int iconSize = 32;
		final int border = 6;
		final int padding = 6;
		final int textBoxWidth = 70;
		final int quantitySize = 20;

		final int width = border * 2 + padding * 2 + iconSize + textBoxWidth
				+ quantitySize;
		final int height = border * 2 + iconSize;

		LayeredImage img = new LayeredImage(width, height, 5);

		// Set background
		img.setLayer(0, ImageBuilder.getColouredRectangle(width, height, 6));

		Point p = new Point(border, border);

		// Icon
		img.setLayerPosition(1, p);
		p = p.move(Dir.EAST, iconSize + padding);

		// Text
		// Middle align this
		p.move(Dir.SOUTH, iconSize/2);
		TextImage name = new TextImage("Name");
		name.setAlignment(0, 1);
		img.setLayer(2, new PositionedImage(p, name));
		p = p.move(Dir.EAST, textBoxWidth + padding);

		// Digits
		TextImage digits = new TextImage("00");
		digits.setAlignment(0,1);
		img.setLayer(3, new PositionedImage(p, digits));

		return img;
	}

	public InventoryDisplaySprite() {
		super(makeImage(), 1);
	}

	private void setDeselectedBackground() {
		getImage().fillLayer(0, 0);
	}

	private void setIcon(LucyImage i) {
		getImage().setLayer(1, i);
	}

	private void setName(String name) {
		getImage().setText(2, name);
	}

	private void setQuantity(int quantity) {
		getImage().setText(3, "" + quantity);
	}
	
	public void setTo(InventoryItem ii, int quantity) {
		LucyImage img = ii.getImage();
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
