package objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import helpers.Point;
import helpers.Rectangle;
import worlds.Camera;
import worlds.GlobalOptions;
import worlds.World;
import worlds.WorldLayer;

public abstract class GameObject {
	//
	// Positioning
	// Position will be the object's position in the world. This should be set
	// to
	// the top-left point of the collider (for solids) or image (for
	// non-solids).
	//
	private Point position;
	private Sprite sprite;
	private Collider collider;
	private InteractBox interactBox;
	private World world;
	private WorldLayer layer;

	boolean enabled = true;
	boolean visible = true;

	/**
	 * Creates a new GameObject. Origin points for sprite, collider, and
	 * interact box should be set in object co-ords (i.e. relative to the
	 * GameObject's origin point).
	 * 
	 * @param origin
	 *            Top-left point of object in world co-ordinates. Should be set
	 *            to top-left of collider for solids or top-left of sprite for
	 *            non-solids.
	 * @param sprite
	 *            The image drawn in the world to represent the object.
	 * @param collider
	 *            Rectangle used for collision checking.
	 * @param interactBox
	 *            Rectangle used for interacting with the object.
	 */
	public GameObject(Point origin, WorldLayer layer, Sprite sprite,
			Collider collider, InteractBox interactBox) {
		this.position = origin;
		this.sprite = sprite;
		this.collider = collider;
		this.interactBox = interactBox;
		this.layer = layer;

		reset();
	}

	public GameObject(Point origin, WorldLayer layer, Sprite sprite) {
		this(origin, layer, sprite, null, null);
	}

	public GameObject(Point origin, WorldLayer layer) {
		this(origin, layer, null, null, null);
	}

	/**
	 * Resets an object to its initial state.
	 */
	public final void reset() {
		resetState();
	}

	protected abstract void resetState();

	// TODO
	// Getters
	//
	public boolean isSolid() {
		return collider != null;
	}

	public boolean isInteractable() {
		return interactBox != null;
	}

	public World getWorld() {
		return world;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Collider getCollider() {
		return collider;
	}

	public InteractBox getInteractBox() {
		return interactBox;
	}

	public Point getPosition() {
		return position;
	}

	public WorldLayer getLayer() {
		return layer;
	}

	public boolean isEnabled() {
		return enabled;
	}

	//
	// Setters
	//
	public void setWorld(World world) {
		this.world = world;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * Enables the object, causing it to to be rendered. Enabled Actors will
	 * receive updates.
	 */
	public void enable() {
		enabled = true;
		getWorld().addToActiveLists(this);
		resetState();
	}

	/**
	 * Disables the object, preventing it from being rendered. Disabled Actors
	 * will not receive updates.
	 */
	public void disable() {
		enabled = false;
		getWorld().removeFromActiveLists(this);
	}

	public void setVisibility(boolean visible) {
		this.visible = visible;
	}

	// TODO
	// Interaction and reactions
	//
	public void interactedBy(Actor a) {

	}

	/**
	 * This is called on an object when the Actor a starts overlapping it.
	 * Override this to react to "on entry" events.
	 * 
	 * @param a
	 *            The Actor that has started overlapping this.
	 */
	public void overlapStart(Actor a) {
	}

	/**
	 * This is called on an object when the Actor a stops overlapping it.
	 * Override this to react to "on exit" events.
	 * 
	 * @param a
	 *            The Actor that has stopped overlapping this.
	 */
	public void overlapEnd(Actor a) {
	}

	//
	// Render
	//
	@SuppressWarnings("unused")
	public void render(GameContainer gc, Graphics g, Camera camera)
			throws InvalidObjectStateException {
		float scale = camera.getScale();
		if (getLayer() == WorldLayer.INTERFACE) {
			scale = 1.0f;
		}
		// The object should be enabled when it is acting
		if (!isEnabled()) {
			throw new InvalidObjectStateException(
					"Attempted to render " + this + " but it is disabled.");
		}
		// Image will be drawn at co-ords:
		// (object origin + image topLeft - camera position)*scale

		if (sprite != null && visible) {
			// Draw image
			Point imageCoOrds = objectToScreenCoOrds(sprite.getOrigin(),
					camera);
			sprite.getImage().draw(imageCoOrds.getX(), imageCoOrds.getY(),
					scale);
		}
		if (collider != null) {
			if (GlobalOptions.DRAW_ALL_COLLIDERS
					|| GlobalOptions.DRAW_INVIS_OBJ_COLLIDERS
							&& (sprite == null || !visible)) {
				// Draw collider
				Point colliderCoOrds = objectToScreenCoOrds(
						collider.getTopLeft(), camera);
				collider.getImage().draw(colliderCoOrds.getX(),
						colliderCoOrds.getY(), scale);
			}
		}
		if (GlobalOptions.DRAW_INTERACT_BOXES && interactBox != null) {
			// Draw interact box
			Point interactCoOrds = objectToScreenCoOrds(
					interactBox.getTopLeft(), camera);
			interactBox.getImage().draw(interactCoOrds.getX(),
					interactCoOrds.getY(), scale);
		}
	}

	/**
	 * Translates a point from object co-ords to world co-ords.
	 * 
	 * @param point
	 * @return
	 */
	public Point objectToWorldCoOrds(Point point) {
		return point.move(position);
	}

	public Rectangle objectToWorldCoOrds(Rectangle rect) {
		return rect.translate(position);
	}

	/**
	 * Translates a point from object co-ords to camera co-ords.
	 * 
	 * @param point
	 * @return
	 */
	public Point objectToScreenCoOrds(Point point, Camera camera) {
		WorldLayer layer = getLayer();
		// do not translate interface layer objects
		if (layer == WorldLayer.INTERFACE) {
			return objectToWorldCoOrds(point);
		} else {
			return objectToWorldCoOrds(point).move(camera.getLocation().scale(
					layer.getParallaxX(), layer.getParallaxY()).neg()).scale(
							camera.getScale() * GlobalOptions.GRID_SIZE);
		}
	}
	
	public Rectangle objectToScreenCoOrds(Rectangle rect, Camera camera) {
		Point origin = objectToScreenCoOrds(rect.getTopLeft(), camera);
		if (getLayer() == WorldLayer.INTERFACE) {
			return new Rectangle(origin, rect.getWidth(), rect.getHeight());
		} else {
			float width = rect.getWidth() * camera.getScale()
					* GlobalOptions.GRID_SIZE;
			float height = rect.getHeight() * camera.getScale()
					* GlobalOptions.GRID_SIZE;
			return new Rectangle(origin, width, height);
		}
	}

	@Override
	public String toString() {
		return "[" + getClass().getName() + " at " + position;
	}
}
