package worlds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;

import objects.GameObject;

public class ObjectLayer<T extends GameObject> {
	List<T> objects;
	boolean visible = true;

	public ObjectLayer() {
		objects = new ArrayList<T>();
	}

	public void add(T go) {
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
	public void render(Camera camera) {
		Iterator<T> oli = objects.iterator();
		while (oli.hasNext()) {
			GameObject go = oli.next();
			go.render(camera);
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
		Iterator<T> oli = objects.iterator();
		while (oli.hasNext()) {
			GameObject go = oli.next();
			go.update(gc, delta);
		}
	}

	public Iterator<T> iterator() {
		return objects.iterator();
	}
}
