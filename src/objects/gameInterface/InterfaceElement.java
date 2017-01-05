package objects.gameInterface;

import helpers.Point;
import helpers.Rectangle;
import objects.GameObject;
import objects.images.ImageBuilder;
import objects.images.LayeredImage;
import objects.images.LucyImage;
import objects.images.Sprite;
import objects.images.SpriteBuilder;

public abstract class InterfaceElement extends GameObject {
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

	protected void setBackground() {
		LayeredImage limg = getSprite().getImage();
		Rectangle rect = limg.getLayer(0).getRectangle();
		LucyImage bg = ImageBuilder.getButtonBackground(rect);
		limg.setLayer(0, bg);
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
	 * Gets the width of this element in pixels.
	 * @return
	 */
	public int getWidthPixels() {
		if (getSprite() != null) {
			return (int) getSprite().getWidth();
		}
		return 0;
	}
	
	/**
	 * Gets the height of this element in pixels.
	 * @return
	 */
	public int getHeightPixels() {
		if (getSprite() != null) {
			return (int) getSprite().getHeight();
		}
		return 0;
	}
}
