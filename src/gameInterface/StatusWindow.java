package gameInterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import characters.NPC;
import helpers.Point;
import helpers.Rectangle;
import objects.Actor;
import objects.ActorState;
import objects.Lockable;
import objects.Locker;
import objects.WorldObject;

class StatusWindow extends InterfaceElement {
	WorldObject watching;
	static Point textTopLeft = new Point(5,5);

	public StatusWindow(Point origin) {
		super(new Rectangle(origin, 200, 200));
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
			String text = "Watching [" + watching.getClass().getSimpleName() + "]\n";
			
			if (watching.isEnabled()) {
				text += "Pos: " + watching.getPosition() + "\n";
			} else {
				text += "Disabled\n";
			}
			
			if (watching instanceof Actor) {
				ActorState state = ((Actor) watching).getState();
				text += "State: " + state;
			}
			
			if (watching instanceof NPC) {
				int npcID = watching.getNPCID();
				text += "NPC ID: " + npcID + "\n";
			}
			
			if (watching instanceof Locker) {
				int lockID = watching.getLockID();
				text += "Lock ID: " + lockID + "\n";
			}
			
			if (watching instanceof Lockable) {
				int lockID = watching.getLockID();
				text += "Lock ID: " + lockID + "\n";
			}
			
			setText(text, textTopLeft);
		}
	}

	@Override
	public void onClick(int button, Point clickPoint) {
		getWorld().startWatchSelect();
	}

}
