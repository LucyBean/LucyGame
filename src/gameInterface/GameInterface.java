package gameInterface;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.WorldObject;
import worlds.ObjectLayer;
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
	ObjectLayer<InterfaceElement> allStateInterface;
	StatusWindow statusWindow;
	World world;
	List<MenuSet> menus;

	public GameInterface() {
		interfaces = new ObjectLayerSet<InterfaceElement>();
		allStateInterface = new ObjectLayer<InterfaceElement>();
		menus = new ArrayList<MenuSet>();
	}

	/**
	 * Sets the World of the GameInterface and all contained InterfaceElements.
	 * 
	 * @param w
	 */
	public void setWorld(final World w) {
		world = w;
		interfaces.applyToAllObjects(a -> a.setWorld(w));
		allStateInterface.applyToAll(a -> a.setWorld(w));
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
		if (ie instanceof MenuSet) {
			menus.add((MenuSet) ie);
		}
	}

	/**
	 * Adds the InterfaceElement to all interface states.
	 * 
	 * @param ie
	 */
	public void addToAll(InterfaceElement ie) {
		allStateInterface.add(ie);
		if (ie instanceof MenuSet) {
			menus.add((MenuSet) ie);
		}
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
			statusWindow = new StatusWindow(new Point(340, 0));
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
	public void mousePressed(int button, Point clickPoint, WorldState state) {
		interfaces.applyToLayerObjects(a -> a.mousePressed(button, clickPoint),
				state.ordinal());
		allStateInterface.applyToAll(a -> a.mousePressed(button, clickPoint));
	}

	/**
	 * Propagates the keyPressed update to all InterfaceElements within the
	 * currently active interface.
	 * 
	 * @param keycode
	 */
	public void keyPressed(int keycode, WorldState state) {
		interfaces.applyToLayerObjects(a -> a.keyPressed(keycode), state.ordinal());
		allStateInterface.applyToAll(a -> a.keyPressed(keycode));
	}

	/**
	 * Renders the required interface for the current WorldState.
	 * 
	 * @param state
	 *            The WorldState for which the interface should be rendered.
	 */
	public void render(WorldState state) {
		interfaces.render(state.ordinal());
		allStateInterface.render();
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
		allStateInterface.update(gc, delta);
	}

	/**
	 * Resets the menus to their original state (i.e. closes all sub-menus)
	 */
	public void resetMenus() {
		menus.stream().forEach(m -> m.reset());
	}
}
