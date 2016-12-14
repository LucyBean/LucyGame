package objects;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import images.Sprite;
import worlds.World;

public abstract class GameObject {
	private Point position;
	private Sprite sprite;
	private World world;
	private final CoOrdTranslator cot;

	private boolean enabled = true;
	private boolean visible = true;

	public GameObject(Point position, Sprite sprite) {
		this.position = position;
		setSprite(sprite);
		cot = new CoOrdTranslator(this);
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

	/**
	 * Returns the current position of the object. Will return null if the
	 * object is disabled.
	 * 
	 * @return
	 */
	public Point getPosition() {
		if (isEnabled()) {
			return position;
		} else {
			return null;
		}
	}

	public CoOrdTranslator getCoOrdTranslator() {
		return cot;
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
		addedToWorld(world);
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		if (sprite != null) {
			sprite.setObject(this);
		}
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
	final public void render() {
		if (isEnabled()) {
			draw();
		}
	}

	/**
	 * This method will be called by render if the GameObject is enabled.
	 */
	protected void draw() {
		if (isVisible() && sprite != null) {
			sprite.draw();
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
		if (sprite != null) {
			getSprite().update(delta);
		}
	}

	/**
	 * Gives this GameObject's Sprite's bounding rectangle in screen
	 * co-ordinates.
	 * 
	 * @return The Sprite's bounding rectangle in screen co-ordinates.
	 */
	public Rectangle getSpriteRectangleScreenCoOrds() {
		if (sprite != null) {
			Rectangle r = getCoOrdTranslator().objectToScreenCoOrds(
					getSprite().getRectangle());
			return r;
		} else {
			return null;
		}
	}

	/**
	 * Called when this GameObject has been added to the World.
	 * 
	 * @param w
	 */
	public void addedToWorld(World w) {

	}

	/**
	 * Called when this GameObject's Sprite has changed image.
	 */
	public void statedSpriteImageChange() {

	}

	/**
	 * This is called for the object when a key is pressed.
	 * 
	 * @param keycode
	 *            The keycode of the key pressed.
	 */
	public void keyPressed(int keycode) {

	}
}
