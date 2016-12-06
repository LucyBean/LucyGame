package objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

import org.newdawn.slick.GameContainer;

import helpers.Point;

public class ObjectLayerSet<T extends GameObject>
		extends HashMap<Integer, ObjectLayer<T>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2821732972727075089L;

	/**
	 * Applies the Function f to all objects in all ObjectLayers in the set. Use
	 * this to render, update, and set the world of all objects.
	 * 
	 * @param f
	 *            A Function to apply. Must take objects of type T and return
	 *            void.
	 */
	public void applyToAllObjects(Consumer<T> f) {
		for (int i : keySet()) {
			get(i).applyToAll(f);
		}
	}

	public void applyToLayerObjects(Consumer<T> f, int index) {
		if (get(index) != null) {
			get(index).applyToAll(f);
		}
	}

	public void applyToAllLayers(Consumer<ObjectLayer<T>> f) {
		values().stream().forEach(a -> f.accept(a));
	}

	public Collection<T> getAll() {
		Collection<T> objects = new HashSet<T>();
		values().stream().forEach(a -> objects.addAll(a));
		return objects;
	}

	/**
	 * Adds an object T to the ObjectLayer number layer. Will add a new
	 * ObjectLayer if that layer is absent.
	 * 
	 * @param t
	 *            The object to be added.
	 * @param layer
	 *            The number of the layer to which t will be added.
	 */
	public void add(T t, int layer) {
		putIfAbsent(layer, new ObjectLayer<T>());
		get(layer).add(t);
	}

	/**
	 * Removes an object T from all ObjectLayers in which it is present.
	 * 
	 * @param t
	 */
	public void remove(T t) {
		applyToAllLayers(l -> l.remove(t));
	}

	/**
	 * Adds an object T to all ObjectLayers within the set.
	 * 
	 * @param t
	 */
	public void addToAll(T t) {
		for (int i : keySet()) {
			get(i).add(t);
		}
	}

	/**
	 * Renders all ObjectLayers within the ObjectLayerSet.
	 */
	public void render() {
		applyToAllLayers(a -> a.render());
	}

	/**
	 * Renders the ObjectLayer with index layer.
	 * 
	 * @param index
	 *            The index of the layer to be rendered.
	 */
	public void render(int index) {
		if (get(index) != null) {
			get(index).render();
		}
	}

	/**
	 * Propagates update signal to all ObjectLayers within the ObjectLayerSet.
	 */
	public void update(GameContainer gc, int delta) {
		applyToAllLayers(a -> a.update(gc, delta));
	}

	public void update(GameContainer gc, int delta, int layer) {
		if (get(layer) != null) {
			get(layer).update(gc, delta);
		}
	}

	/**
	 * Finds the first object that is currently under the mouse on any layer.
	 * 
	 * @param p
	 * @return
	 */
	public T findClickedObject(Point p) {
		T clicked = null;

		for (int i : keySet()) {
			ObjectLayer<T> ol = get(i);
			T wo = ol.findClickedObject(p);
			if (wo != null) {
				clicked = wo;
			}
		}

		return clicked;
	}

	/**
	 * Finds the first object that is currently under the mouse on the required
	 * layer.
	 * 
	 * @param p
	 * @param index
	 * @return
	 */
	public T findClickedObject(Point p, int index) {
		if (get(index) != null) {
			return get(index).findClickedObject(p);
		} else {
			return null;
		}
	}
}
