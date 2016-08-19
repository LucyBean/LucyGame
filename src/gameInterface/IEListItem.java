package gameInterface;

import org.newdawn.slick.Image;

import helpers.Point;
import helpers.Rectangle;
import images.Sprite;

public abstract class IEListItem extends InterfaceElement {
	
	public IEListItem(Sprite sprite) {
		super(Point.ZERO, sprite);
	}
	
	public IEListItem(Image backgroundImage) {
		super(new Rectangle(Point.ZERO, backgroundImage.getWidth(), backgroundImage.getHeight()));
		setBackground(backgroundImage);
	}

	public abstract float getHeight();
}
