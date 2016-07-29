package worlds;

import java.util.Iterator;

import helpers.Point;
import objects.GameObject;
import objects.InterfaceElement;

public class GameInterface extends ObjectLayer {
	@Override
	public void add(GameObject go) {
		if (go instanceof InterfaceElement) {
			super.add(go);
		} else {
			System.err.println(
					"Attempting to add a non-InterfaceElement object to the interface");
		}
	}

	public void mousePressed(int button, Point clickPoint) {
		Iterator<GameObject> oli = objects.iterator();

		while (oli.hasNext()) {
			InterfaceElement go = (InterfaceElement) oli.next();
			go.mousePressed(button, clickPoint);
		}
	}
}
