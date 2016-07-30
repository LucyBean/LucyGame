package gameInterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import objects.InterfaceElement;
import objects.WorldObject;

class StatusWindow extends InterfaceElement {
	WorldObject watching;

	public StatusWindow(Point origin) {
		super(new Rectangle(origin, 200, 200));
		setBackground(new Color(240, 220, 240, 200));
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
	public void setWatching(WorldObject a) {
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

	@Override
	public void onClick(int button) {

	}

}
