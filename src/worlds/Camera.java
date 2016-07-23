package worlds;

import helpers.*;

/**
 * Camera class for storing information about the camera.
 * 
 * @author Lucy
 *
 */
public class Camera {
	Point location = new Point(0, 0);
	float scale = 1.0f;
	float speed = 0.02f;
	
	public final float MAX_SCALE = 3.0f;
	public final float MIN_SCALE = 0.3f;
	public final float SCALE_RATE = 0.01f;
	
	public Point getLocation() {
		return location;
	}
	
	public void move(Dir d, float amount) {
		location = location.move(d,amount);
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void zoomIn() {
		scale = Math.min(scale + SCALE_RATE, MAX_SCALE);
	}
	
	public void zoomOut() {
		scale = Math.max(scale - SCALE_RATE, MIN_SCALE);
	}
}
