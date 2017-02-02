package objects.gameInterface;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import objects.world.WorldObject;

class StatusWindow extends InterfaceElement {
	private WorldObject watching;
	private static Point textTopLeft = new Point(5,5);

	public StatusWindow(Point origin) {
		super(new Rectangle(origin, 200, 200));
		setBackground(3);
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
			String text = watching.getInfo();
			
			setText(text, textTopLeft);
		}
	}

	@Override
	public void onClick(int button, Point clickPoint) {
		getWorld().startWatchSelect();
	}

}
