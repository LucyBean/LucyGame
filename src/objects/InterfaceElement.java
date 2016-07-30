package objects;

import helpers.Point;
import helpers.Rectangle;

public abstract class InterfaceElement extends GameObject {

	public InterfaceElement(Point position, Sprite sprite) {
		super(position, sprite);
	}

	public void mousePressed(int button, Point clickPoint) {
		Sprite sprite = getSprite();
		if (sprite != null) {
			Rectangle rect = sprite.getBoundingRectangle();
			rect = getCoOrdTranslator().objectToScreenCoOrds(rect);
			if (rect.contains(clickPoint)) {
				onClick(button);
			}
		}
	}

	public abstract void onClick(int button);
	
	
}
