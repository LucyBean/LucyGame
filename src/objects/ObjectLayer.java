package objects;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;

public class ObjectLayer<T extends GameObject> extends HashSet<T> {
	/**
	 * No I don't know what this is.
	 */
	private static final long serialVersionUID = 1154685374354625221L;
	boolean visible = true;

	public boolean isVisible() {
		return visible;
	}

	/**
	 * Finds the object, if any, on this layer which contains the point p in
	 * screen co-ordinates.
	 * 
	 * @param p
	 *            The point to be checked given in screen co-ordinates.
	 * @param c
	 *            The current position of the Camera.
	 * @return The first object in the layer (if any) which contains the point.
	 */
	public Optional<T> findClickedObject(Point p) {
		Iterator<T> io = iterator();

		while (io.hasNext()) {
			T wo = io.next();
			if (wo.isEnabled() && wo.getSprite().isPresent()) {
				Rectangle spriteRect = wo.getSprite().get().getRectangle();
				Rectangle spriteWorld = wo.getCoOrdTranslator().objectToScreenCoOrds(spriteRect);
				if (spriteWorld.contains(p)) {
					return Optional.of(wo);
				}
			}
		}

		return Optional.empty();
	}

	/**
	 * Applies a Function f to all objects in the ObjectLayer. Use this to
	 * render, update, and set the world of all objects.
	 * 
	 * @param f
	 *            A Function to apply. Must take objects of type T and return
	 *            void.
	 */
	public void applyToAll(Consumer<T> f) {
		stream().forEach(f);
	}

	/**
	 * Renders all objects on the layer.
	 */
	public void render() {
		if (isVisible()) {
			applyToAll(t -> t.render());
		}
	}

	/**
	 * Propagates the update signal to all objects on the layer.
	 * 
	 * @param gc
	 * @param delta
	 */
	public void update(GameContainer gc, int delta) {
		applyToAll(t -> t.update(gc, delta));
	}
}
