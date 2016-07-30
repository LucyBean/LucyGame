package worlds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import helpers.Point;
import objects.InterfaceElement;

public class GameInterface {
	List<InterfaceElement> objects;
	
	public GameInterface() {
		objects = new ArrayList<InterfaceElement>();
	}
	
	public void add(InterfaceElement ie) {
		objects.add(ie);
	}

	public void mousePressed(int button, Point clickPoint) {
		Iterator<InterfaceElement> oli = objects.iterator();

		while (oli.hasNext()) {
			InterfaceElement go = oli.next();
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
	public void render(GameContainer gc, Graphics g) {
		Iterator<InterfaceElement> oli = objects.iterator();
		while (oli.hasNext()) {
			InterfaceElement go = oli.next();
			go.render(gc, g);
		}
	}

	/**
	 * Propagates updates to all objects on this layer.
	 * 
	 * @param gc
	 *            GameContainer object to use.
	 * @param delta
	 *            Time difference.
	 */
	public void update(GameContainer gc, int delta) {
		// Propagate updates to all objects
		Iterator<InterfaceElement> oli = objects.iterator();
		while (oli.hasNext()) {
			InterfaceElement go = oli.next();
			go.update(gc, delta);
		}
	}
}
