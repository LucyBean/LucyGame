package objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import helpers.Point;
import helpers.Rectangle;
import worlds.Camera;
import worlds.GlobalOptions;
import worlds.World;

public abstract class GameObject {
	//
	// Positioning
	// Position will be the object's position in the world. This should be set
	// to
	// the top-left point of the collider (for solids) or image (for
	// non-solids).
	//
	Point position;
	Sprite sprite;
	Collider collider;
	InteractBox interactBox;
	World world;

	boolean enabled = true;
	boolean visible = true;

	/**
	 * Creates a new GameObject. Origin points for sprite, collider, and interact box should be set
	 * in object co-ords (i.e. relative to the GameObject's origin point).
	 * 
	 * @param origin
	 *            Top-left point of object in world co-ordinates. Should be set to top-left of
	 *            collider for solids or top-left of sprite for non-solids.
	 * @param sprite
	 *            The image drawn in the world to represent the object.
	 * @param collider
	 *            Rectangle used for collision checking.
	 * @param interactBox
	 *            Rectangle used for interacting with the object.
	 */
	public GameObject(Point origin, Sprite sprite, Collider collider, InteractBox interactBox) {
		this.position = origin;
		this.sprite = sprite;
		this.collider = collider;
		this.interactBox = interactBox;

		reset();
	}

	public GameObject(Point origin, Sprite sprite) {
		this(origin, sprite, null, null);
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

	/**
	 * Enables the object, causing it to to be rendered. Enabled Actors will receive updates.
	 */
	public void enable() {
		enabled = true;
		getWorld().addToActiveLists(this);
		resetState();
	}

	/**
	 * Disables the object, preventing it from being rendered. Disabled Actors will not receive
	 * updates.
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
	 * This is called on an object when the Actor a starts overlapping it. Override this to react to
	 * "on entry" events.
	 * 
	 * @param a
	 *            The Actor that has started overlapping this.
	 */
	public void overlapStart(Actor a) {
	}

	/**
	 * This is called on an object when the Actor a stops overlapping it. Override this to react to
	 * "on exit" events.
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
		// The object should be enabled when it is acting
		if (!isEnabled()) {
			throw new InvalidObjectStateException(
					"Attempted to render " + this + " but it is disabled.");
		}
		// Image will be drawn at co-ords:
		// (object origin + image topLeft - camera position)*scale

		if (sprite != null && visible) {
			// Draw image
			Point imageCoOrds = translateToScreenCoOrds(sprite.getOrigin(), camera);
			sprite.getImage().draw(imageCoOrds.getX(), imageCoOrds.getY(), camera.getScale());
		}
		if (collider != null) {
			if (GlobalOptions.DRAW_ALL_COLLIDERS
					|| GlobalOptions.DRAW_INVIS_OBJ_COLLIDERS && (sprite == null || !visible)) {
				// Draw collider
				Point colliderCoOrds = translateToScreenCoOrds(collider.getTopLeft(), camera);
				collider.getImage().draw(colliderCoOrds.getX(), colliderCoOrds.getY(),
						camera.getScale());
			}
		}
		if (GlobalOptions.DRAW_INTERACT_BOXES && interactBox != null) {
			// Draw interact box
			Point interactCoOrds = translateToScreenCoOrds(interactBox.getTopLeft(), camera);
			interactBox.getImage().draw(interactCoOrds.getX(), interactCoOrds.getY(),
					camera.getScale());
		}
	}

	/**
	 * Translates a point from object co-ords to world co-ords.
	 * 
	 * @param point
	 * @return
	 */
	protected Point translateToWorldCoOrds(Point point) {
		return point.move(position);
	}

	protected Rectangle translateToWorldCoOrds(Rectangle rect) {
		return rect.translate(position);
	}

	/**
	 * Translates a point from object co-ords to camera co-ords.
	 * 
	 * @param point
	 * @return
	 */
	protected Point translateToScreenCoOrds(Point point, Camera camera) {
		return translateToWorldCoOrds(point).move(camera.getLocation().neg()).scale(
				camera.getScale());
	}

	protected Rectangle translateToScreenCoOrds(Rectangle rect, Camera camera) {
		return translateToWorldCoOrds(rect).translate(camera.getLocation().neg()).scaleAboutOrigin(
				camera.getScale());
	}

	@Override
	public String toString() {
		return "[" + getClass().getName() + " at " + position;
	}
}
