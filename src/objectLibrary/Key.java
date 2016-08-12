package objectLibrary;

import helpers.Point;
import objectLibs.SpriteBuilder;
import objects.PickUpItem;

public class Key extends PickUpItem {
	public Key(Point origin, int keyID) {
		super(origin, SpriteBuilder.getKeyImg(keyID));
	}
}
