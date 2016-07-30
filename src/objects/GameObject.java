package objects;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import worlds.Camera;
import worlds.World;

public abstract class GameObject {
	private Point position;
	private Sprite sprite;
	private World world;

	private boolean enabled = true;
	private boolean visible = true;

	public GameObject(Point position, Sprite sprite) {
		this.position = position;
		this.sprite = sprite;
	}

	//
	// Getters
	//
	public World getWorld() {
		return world;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Point getPosition() {
		return position;
	}

	/**
	 * Checks whether the object is enabled. Enabled objects will be rendered
	 * and will receive updates.
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Checks whether the object is visible. Invisible objects will not be
	 * rendered but will still receive updates.
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return visible;
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
	}

	/**
	 * Disables the object, preventing it from being rendered. Disabled Actors
	 * will not receive updates.
	 */
	public void disable() {
		enabled = false;
	}

	public void setVisibility(boolean visible) {
		this.visible = visible;
	}

	//
	// Render
	//
	/**
	 * Renders the image if it is enabled.
	 * 
	 * @param gc
	 * @param g
	 * @param camera
	 */
	final public void render(Camera camera) {
		if (isEnabled()) {
			draw(camera);
		}
	}

	/**
	 * This method will be called by render if it is enabled.
	 * 
	 * @param gc
	 * @param g
	 * @param camera
	 */
	protected void draw(Camera camera) {
		if (isVisible() && sprite != null) {
			// Draw image
			Point imageCoOrds = objectToScreenCoOrds(sprite.getOrigin(),
					camera);
			sprite.getImage().draw(imageCoOrds.getX(), imageCoOrds.getY(),
					getDrawScale(camera));
		}
	}

	/**
	 * Updates the object.
	 * 
	 * @param gc
	 * @param delta
	 *            The change in time.
	 */
	public void update(GameContainer gc, int delta) {

	}

	public abstract Point objectToScreenCoOrds(Point point, Camera camera);

	public abstract Rectangle objectToScreenCoOrds(Rectangle rect,
			Camera camera);

	protected abstract float getDrawScale(Camera camera);
}
