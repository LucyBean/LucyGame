package objects;

import helpers.Point;
import helpers.Rectangle;
import objects.gameInterface.InterfaceElement;
import objects.world.WorldObject;
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
	
	public Point worldToObjectCoOrds(Point point) {
		return point.move(go.getPosition().neg());
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
			throw new IllegalStateException("GameObject " + go + " is of incorrect type");
		}
	}
	
	public Point screenToWorldCoOrds(Point point) {
		if (go instanceof InterfaceElement) {
			return point;
		} else if (go instanceof WorldObject) {
			Camera camera = go.getWorld().getCamera();
			WorldLayer layer = ((WorldObject) go).getLayer();
			int gridSize = GlobalOptions.GRID_SIZE;
			
			Point worldCoOrds = point.scale(
					1 / (camera.getScale() * gridSize)).move(
							camera.getLocation().scale(
									layer.getParallaxX(),
									layer.getParallaxY()));
			
			return worldCoOrds;
		} else {
			throw new IllegalStateException("GameObject " + go + " is of incorrect type");
		}
	}
	
	public Point screenToObjectCoOrds(Point point) {
		Point worldCoOrds = screenToWorldCoOrds(point);
		return worldToObjectCoOrds(worldCoOrds);
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
			throw new IllegalStateException("GameObject " + go + " is of incorrect type");
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
