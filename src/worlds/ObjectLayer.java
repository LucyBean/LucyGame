package worlds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import objects.GameObject;
import objects.InterfaceElement;

public class ObjectLayer {
	List<GameObject> objects;
	boolean visible = true;

	public ObjectLayer() {
		objects = new ArrayList<GameObject>();
	}

	public void add(GameObject go) {
		objects.add(go);
	}

	public boolean isVisible() {
		return visible;
	}

	/**
	 * Renders all the objects on this layer.
	 * 
	 * @param gc
	 *            GameContainer object to use.
	 * @param g
	 *            Graphics object.
	 * @param camera
	 *            The current world camera.
	 */
	public void render(GameContainer gc, Graphics g, Camera camera) {
		Iterator<GameObject> oli = objects.iterator();
		while (oli.hasNext()) {
			GameObject go = oli.next();
			go.render(gc, g, camera);
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
		Iterator<GameObject> oli = objects.iterator();
		while (oli.hasNext()) {
			GameObject go = oli.next();
			go.update(gc, delta);
		}
	}

	public Iterator<GameObject> iterator() {
		return objects.iterator();
	}
}
