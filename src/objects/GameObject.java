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
	}

	public GameObject(Point origin, Sprite sprite) {
		this(origin, sprite, null, null);
	}

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

	//
	// Setters
	//
	public void setWorld(World world) {
		this.world = world;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	// TODO
	// Interaction and reactions
	//
	public void interactedhBy(Actor go) {
		
	}

	//
	// Render
	//
	public void render(GameContainer gc, Graphics g, Camera camera) {
		// Image will be drawn at co-ords:
		// (object origin + image topLeft - camera position)*scale

		if (sprite != null) {
			// Draw image
			Point imageCoOrds = translateToScreenCoOrds(sprite.getOrigin(), camera);
			sprite.getImage().draw(imageCoOrds.getX(), imageCoOrds.getY(), camera.getScale());
		}
		if (collider != null) {
			if (GlobalOptions.DRAW_ALL_COLLIDERS || GlobalOptions.DRAW_INVIS_OBJ_COLLIDERS
					&& (sprite == null || !sprite.isVisible())) {
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
