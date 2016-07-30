package objectLibrary;

import helpers.Point;
import helpers.Rectangle;
import objectLibs.SpriteLibrary;
import objects.Collider;
import objects.Sprite;
import objects.Static;
import worlds.Camera;
import worlds.GlobalOptions;
import worlds.WorldLayer;

public class Wall extends Static {
	final int width;
	final int height;

	public Wall(Point origin, int width, int height) {
		super(origin, WorldLayer.WORLD, SpriteLibrary.WALL,
				new Collider(Point.ZERO, width, height), null);
		this.width = width;
		this.height = height;
	}

	@Override
	protected void resetStaticState() {

	}

	/**
	 * Draws the Wall object.
	 */
	@Override
	public void draw(Camera camera) {
		// This must be overriden as drawing the wall involves drawing
		// multiple Wall sprites
		Sprite sprite = getSprite();
		Rectangle imageRect = sprite.getBoundingRectangle();
		Rectangle offset = objectToScreenCoOrds(
				imageRect.scaleAboutOrigin(1.0f / GlobalOptions.GRID_SIZE),
				camera);
		Point imageCoOrds = objectToScreenCoOrds(sprite.getOrigin(), camera);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sprite.getImage().draw(
						imageCoOrds.getX() + offset.getWidth() * x,
						imageCoOrds.getY() + offset.getHeight() * y,
						getDrawScale(camera));
			}
		}

		drawCollider(camera);
		drawInteractBox(camera);
	}
}
