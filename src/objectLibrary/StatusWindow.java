package objectLibrary;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Point;
import helpers.Rectangle;
import objectLibs.SpriteLibrary;
import objects.Actor;
import objects.InterfaceElement;
import objects.Sprite;

public class StatusWindow extends InterfaceElement {
	static Sprite sprite = SpriteLibrary.createRectangle(
			new Rectangle(Point.ZERO, 300, 200), 1,
			new Color(200, 170, 210, 100));
	Actor watching;

	public StatusWindow(Point origin) {
		super(origin, sprite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(GameContainer gc, int delta) {
		updateStatus();
	}
	

	/**
	 * Sets the StatusWindow to watch the Actor a. The status of a will be
	 * displayed in the status window.
	 * 
	 * @param a
	 */
	public void setWatching(Actor a) {
		watching = a;
	}

	private void updateStatus() {
		if (watching == null) {
			setText("Watching nothing");
		} else {
			setText("Watching " + watching + "\nPos: "
					+ watching.getPosition());
		}
	}

	private void setText(String s) {
		try {
			Image img = getSprite().getImage();
			Graphics g = img.getGraphics();
			g.clear();
			g.setColor(Color.black);
			g.drawString(s, 5, 5);
			g.flush();
			setSprite(new Sprite(img));
		} catch (SlickException se) {
			System.err.println("Tried to add update status for " + watching
					+ " and failed");
			se.printStackTrace();
		}
	}

	@Override
	public void onClick(int button) {
		
	}

}
