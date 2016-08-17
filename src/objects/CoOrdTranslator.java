package objects;

import gameInterface.InterfaceElement;
import helpers.Point;
import helpers.Rectangle;
import options.GlobalOptions;
import worlds.Camera;
import worlds.WorldLayer;

public class CoOrdTranslator {	
	GameObject go;
	public CoOrdTranslator(GameObject go) {
		this.go = go;
	}

	public Point objectToWorldCoOrds(Point point) {
		return point.move(go.getPosition());
	}

	public Rectangle objectToWorldCoOrds(Rectangle rect) {
		return rect.translate(go.getPosition());
	}

	public Point objectToScreenCoOrds(Point point) {
		if (go instanceof InterfaceElement) {
			return objectToWorldCoOrds(point);
		} else if (go instanceof WorldObject) {
			Camera camera = go.getWorld().getCamera();
			WorldLayer layer = ((WorldObject) go).getLayer();
			return objectToWorldCoOrds(point).move(
					camera.getLocation().scale(layer.getParallaxX(),
							layer.getParallaxY()).neg()).scale(
									camera.getScale()
											* GlobalOptions.GRID_SIZE);
		} else {
			return null;
		}
	}

	public Rectangle objectToScreenCoOrds(Rectangle rect) {
		if (go instanceof InterfaceElement) {
			return objectToWorldCoOrds(rect);
		} else if (go instanceof WorldObject) {
			Camera camera = go.getWorld().getCamera();
			Point origin = objectToScreenCoOrds(rect.getTopLeft());
			float width = rect.getWidth() * camera.getScale()
					* GlobalOptions.GRID_SIZE;
			float height = rect.getHeight() * camera.getScale()
					* GlobalOptions.GRID_SIZE;
			return new Rectangle(origin, width, height);
		} else {
			return null;
		}
	}
	
	public float getDrawScale() {
		if (go instanceof InterfaceElement) {
			return 1.0f;
		} else if (go instanceof WorldObject) {
			return go.getWorld().getCamera().getScale();
		} else {
			return 1.0f;
		}
	}
}
