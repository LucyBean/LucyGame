package gameInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.InterfaceElement;
import objects.WorldObject;
import worlds.ObjectLayer;
import worlds.World;
import worlds.WorldState;

public class GameInterface {
	List<ObjectLayer<InterfaceElement>> interfaces;
	StatusWindow statusWindow;
	World world;

	public GameInterface(World world) {
		interfaces = new ArrayList<ObjectLayer<InterfaceElement>>();
		for (int i = 0; i < WorldState.values().length; i++) {
			interfaces.add(new ObjectLayer<InterfaceElement>());
		}
		this.world = world;
	}

	/**
	 * Adds an InterfaceElement to the interface for a specified WorldState.
	 * 
	 * @param ie
	 *            The InterfaceElement to be added.
	 * @param state
	 *            The WorldState for which the InterfaceElement should appear.
	 *            Setting this to null will cause it to be added for all
	 *            WorldStates.
	 */
	public void add(InterfaceElement ie, WorldState state) {
		if (state != null) {
			interfaces.get(state.ordinal()).add(ie);
			ie.setWorld(world);
		} else {
			Iterator<ObjectLayer<InterfaceElement>> ii = interfaces.iterator();
			while (ii.hasNext()) {
				ObjectLayer<InterfaceElement> olie = ii.next();
				olie.add(ie);
			}
		}
	}

	public void add(InterfaceElement ie) {
		add(ie, null);
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
			add(statusWindow);
		}
	}

	public void mousePressed(int button, Point clickPoint, WorldState state) {
		Iterator<InterfaceElement> oli = interfaces.get(
				state.ordinal()).iterator();

		while (oli.hasNext()) {
			InterfaceElement go = oli.next();
			go.mousePressed(button, clickPoint);
		}
	}

	/**
	 * Renders the required interface for the current WorldState.
	 * 
	 * @param state
	 *            The WorldState for which the interface should be rendered.
	 */
	public void render(WorldState state) {
		interfaces.get(state.ordinal()).render();
	}

	/**
	 * Propagates updates to all objects on this layer.
	 * 
	 * @param gc
	 *            GameContainer object to use.
	 * @param delta
	 *            Time difference.
	 */
	public void update(GameContainer gc, int delta, WorldState state) {
		interfaces.get(state.ordinal()).update(gc, delta);
	}
}
