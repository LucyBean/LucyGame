package gameInterface;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;

import helpers.Function;
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
	public void applyToAllObjects(Function<T> f) {
		for (int i : ols.keySet()) {
			ObjectLayer<T> ol = ols.get(i);
			ol.applyToAll(f);
		}
	}

	/**
	 * Applies the Function f to all ObjectLayers within the set. Use this to
	 * add an object to all layers within the set.
	 * 
	 * @param f
	 */
	public void applyToAllLayers(Function<ObjectLayer<T>> f) {
		for (int i : ols.keySet()) {
			ObjectLayer<T> ol = ols.get(i);
			f.exec(ol);
		}
	}

	public void applyToLayer(Function<ObjectLayer<T>> f, int index) {
		if (ols.get(index) != null) {
			f.exec(ols.get(index));
		}
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
		applyToAllLayers(new Function<ObjectLayer<T>>() {
			@Override
			public void exec(ObjectLayer<T> olt) {
				olt.render();
			}
		});
	}

	/**
	 * Renders the ObjectLayer with index layer.
	 * 
	 * @param layer
	 *            The index of the layer to be rendered.
	 */
	public void render(int layer) {
		if (ols.get(layer) != null) {
			ols.get(layer).render();
		}
	}

	/**
	 * Propagates update signal to all ObjectLayers within the ObjectLayerSet.
	 */
	public void update(final GameContainer gc, final int delta) {
		applyToAllLayers(new Function<ObjectLayer<T>>() {
			@Override
			public void exec(ObjectLayer<T> olt) {
				olt.update(gc, delta);
			}
		});
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
