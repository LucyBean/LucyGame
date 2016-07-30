package objects;

import helpers.Point;
import helpers.Rectangle;
import worlds.Camera;
import worlds.GlobalOptions;
import worlds.WorldLayer;

public abstract class WorldObject extends GameObject {
	//
	// Positioning
	// Position will be the object's position in the world. This should be set
	// to
	// the top-left point of the collider (for solids) or image (for
	// non-solids).
	//
	private Collider collider;
	private InteractBox interactBox;
	private WorldLayer layer;

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
	public WorldObject(Point origin, WorldLayer layer, Sprite sprite,
			Collider collider, InteractBox interactBox) {
		super(origin, sprite);
		this.collider = collider;
		this.interactBox = interactBox;
		this.layer = layer;

		reset();
	}

	public WorldObject(Point origin, WorldLayer layer, Sprite sprite) {
		this(origin, layer, sprite, null, null);
	}

	public WorldObject(Point origin, WorldLayer layer) {
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

	public Collider getCollider() {
		return collider;
	}

	public InteractBox getInteractBox() {
		return interactBox;
	}

	public WorldLayer getLayer() {
		return layer;
	}

	/**
	 * Enables the object, causing it to to be rendered. Enabled Actors will
	 * receive updates.
	 */
	public void enable() {
		super.enable();
		getWorld().addToActiveLists(this);
		resetState();
	}

	/**
	 * Disables the object, preventing it from being rendered. Disabled Actors
	 * will not receive updates.
	 */
	public void disable() {
		super.disable();
		getWorld().removeFromActiveLists(this);
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
	@Override
	public void draw(Camera camera) {
		// Draws the sprite
		draw(camera, 1, 1);

	}
	
	@Override
	public void draw(Camera camera, int tileX, int tileY) {
		// Draw the sprite
		super.draw(camera, tileX, tileY);
		
		// Draws collider and interact boxes
		drawCollider(camera);
		drawInteractBox(camera);
	}
	
	/**
	 * Draws the Collider for this object if it is set to be shown.
	 * @param camera
	 */
	final protected void drawCollider(Camera camera) {
		if (collider != null) {
			if (GlobalOptions.DRAW_ALL_COLLIDERS
					|| GlobalOptions.DRAW_INVIS_OBJ_COLLIDERS
							&& (getSprite() == null || !isVisible())) {
				// Draw collider
				Point colliderCoOrds = objectToScreenCoOrds(
						collider.getTopLeft(), camera);
				collider.getImage().draw(colliderCoOrds.getX(),
						colliderCoOrds.getY(), getDrawScale(camera));
			}
		}
	}
	
	/**
	 * Draws the InteractBox for this object if it is set to be shown.
	 * @param camera
	 */
	@SuppressWarnings("unused")
	final protected void drawInteractBox(Camera camera) {
		if (GlobalOptions.DRAW_INTERACT_BOXES && interactBox != null) {
			// Draw interact box
			Point interactCoOrds = objectToScreenCoOrds(
					interactBox.getTopLeft(), camera);
			interactBox.getImage().draw(interactCoOrds.getX(),
					interactCoOrds.getY(), getDrawScale(camera));
		}
	}

	/**
	 * Translates a point from object co-ords to world co-ords.
	 * 
	 * @param point
	 * @return
	 */
	public Point objectToWorldCoOrds(Point point) {
		return point.move(getPosition());
	}

	public Rectangle objectToWorldCoOrds(Rectangle rect) {
		return rect.translate(getPosition());
	}

	/**
	 * Translates a point from object co-ords to camera co-ords.
	 * 
	 * @param point
	 * @return
	 */
	@Override
	public Point objectToScreenCoOrds(Point point, Camera camera) {
		WorldLayer layer = getLayer();
		// do not translate interface layer objects
		return objectToWorldCoOrds(point).move(camera.getLocation().scale(
				layer.getParallaxX(), layer.getParallaxY()).neg()).scale(
						camera.getScale() * GlobalOptions.GRID_SIZE);
	}

	@Override
	public Rectangle objectToScreenCoOrds(Rectangle rect, Camera camera) {
		Point origin = objectToScreenCoOrds(rect.getTopLeft(), camera);
		float width = rect.getWidth() * camera.getScale()
				* GlobalOptions.GRID_SIZE;
		float height = rect.getHeight() * camera.getScale()
				* GlobalOptions.GRID_SIZE;
		return new Rectangle(origin, width, height);
	}

	@Override
	public float getDrawScale(Camera camera) {
		return camera.getScale();
	}

	@Override
	public String toString() {
		return "[" + getClass().getName() + " at " + getPosition();
	}
}
