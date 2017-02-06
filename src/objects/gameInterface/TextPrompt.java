package objects.gameInterface;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Point;
import helpers.Rectangle;
import objects.images.ImageBuilder;
import objects.images.Sprite;
import objects.images.SpriteBuilder;
import objects.images.TextImage;

public class TextPrompt extends InterfaceElement {
	private TextImage timg;
	private TextImage pimg;
	private int width;
	private boolean focused = false;
	private boolean cursorOn = false;
	private int cursorTimerReset = 500;
	private int cursorTimer = 0;
	String s = "";

	public TextPrompt(Point origin, int width, String prompt) {
		super(origin, SpriteBuilder.makeTextPrompt(width, 10, 4));
		this.width = width;
		pimg = (TextImage) getSprite().getImage().getLayer(2).getImage();
		pimg.setText(prompt);
		timg = (TextImage) getSprite().getImage().getLayer(3).getImage();
	}
	
	public void setPrompt(String prompt) {
		pimg.setText(prompt);
	}
	
	public void focus() {
		focused = true;
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
	
	private void accept() {
		removeCursor();
		getWorld().acceptInput(s);
		s = "";
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
		if (focused) {
			String input = Input.getKeyName(keycode);
			// Check if it's a letter
			if (input.matches("\\w")) {
				removeCursor();
				// Check if it is beyond the character limit
				Font f = ImageBuilder.getFont();
				if (f.getWidth(s + input + "_") <= width) {
					s += input;
					timg.setText(s);
				}
				addCursor();
				cursorTimer = cursorTimerReset;
			} else if (input.equals("BACK")) {
				// Remove a character if there is one to remove
				removeCursor();
				if (s.length() > 0) {
					s = s.substring(0, s.length() - 1);
					timg.setText(s);
				}
				addCursor();
			} else if (input.equals("RETURN")) {
				accept();
			}
		}
	}
}
