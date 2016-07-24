package worlds;

public enum WorldLayer {
	BACKGROUND(0.25f, 0.25f), WORLD, PLAYER, INTERFACE;

	float parX;
	float parY;

	/**
	 * Defines a layer to have a certain amount of parallax. Parallax is the
	 * fraction moved by the object when the camera is moved. (e.g. if the
	 * camera moves 100 px then a layer with parallax 0.25f will move by 25 px.)
	 * 
	 * @param parallax_x
	 * @param parallax_y
	 */
	WorldLayer(float parallax_x, float parallax_y) {
		parX = parallax_x;
		parY = parallax_y;
	}

	WorldLayer() {
		parX = 1.0f;
		parY = 1.0f;
	}

	public float getParallaxX() {
		return parX;
	}

	public float getParallaxY() {
		return parY;
	}
}
