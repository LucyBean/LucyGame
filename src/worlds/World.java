package worlds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;
import objectLibrary.Wall;
import objects.Actor;
import objects.GameObject;
import objects.InvalidObjectStateException;

public class World {
	Camera camera;
	List<ObjectLayer> layers;
	List<Actor> actors;
	List<Actor> activeActors;
	List<GameObject> solids;
	List<GameObject> activeSolids;
	List<GameObject> interactables;

	public World() {
		reset();
	}

	/**
	 * Sets the world to its initial state.
	 */
	private void reset() {
		try {
			camera = new Camera();
			layers = new ArrayList<ObjectLayer>();
			actors = new ArrayList<Actor>();
			activeActors = new ArrayList<Actor>();
			solids = new ArrayList<GameObject>();
			activeSolids = new ArrayList<GameObject>();
			interactables = new ArrayList<GameObject>();

			for (@SuppressWarnings("unused")
			WorldLayer l : WorldLayer.values()) {
				layers.add(new ObjectLayer());
			}

			init();
		} catch (SlickException se) {
			System.err.println("Unable to initialise or reset world.");
			se.printStackTrace();
		}
	}

	/**
	 * Called when the World is constructed. Override this when creating a new World to add initial
	 * objects.
	 * 
	 * @throws SlickException
	 */
	public void init() throws SlickException {

	}

	/**
	 * Adds the object to world on the specified layer.
	 * 
	 * @param go
	 *            The GameObject to add.
	 * @param layer
	 *            The WorldLayer for the object.
	 */
	public void addObject(GameObject go, WorldLayer layer) {
		layers.get(layer.ordinal()).add(go);

		// Adds the object to any extra lists.
		if (go instanceof Actor) {
			actors.add((Actor) go);
		}
		
		if (go.isSolid()) {
			solids.add(go);
		}
		if (go.isInteractable()) {
			interactables.add(go);
		}

		if (go.isEnabled()) {
			addToActiveLists(go);
		}

		go.setWorld(this);
	}

	/**
	 * Returns all active solid objects in the world.
	 * 
	 * @return
	 */
	public List<GameObject> getActiveSolids() {
		// Currently returns all solid objects in the world.
		// Modify to keep track of on screen objects.
		return activeSolids;
	}

	/**
	 * Returns all interactable objects in the world.
	 * 
	 * @return
	 */
	public List<GameObject> getAllInteractables() {
		// TODO
		// Currently returns all interactables in the world.
		// Modify to keep track of on screen objects.
		return interactables;
	}

	public Camera getCamera() {
		return camera;
	}

	/**
	 * Removes this GameObject from all active lists. The GameObject will no longer be updated or
	 * rendered.
	 * 
	 * @param go
	 */
	public void removeFromActiveLists(GameObject go) {
		if (go instanceof Actor) {
			activeActors.remove((Actor) go);
		}
		if (go.isSolid()) {
			activeSolids.remove(go);
		}
	}

	/**
	 * Adds this GameObject to all applicable active lists. The GameObject will now be updated and
	 * rendered.
	 * 
	 * @param go
	 */
	public void addToActiveLists(GameObject go) {
		if (go instanceof Actor) {
			activeActors.add((Actor) go);
		}
		if (go.isSolid()) {
			activeSolids.add(go);
		}
	}

	/**
	 * Renders the world.
	 * 
	 * @param gc
	 *            GameContainer object, passed from a Slick2D BasicGame render method
	 * @param g
	 *            Graphics object, passed from a Slick2D BasicGame render method
	 */
	public void render(GameContainer gc, Graphics g) {
		// Renders all objects in layer order if the layer is visible.
		for (WorldLayer l : WorldLayer.values()) {
			ObjectLayer ol = layers.get(l.ordinal());
			if (ol.isVisible()) {
				Iterator<GameObject> oli = ol.iterator();

				while (oli.hasNext()) {
					GameObject go = oli.next();
					if (go.isEnabled()) {
						try {
							go.render(gc, g, camera);
						} catch (InvalidObjectStateException iose) {
							System.err.println(iose.getMessage());
							iose.printStackTrace();
						}
					}
				}
			}
		}
	}

	public void update(GameContainer gc, int delta) {
		Input input = gc.getInput();

		// Move the camera around the world.
		if (input.isKeyDown(Input.KEY_T)) {
			camera.move(Dir.SOUTH, camera.getSpeed() * delta);
		}
		if (input.isKeyDown(Input.KEY_C)) {
			camera.move(Dir.NORTH, camera.getSpeed() * delta);
		}
		if (input.isKeyDown(Input.KEY_H)) {
			camera.move(Dir.WEST, camera.getSpeed() * delta);
		}
		if (input.isKeyDown(Input.KEY_N)) {
			camera.move(Dir.EAST, camera.getSpeed() * delta);
		}
		if (input.isKeyDown(Input.KEY_R)) {
			camera.zoomIn();
		}
		if (input.isKeyDown(Input.KEY_G)) {
			camera.zoomOut();
		}

		// Reset on D
		if (input.isKeyDown(Input.KEY_D)) {
			reset();
		}

		// Propagate updates to actors
		Iterator<Actor> ai = activeActors.iterator();
		while (ai.hasNext()) {
			try {
				Actor a = ai.next();
				a.update(gc, delta);
			} catch (InvalidObjectStateException iose) {
				System.err.println(iose.getMessage());
				iose.printStackTrace();
			}
		}
	}
	
	//
	// Some helpful world creator tools
	//
	protected void drawWallBorder() {
		for (int i = 0; i < 13; i++) {
			addObject(new Wall(new Point(0 + 50 * i, 0)), WorldLayer.WORLD);
			addObject(new Wall(new Point(0 + 50 * i, 450)), WorldLayer.WORLD);
		}
		for (int i = 0; i < 8; i++) {
			addObject(new Wall(new Point(0, 50 + 50 * i)), WorldLayer.WORLD);
			addObject(new Wall(new Point(600, 50 + 50 * i)), WorldLayer.WORLD);
		}
	}
}
