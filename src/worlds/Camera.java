package worlds;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import objects.world.WorldObject;
import options.GlobalOptions;

/**
 * Camera class for storing information about the camera.
 * 
 * @author Lucy
 *
 */
public class Camera {
	Point location = new Point(0, 0);
	float scale = 1.0f;
	float speed = 0.01f;
	boolean following = true;

	final float MAX_SCALE = 3.0f;
	final float MIN_SCALE = 0.3f;
	final float SCALE_RATE = 0.01f;
	final float SCROLL_RECTANGLE_WIDTH = 100;
	final int WINDOW_WIDTH = GlobalOptions.WINDOW_WIDTH;
	final int WINDOW_HEIGHT = GlobalOptions.WINDOW_HEIGHT;
	final Point center = new Point(GlobalOptions.WINDOW_WIDTH_GRID / 2,
			GlobalOptions.WINDOW_HEIGHT_GRID / 2);
	final Rectangle noScrollRegion = new Rectangle(
			new Point(SCROLL_RECTANGLE_WIDTH, SCROLL_RECTANGLE_WIDTH),
			WINDOW_WIDTH - SCROLL_RECTANGLE_WIDTH * 2,
			WINDOW_HEIGHT - SCROLL_RECTANGLE_WIDTH * 2);

	WorldObject target;

	public void update(GameContainer gc, int delta) {
		Input input = gc.getInput();
		// Manula control if there is no target or set to not following
		if (target == null || !following) {
			// Move the camera around the world.
			if (input.isKeyDown(Controller.CAMERA_S)) {
				move(Dir.SOUTH, getSpeed() * delta);
			}
			if (input.isKeyDown(Controller.CAMERA_N)) {
				move(Dir.NORTH, getSpeed() * delta);
			}
			if (input.isKeyDown(Controller.CAMERA_W)) {
				move(Dir.WEST, getSpeed() * delta);
			}
			if (input.isKeyDown(Controller.CAMERA_E)) {
				move(Dir.EAST, getSpeed() * delta);
			}
		} else {
			// Get position of target on screen
			// tsc = target in screen co-ordinates
			Rectangle tsc = target.getCoOrdTranslator().objectToScreenCoOrds(
					target.getCollider().getRectangle());

			// Check if it the object is outside the noScrollRegion
			if (!noScrollRegion.contains(tsc)) {
				// determine direction
				// x
				if (tsc.getTopLeft().getX() < SCROLL_RECTANGLE_WIDTH) {
					float amount = SCROLL_RECTANGLE_WIDTH
							- tsc.getTopLeft().getX();
					move(Dir.WEST, amount, speed * delta);
				} else if (tsc.getTopRight().getX() > WINDOW_WIDTH
						- SCROLL_RECTANGLE_WIDTH) {
					float amount = tsc.getTopRight().getX()
							- (WINDOW_WIDTH - SCROLL_RECTANGLE_WIDTH);
					move(Dir.EAST, amount, speed * delta);
				}

				// y
				if (tsc.getTopLeft().getY() < SCROLL_RECTANGLE_WIDTH) {
					float amount = SCROLL_RECTANGLE_WIDTH
							- tsc.getTopLeft().getY();
					move(Dir.NORTH, amount, speed * delta);
				} else if (tsc.getBottomLeft().getY() > WINDOW_HEIGHT
						- SCROLL_RECTANGLE_WIDTH) {
					float amount = tsc.getBottomLeft().getY()
							- (WINDOW_HEIGHT - SCROLL_RECTANGLE_WIDTH);
					move(Dir.SOUTH, amount, speed * delta);
				}
			}
		}

		// Zooming

		if (input.isKeyDown(Controller.CAMERA_IN)) {
			zoomIn();
		}
		if (input.isKeyDown(Controller.CAMERA_OUT)) {
			zoomOut();
		}
	}

	public void move(Dir d, float amount) {
		location = location.move(d, amount);
	}

	public void move(Dir d, float amount, float maxSpeed) {
		move(d, Math.min(amount, maxSpeed));
	}

	//
	// Getters
	//
	public Point getLocation() {
		return location;
	}

	public float getSpeed() {
		return speed;
	}

	public float getScale() {
		return scale;
	}

	//
	// Setters
	//
	/**
	 * Sets the target for the camera to follow.
	 */
	public void setTarget(WorldObject t) {
		target = t;
	}

	/**
	 * Sets the camera to follow its current target.
	 */
	public void startFollowing() {
		following = true;
	}

	/**
	 * Sets the camera to stop following its current target.
	 */
	public void stopFollowing() {
		following = false;
	}

	//
	// Zoom
	//
	public void zoomIn() {
		scale = Math.min(scale + SCALE_RATE, MAX_SCALE);
	}

	public void zoomOut() {
		scale = Math.max(scale - SCALE_RATE, MIN_SCALE);
	}
}
