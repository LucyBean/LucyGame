package gameInterface;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;

import helpers.Function;
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
	 * Gets the ObjectLayer at index layer.
	 * 
	 * @param layer
	 * @return
	 */
	public ObjectLayer<T> get(int layer) {
		return ols.get(layer);
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
	 * Propagates update signal to all ObjectLayers within the ObjectLayerSet.
	 */
	public void update(final GameContainer gc, final int delta) {
		applyToAllLayers(new Function<ObjectLayer<T>>() {
			@Override
			public void exec(ObjectLayer<T> olt) {
				olt.update(gc,  delta);
			}
		});
	}
}
