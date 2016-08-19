package gameInterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import helpers.Point;
import helpers.Rectangle;
import objectLibs.SpriteBuilder;
import objects.GameObject;
import objects.LayeredImage;
import objects.Sprite;

public abstract class InterfaceElement extends GameObject {
	Rectangle rect;

	public InterfaceElement(Point point, Sprite sprite) {
		super(point, sprite);
	}
	
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
	
	protected void setBackground(Image img) {
		LayeredImage limg = getSprite().getImage();
		limg.setLayer(0, img);
	}

	/**
	 * Sets the top-most layer of the InterfaceElement to show the text
	 * centered.
	 * 
	 * @param text
	 *            The String to show.
	 */
	public void setTextCentered(String text) {
		LayeredImage limg = getSprite().getImage();
		limg.setTextCentered(limg.getTopLayerNumber(), text);
	}

	/**
	 * Sets the top-most layer of the InterfaceElement to show the text at a
	 * point.
	 * 
	 * @param text
	 *            The String to display.
	 * @param topLeft
	 *            The top-left position of the text.
	 */
	public void setText(String text, Point topLeft) {
		LayeredImage limg = getSprite().getImage();
		limg.setText(limg.getTopLayerNumber(), text, topLeft);
	}
}
