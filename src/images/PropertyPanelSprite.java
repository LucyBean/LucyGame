package images;

import org.newdawn.slick.Color;

import helpers.Point;
import helpers.Rectangle;

public class PropertyPanelSprite extends Sprite {
	private static LayeredImage makeImage() {
		final int elementHeight = 20;
		final int textBoxWidth = 60;
		final int buttonWidth = 20;
		final int padding = 2;
		final int border = 4;

		final int width = textBoxWidth + 2 * border;
		final int height = 4 * elementHeight + 3 * padding + 2 * border;

		LayeredImage limg = new LayeredImage(width, height, 4);

		int x;
		int y;

		// Name
		x = border;
		y = border;
		limg.setLayer(0, new PositionedImage(new Point(x, y),
				ImageBuilder.makeRectangle(textBoxWidth, elementHeight)));

		// Quantity
		y = border + 2 * elementHeight + 2 * padding;
		limg.setLayer(1, new PositionedImage(new Point(x, y),
				ImageBuilder.makeRectangle(textBoxWidth, elementHeight)));

		// Plus
		x = border + (textBoxWidth - buttonWidth) / 2;
		y = border + elementHeight + padding;
		limg.setLayer(2,
				new PositionedImage(new Point(x, y), ImageBuilder.makeRectangle(
						buttonWidth, elementHeight, Color.red)));

		// Minus
		y = border + 3 * elementHeight + 3 * padding;
		limg.setLayer(3,
				new PositionedImage(new Point(x, y), ImageBuilder.makeRectangle(
						buttonWidth, elementHeight, Color.red)));

		return limg;
	}

	public PropertyPanelSprite() {
		super(makeImage(), 1);
		setQuantity(0);
	}
	
	public void setName(String name) {
		LayeredImage limg = getImage();
		LucyImage i = limg.getLayer(0).getImage();
		Point alignment = new Point(i.getWidth()/2, i.getHeight());
		limg.setText(0, name, alignment, 1, 2);
	}
	
	public void setQuantity(int amount) {
		LayeredImage limg = getImage();
		limg.setTextCentered(1, "" + amount);
	}
	
	public void clear() {
		LayeredImage limg = getImage();
		limg.clear(0);
		limg.clear(1);
	}
	
	public Rectangle getPlusBound() {
		LayeredImage limg = getImage();
		Point origin = limg.getLayer(2).getOrigin();
		LucyImage i = limg.getLayer(2).getImage();
		return new Rectangle(origin, i.getWidth(), i.getHeight());
	}
	
	public Rectangle getMinusBound() {
		LayeredImage limg = getImage();
		Point origin = limg.getLayer(3).getOrigin();
		LucyImage i = limg.getLayer(3).getImage();
		return new Rectangle(origin, i.getWidth(), i.getHeight());
	}

}
