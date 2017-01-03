package objects.world;

import helpers.Dir;
import helpers.Point;

public interface Pushable {
	public Point getPosition();
	public Point bePushed(Dir d, float amount);
}
