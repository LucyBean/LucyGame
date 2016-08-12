package objects;

import helpers.Point;
import options.GlobalOptions;
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
	 * This is called on an object when a WorldObject starts overlapping it.
	 * Override this to react to "on entry" events.
	 * 
	 * @param a
	 *            The WorldObject that has started overlapping this.
	 */
	public void overlapStart(WorldObject a) {
	}

	/**
	 * This is called on an object when the a WorldObject stops overlapping it.
	 * Override this to react to "on exit" events.
	 * 
	 * @param a
	 *            The WorldObject that has stopped overlapping this.
	 */
	public void overlapEnd(WorldObject a) {
	}

	//
	// Render
	//
	@Override
	public void draw() {
		// Draw the sprite
		super.draw();

		// Draws collider and interact boxes
		if (getCollider() != null) {
			if (GlobalOptions.drawAllColliders()
					|| GlobalOptions.drawInvisObjColliders()
					&& (getSprite() == null || !isVisible())) {
				getCollider().draw(getCoOrdTranslator());
			}
		}
		if (interactBox != null && GlobalOptions.drawInteractBoxes()) {
			getInteractBox().draw(getCoOrdTranslator());
		}
	}

	@Override
	public String toString() {
		if (isEnabled()) {
			return getClass().getSimpleName() + " at " + getPosition();
		} else {
			return getClass().getSimpleName();
		}
	}
}
