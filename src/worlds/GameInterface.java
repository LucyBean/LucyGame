package worlds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import helpers.Point;
import objects.GameObject;
import objects.InterfaceElement;

public class GameInterface {
	List<ObjectLayer> interfaces;

	public GameInterface() {
		interfaces = new ArrayList<ObjectLayer>();
		for (int i = 0; i < WorldState.values().length; i++) {
			interfaces.add(new ObjectLayer());
		}
	}

	public void add(InterfaceElement ie, WorldState state) {
		interfaces.get(state.ordinal()).add(ie);
	}

	public void mousePressed(int button, Point clickPoint, WorldState state) {
		Iterator<GameObject> oli = interfaces.get(
				state.ordinal()).iterator();

		while (oli.hasNext()) {
			InterfaceElement go = (InterfaceElement) oli.next();
			go.mousePressed(button, clickPoint);
		}
	}

	/**
	 * Renders all the objects on this layer.
	 * 
	 * @param gc
	 *            GameContainer object to use.
	 * @param g
	 *            Graphics object.
	 */
	public void render(GameContainer gc, Graphics g, WorldState state) {
		interfaces.get(state.ordinal()).render(gc, g, null);
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
		interfaces.get(state.ordinal()).update(gc,delta);
	}
}
