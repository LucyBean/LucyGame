package worlds;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import helpers.Point;

public abstract class GameObject {
	//
	// Positioning
	// Position will be the object's position in the world. This should be set
	// to
	// the top-left point of the collider (for solids) or image (for
	// non-solids).
	//
	protected Point position;
	protected Sprite sprite;
	protected Collider collider;
	protected InteractBox interactBox;
	World world;

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
	
	//
	// Setters
	//
	public void setWorld(World world) {
		this.world = world;
	}
	

	// TODO
	// onInteract functions
	//

	//
	// Render
	//
	@SuppressWarnings("unused")
	public void render(GameContainer gc, Graphics g, Camera camera) {
		// Image will be drawn at co-ords:
		// (object origin + image topLeft - camera position)*scale

		if (sprite != null) {
			// Draw image
			Point imageCoOrds = translateToWorldCoOrds(sprite.getOrigin(), camera);
			sprite.getImage().draw(imageCoOrds.getX(), imageCoOrds.getY(), camera.getScale());
		}
		if (GlobalOptions.DRAW_COLLIDERS && collider != null) {
			// Draw collider
			Point colliderCoOrds = translateToWorldCoOrds(collider.getTopLeft(), camera);
			collider.getImage().draw(colliderCoOrds.getX(), colliderCoOrds.getY(), camera.getScale());
		}
		if (GlobalOptions.DRAW_INTERACT_BOXES && interactBox != null) {
			// Draw interact box
			Point interactCoOrds = translateToWorldCoOrds(interactBox.getTopLeft(), camera);
			interactBox.getImage().draw(interactCoOrds.getX(), interactCoOrds.getY(), camera.getScale());
		}
	}

	/**
	 * Translates a point from object co-ords to world co-ords.
	 * 
	 * @param point
	 * @return
	 */
	private Point translateToWorldCoOrds(Point point, Camera camera) {
		return position.move(point).move(camera.getLocation().neg()).scale(camera.getScale());
	}
}
