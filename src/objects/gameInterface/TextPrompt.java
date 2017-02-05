package objects.gameInterface;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import objects.images.Sprite;
import objects.images.SpriteBuilder;
import objects.images.TextImage;

public class TextPrompt extends InterfaceElement {
	private TextImage timg;
	private boolean focused = false;
	private boolean cursorOn = false;
	private int cursorTimerReset = 500;
	private int cursorTimer = 0;
	String s = "";

	public TextPrompt(Point origin) {
		super(origin, SpriteBuilder.makeTextPrompt(150, 10));
		timg = (TextImage) getSprite().getImage().getLayer(2).getImage();
	}

	@Override
	public void mousePressed(int button, Point clickPoint) {
		if (isVisible()) {
			Sprite sprite = getSprite();
			if (sprite != null) {
				Rectangle rect = sprite.getRectangle();
				clickPoint = getCoOrdTranslator().screenToObjectCoOrds(
						clickPoint);
				if (rect.contains(clickPoint)) {
					focused = true;
				} else {
					focused = false;
					removeCursor();
					cursorTimer = 0;
				}
			}
		}
	}
	
	private void addCursor() {
		if (!cursorOn) {
			cursorOn = true;
			s += "_";
			timg.setText(s);
		}
	}
	
	private void removeCursor() {
		if (cursorOn) {
			cursorOn = false;
			s = s.substring(0, s.length() - 1);
			timg.setText(s);
		}
	}

	@Override
	public void update(GameContainer gc, int delta) {
		if (focused) {
			cursorTimer -= delta;
			if (cursorTimer <= 0) {
				cursorTimer = cursorTimerReset;
				if (cursorOn) {
					removeCursor();
				} else {
					addCursor();
				}
			}
		}
	}
	
	@Override
	public void keyPressed(int keycode) {
		
	}
}
