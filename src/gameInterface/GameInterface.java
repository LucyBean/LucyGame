package gameInterface;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.InterfaceElement;
import objects.WorldObject;
import worlds.World;
import worlds.WorldState;

/**
 * Class for building GameInterfaces.
 * 
 * @author Lucy
 *
 */
public class GameInterface {
	ObjectLayerSet<InterfaceElement> interfaces;
	StatusWindow statusWindow;
	World world;

	public GameInterface() {
		interfaces = new ObjectLayerSet<InterfaceElement>();
	}

	/**
	 * Sets the World of the GameInterface and all contained InterfaceElements.
	 * 
	 * @param w
	 */
	public void setWorld(final World w) {
		world = w;
		interfaces.applyToAllObjects(a -> a.setWorld(w));
	}

	/**
	 * Adds an InterfaceElement to the interface for a specified WorldState.
	 * 
	 * @param ie
	 *            The InterfaceElement to be added.
	 * @param state
	 *            The WorldState for which the InterfaceElement should appear.
	 */
	public void add(InterfaceElement ie, WorldState state) {
		if (state != null) {
			interfaces.add(ie, state.ordinal());
			ie.setWorld(world);
		}
	}

	/**
	 * Adds the InterfaceElement to all interface states.
	 * 
	 * @param ie
	 */
	public void addToAll(InterfaceElement ie) {
		interfaces.addToAll(ie);
	}

	/**
	 * Sets the watch target for the GameInterface's StatusWindow. This will
	 * also enable the StatusWindow.
	 * 
	 * @param a
	 */
	public void setWatchTarget(WorldObject a) {
		enableStatusWindow();
		statusWindow.setWatching(a);
	}

	public void enableStatusWindow() {
		if (statusWindow == null) {
			statusWindow = new StatusWindow(new Point(440, 0));
			addToAll(statusWindow);
		}
	}

	/**
	 * Propagates the mousePressed update to all InterfaceElements within the
	 * currently active interface.
	 * 
	 * @param button
	 * @param clickPoint
	 * @param state
	 */
	public void mousePressed(final int button, final Point clickPoint,
			final WorldState state) {
		// TODO: Look at how to build this using specialised templates.
		// I want an interface.mousePressed(...) method.
		interfaces.applyToLayerObjects(a -> a.mousePressed(button, clickPoint),
				state.ordinal());
	}

	/**
	 * Renders the required interface for the current WorldState.
	 * 
	 * @param state
	 *            The WorldState for which the interface should be rendered.
	 */
	public void render(WorldState state) {
		interfaces.render(state.ordinal());
	}

	/**
	 * Propagates updates to all objects on this layer.
	 * 
	 * @param gc
	 *            GameContainer object to use.
	 * @param delta
	 *            Time difference.
	 */
	public void update(final GameContainer gc, final int delta,
			WorldState state) {
		interfaces.update(gc, delta, state.ordinal());
	}
}
