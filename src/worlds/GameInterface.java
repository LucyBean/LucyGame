package worlds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.InterfaceElement;

public class GameInterface {
	List<ObjectLayer<InterfaceElement>> interfaces;
	World world;

	public GameInterface(World world) {
		interfaces = new ArrayList<ObjectLayer<InterfaceElement>>();
		for (int i = 0; i < WorldState.values().length; i++) {
			interfaces.add(new ObjectLayer<InterfaceElement>());
		}
		this.world = world;
	}

	public void add(InterfaceElement ie, WorldState state) {
		interfaces.get(state.ordinal()).add(ie);
		ie.setWorld(world);
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
		interfaces.get(state.ordinal()).render(null);
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
