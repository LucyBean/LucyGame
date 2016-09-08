package worlds;

import java.util.function.Function;

import helpers.Point;
import objectLibrary.Wall;
import objects.WorldObject;

public class MapPainter {
	private Map map;
	private Function<Point, WorldObject> brush;
	
	public MapPainter (Map m) {
		map = m;
		brush = p -> new Wall(p,1,1);
	}
	
	public void paint(Point p) {
		WorldObject wo = brush.apply(p);
		map.addObject(wo);
	}
	
	public void setBrush(Function<Point, WorldObject> brush) {
		this.brush = brush;
	}
}
