package worlds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjectLayer {
	List<GameObject> objects;
	boolean visible = true;
	
	public ObjectLayer() {
		objects = new ArrayList<GameObject>();
	}
	
	public void add(GameObject go) {
		objects.add(go);
	}
	
	public Iterator<GameObject> iterator() {
		return objects.iterator();
	}
	
	public boolean isVisible() {
		return visible;
	}
}
