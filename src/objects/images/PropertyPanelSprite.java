package objects.images;

import helpers.Point;
import helpers.Rectangle;

public class PropertyPanelSprite extends SingleSprite {
	private static LayeredImage makeImage() {
		LucyImage up = ImageBuilder.getItemImage(12);
		LucyImage down = ImageBuilder.getItemImage(13);
		final int elementHeight = 20;
		final int textBoxWidth = 60;
		final int padding = 2;
		final int border = 4;

		final int width = textBoxWidth + 2 * border;
		final int height = 2 * elementHeight + 3 * padding + 2 * border
				+ up.getHeight() + down.getHeight();

		LayeredImage limg = new LayeredImage(width, height, 4);

		int x;
		int y;

		// Name
		x = border;
		y = border;
		limg.setLayer(0, new PositionedImage(new Point(x, y), null));

		// Quantity
		y = border + elementHeight + 2 * padding + up.getHeight();
		limg.setLayer(1, new PositionedImage(new Point(x, y), null));

		// Plus
		x = border + (textBoxWidth - up.getWidth()) / 2;
		y = border + elementHeight + padding;
		limg.setLayer(2, new PositionedImage(new Point(x, y), up));

		// Minus
		y = border + 2 * elementHeight + 3 * padding + up.getHeight();
		limg.setLayer(3, new PositionedImage(new Point(x, y), down));

		return limg;
	}

	public PropertyPanelSprite() {
		super(makeImage(), 1);
		setQuantity(0);
	}

	public void setName(String name) {
		LayeredImage limg = getImage();
		TextImage timg = new TextImage(name);
		limg.setLayer(0, timg);
	}

	public void setQuantity(int amount) {
		LayeredImage limg = getImage();
		TextImage timg = new TextImage("" + amount);
		limg.setLayer(1, timg);
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
