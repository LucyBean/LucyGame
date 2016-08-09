package gameInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.GameObject;
import worlds.ObjectLayer;

public class ObjectLayerSet<T extends GameObject> {
	private Map<Integer, ObjectLayer<T>> ols;

	public ObjectLayerSet() {
		ols = new HashMap<Integer, ObjectLayer<T>>();
	}

	/**
	 * Applies the Function f to all objects in all ObjectLayers in the set. Use
	 * this to render, update, and set the world of all objects.
	 * 
	 * @param f
	 *            A Function to apply. Must take objects of type T and return
	 *            void.
	 */
	public void applyToAllObjects(Consumer<T> f) {
		for (int i : ols.keySet()) {
		ols.get(i).applyToAll(f);
		}
	}

	public void applyToLayerObjects(Consumer<T> f, int index) {
		if (ols.get(index) != null) {
			ols.get(index).applyToAll(f);
		}
	}
	
	public void applyToAllLayers(Consumer<ObjectLayer<T>> f) {
		ols.entrySet().stream().forEach(a -> f.accept(a.getValue()));
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
		ols.putIfAbsent(layer, new ObjectLayer<T>());
		ols.get(layer).add(t);
	}

	/**
	 * Adds an object T to all ObjectLayers within the set.
	 * 
	 * @param t
	 */
	public void addToAll(T t) {
		for (int i : ols.keySet()) {
			ols.get(i).add(t);
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
		if (ols.get(index) != null) {
			ols.get(index).render();
		}
	}

	/**
	 * Propagates update signal to all ObjectLayers within the ObjectLayerSet.
	 */
	public void update(GameContainer gc, int delta) {
		applyToAllLayers(a -> a.update(gc, delta));
	}

	public void update(GameContainer gc, int delta, int layer) {
		if (ols.get(layer) != null) {
			ols.get(layer).update(gc, delta);
		}
	}

	public T findClickedObject(Point p) {
		T clicked = null;

		for (int i : ols.keySet()) {
			ObjectLayer<T> ol = ols.get(i);
			T wo = ol.findClickedObject(p);
			if (wo != null) {
				clicked = wo;
			}
		}

		return clicked;
	}

	public T findClickedObject(Point p, int index) {
		if (ols.get(index) != null) {
			return ols.get(index).findClickedObject(p);
		} else {
			return null;
		}
	}
}
