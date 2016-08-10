package objects;

import org.newdawn.slick.Color;

import helpers.Point;
import helpers.Rectangle;
import objectLibs.SpriteBuilder;

public abstract class InterfaceElement extends GameObject {
	Rectangle rect;

	public InterfaceElement(Rectangle rect) {
		super(rect.getTopLeft(), null);
		Sprite sprite = SpriteBuilder.interfaceElement((int) rect.getWidth(),
				(int) rect.getHeight());
		setSprite(sprite);
	}
	
	protected InterfaceElement() {
		super(null, null);
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

	/**
	 * Sets the background color for the InterfaceElement's sprite.
	 * 
	 * @param c
	 *            The new background color.
	 */
	protected void setBackground(Color c) {
		LayeredImage limg = getSprite().getImage();
		limg.fillLayer(0, c);
	}

	/**
	 * Sets the top-most layer of the InterfaceElement to show String text.
	 * 
	 * @param text
	 *            The String to show.
	 */
	public void setText(String text) {
		LayeredImage limg = getSprite().getImage();
		limg.setText(limg.getTopLayerNumber(), text);
	}
}
