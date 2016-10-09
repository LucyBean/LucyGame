package gameInterface;

import org.newdawn.slick.Color;

import helpers.Point;
import helpers.Rectangle;
import images.LayeredImage;
import images.LucyImage;
import images.Sprite;
import images.SpriteBuilder;
import objects.GameObject;

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

	/**
	 * @param button
	 *            The button used to click.
	 * @param clickPoint
	 *            The position of the mouse click in screen co-ordinates.
	 */
	public void mousePressed(int button, Point clickPoint) {
		if (isVisible()) {
			Sprite sprite = getSprite();
			if (sprite != null) {
				Rectangle rect = sprite.getRectangle();
				clickPoint = getCoOrdTranslator().screenToObjectCoOrds(
						clickPoint);
				if (rect.contains(clickPoint)) {
					onClick(button, clickPoint);
				}
			}
		}
	}

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

	protected void setBackground(LucyImage img) {
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

	/**
	 * This is called when the object is clicked on.
	 * 
	 * @param button
	 *            The mouse button used to click the object.
	 * @param clickPoint
	 *            The position of the mouse click in object co-ordinates.
	 */
	public void onClick(int button, Point clickPoint) {

	}

	/**
	 * This is called for the object when a key is pressed.
	 * 
	 * @param keycode
	 *            The keycode of the key pressed.
	 */
	public void keyPressed(int keycode) {

	}
}
