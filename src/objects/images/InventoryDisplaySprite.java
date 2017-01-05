package objects.images;

import org.newdawn.slick.Color;

import helpers.Dir;
import helpers.Point;
import objects.world.characters.InventoryItem;

public class InventoryDisplaySprite extends SingleSprite {
	private final static Point vTextAlign = new Point(0, 8);

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
		img.setLayerPosition(2, p);
		p = p.move(Dir.EAST, textBoxWidth + padding);

		// Digits
		img.setLayerPosition(3, p);

		return img;
	}

	public InventoryDisplaySprite() {
		super(makeImage(), 1);
	}

	private void setDeselectedBackground() {
		getImage().fillLayer(0, new Color(200, 200, 200));
	}

	private void setIcon(LucyImage i) {
		getImage().setLayer(1, i);
	}

	private void setName(String name) {
		getImage().setText(2, name, vTextAlign);
	}

	private void setQuantity(int quantity) {
		getImage().setText(3, "" + quantity, vTextAlign);
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
