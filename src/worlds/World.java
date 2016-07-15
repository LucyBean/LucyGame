package worlds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;

public class World {
	Camera camera;
	List<ObjectLayer> layers;
	List<Actor> actors;

	public World() throws SlickException {
		camera = new Camera();
		layers = new ArrayList<ObjectLayer>();
		actors = new ArrayList<Actor>();

		for (@SuppressWarnings("unused")
		WorldLayer l : WorldLayer.values()) {
			layers.add(new ObjectLayer());
		}

		createDemoWorld();
	}

	public void createDemoWorld() throws SlickException {
		GameObject background = new Static(Point.ZERO, new Sprite(new Image("data/Desert.jpg")));
		addObject(background, WorldLayer.BACKGROUND);

		GameObject player = new Actor(new Point(40, 50), new Sprite(ImageLibrary.PLAYER_IMAGE), new Collider(Point.ZERO, 40, 80), null) {
			public void update(GameContainer gc, int delta) {
				Input input = gc.getInput();
				
				if (input.isKeyDown(Input.KEY_COMMA)) {
					move(Dir.NORTH, 1);
				}
				if (input.isKeyDown(Input.KEY_O)) {
					move(Dir.SOUTH, 1);
				}
				if (input.isKeyDown(Input.KEY_A)) {
					move(Dir.WEST, 1);
				}
				if (input.isKeyDown(Input.KEY_E)) {
					move(Dir.EAST, 1);
				}
			}
		};
		addObject(player, WorldLayer.PLAYER);
		
		for (int i = 0; i < 10; i++) {
			addObject(GameObjectLibrary.WALL(new Point(200 + 50*i, 200)), WorldLayer.WORLD);
		}
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
		
		if (go instanceof Actor) {
			actors.add((Actor) go);
		}
	}

	/**
	 * Renders the world.
	 * 
	 * @param gc
	 *            GameContainer object, passed from a Slick2D BasicGame render
	 *            method
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
					go.render(gc, g, camera);
				}
			}
		}
	}

	public void update(GameContainer gc, int delta) {
		Input input = gc.getInput();

		// Move the camera around the world.
		if (input.isKeyDown(Input.KEY_T)) {
			camera.move(Dir.SOUTH, camera.getSpeed()*delta);
		}
		if (input.isKeyDown(Input.KEY_C)) {
			camera.move(Dir.NORTH, camera.getSpeed()*delta);
		}
		if (input.isKeyDown(Input.KEY_H)) {
			camera.move(Dir.WEST, camera.getSpeed()*delta);
		}
		if (input.isKeyDown(Input.KEY_N)) {
			camera.move(Dir.EAST, camera.getSpeed()*delta);
		}
		if (input.isKeyDown(Input.KEY_R)) {
			camera.zoomIn();
		}
		if (input.isKeyDown(Input.KEY_G)) {
			camera.zoomOut();
		}

		// Propagate updates to actors
		Iterator<Actor> ai = actors.iterator();
		while (ai.hasNext()) {
			Actor a = ai.next();
			a.update(gc, delta);
		}
	}
}
