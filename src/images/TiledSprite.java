package images;

import helpers.Point;
import helpers.Rectangle;
import objects.CoOrdTranslator;

public class TiledSprite extends Sprite {
	private LayeredImage image;
	private int tileX;
	private int tileY;
	
	public TiledSprite(LayeredImage img, Point origin, int tileX, int tileY,
			int gridSize) {
		super(img.getRectangle().scale(tileX, tileY), gridSize);
		image = img;
		this.tileX = tileX;
		this.tileY = tileY;
	}
	
	@Override
	public void draw(CoOrdTranslator cot) {
		Point imageCoOrds = cot.objectToScreenCoOrds(getTopLeft());
		Rectangle bounding = getRectangle();
		Rectangle imageBoundingRectangle = bounding.scale(1.0f/tileX, 1.0f/tileY);
		Rectangle offset = cot.objectToScreenCoOrds(imageBoundingRectangle);
		for (int y = 0; y < tileY; y++) {
			for (int x = 0; x < tileX; x++) {
				getImage().draw(imageCoOrds.getX() + offset.getWidth() * x,
						imageCoOrds.getY() + offset.getHeight() * y,
						cot.getDrawScale());
			}
		}
	}

	@Override
	public void update(int delta) {
		image.update(delta);
	}

	@Override
	public void setMirrored(boolean mirrored) {
		image.setMirrored(mirrored);
	}

	@Override
	public LayeredImage getImage() {
		return image;
	}

}
