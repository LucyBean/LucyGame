package gameInterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import objects.WorldObject;

class StatusWindow extends InterfaceElement {
	WorldObject watching;
	static Point textTopLeft = new Point(5,5);

	public StatusWindow(Point origin) {
		super(new Rectangle(origin, 300, 200));
		setBackground(new Color(240, 220, 240, 200));
	}

	@Override
	public void update(GameContainer gc, int delta) {
		updateStatus(delta);
	}

	/**
	 * Sets the StatusWindow to watch the Actor a. The status of a will be
	 * displayed in the status window.
	 * 
	 * @param a
	 */
	public void setWatching(WorldObject a) {
		watching = a;
	}

	private void updateStatus(int delta) {
		if (watching == null) {
			setText("Watching nothing", textTopLeft);
		} else {

			setText("Watching [" + watching.getClass().getSimpleName() + "]\n"
					+ "Pos: " + watching.getPosition(), textTopLeft);
		}
	}

	@Override
	public void onClick(int button) {

	}

}
