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
import objects.Sprite;
import worlds.WorldLayer;

public class StatusWindow extends Actor {
	public StatusWindow(Point origin) {
		super(origin, WorldLayer.INTERFACE, SpriteLibrary.createRectangle(
				new Rectangle(origin, 300, 300), 1, new Color(230, 130, 230)));
		// TODO Auto-generated constructor stub
	}

	Actor watching;

	@Override
	protected void resetActorState() {
		watching = null;
	}

	@Override
	public void act(GameContainer gc, int delta) {
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
			// draw the string at (imgWidth - w)/2
			g.drawString(s, 5, 5);
			g.flush();
			setSprite(new Sprite(img));
		} catch (SlickException se) {
			System.err.println("Tried to add update status for " + watching
					+ " and failed");
			se.printStackTrace();
		}
	}

}
