package objects.gameInterface;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import objects.world.Actor;
import objects.world.ActorState;
import objects.world.Lockable;
import objects.world.Locker;
import objects.world.WorldObject;
import objects.world.characters.NPC;

class StatusWindow extends InterfaceElement {
	WorldObject watching;
	static Point textTopLeft = new Point(5,5);

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
			String text = "Watching [" + watching.getClass().getSimpleName() + "]\n";
			
			if (watching.isEnabled()) {
				text += "Pos: " + watching.getPosition() + "\n";
			} else {
				text += "Disabled\n";
			}
			
			if (watching instanceof Actor) {
				Actor a = (Actor) watching;
				ActorState state = a.getState();
				text += "State: " + state + "\n";
				Actor pushTarget = a.getPushTarget();
				text += "Pushing: " + pushTarget + "\n";
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
