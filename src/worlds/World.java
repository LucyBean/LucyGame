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
import helpers.Rectangle;
import objectLibrary.Button;
import objectLibrary.Menu;
import objectLibrary.MenuButton;
import objectLibrary.Wall;
import objects.Actor;
import objects.InterfaceElement;
import objects.WorldObject;

public class World {
	private Camera camera;
	private List<ObjectLayer<WorldObject>> layers;
	private GameInterface gameInterface;
	private List<Actor> actors;
	private List<Actor> activeActors;
	private List<WorldObject> solids;
	private List<WorldObject> activeSolids;
	private List<WorldObject> interactables;
	private WorldState worldState;
	private final LucyGame game;
	private final String name;

	public World(LucyGame game, String name) {
		this.game = game;
		this.name = name;
		reset();
	}

	/**
	 * Sets the world to its initial state.
	 */
	private void reset() {
		try {
			camera = new Camera();
			layers = new ArrayList<ObjectLayer<WorldObject>>();
			gameInterface = new GameInterface(this);
			actors = new ArrayList<Actor>();
			activeActors = new ArrayList<Actor>();
			solids = new ArrayList<WorldObject>();
			activeSolids = new ArrayList<WorldObject>();
			interactables = new ArrayList<WorldObject>();
			worldState = WorldState.PLAYING;

			for (@SuppressWarnings("unused")
			WorldLayer l : WorldLayer.values()) {
				layers.add(new ObjectLayer<WorldObject>());
			}

			init();
			buildGameInterface();

		} catch (SlickException se) {
			System.err.println("Unable to initialise or reset world.");
			se.printStackTrace();
		}
	}

	/**
	 * Builds the default interface for a World. Override this to implement a
	 * different interface for that World.
	 */
	protected void buildGameInterface() {
		Button openMenuButton = new Button(
				new Rectangle(new Point(200, 0), 100, 32)) {
			@Override
			public void onClick(int button) {
				getWorld().openMenu();
			}
		};
		openMenuButton.setText("Open menu");
		addObject(openMenuButton, WorldState.PLAYING);

		Button closeMenuButton = new Button(
				new Rectangle(new Point(300, 0), 100, 32)) {
			@Override
			public void onClick(int button) {
				getWorld().closeMenu();
			}
		};
		closeMenuButton.setText("Close menu");
		addObject(closeMenuButton, WorldState.MENU);

		Button clickToStopSelect = new Button(
				new Rectangle(new Point(150, 0), 340, 32)) {
			@Override
			public void onClick(int button) {
				getWorld().stopWatchSelect();
			}
		};
		clickToStopSelect.setText("Click here to stop selecting");
		addObject(clickToStopSelect, WorldState.WATCH_SELECT);

		Menu m = new Menu();
		m.add(new MenuButton("Hello!"));
		m.add(new MenuButton("World!"));
		MenuButton backToMainMenu = new MenuButton("Main menu") {
			@Override
			public void onClick(int button) {
				getWorld().getGame().loadMainMenu();
			}
		};
		m.add(backToMainMenu);
		MenuButton selectWatchedObject = new MenuButton("Select watch object") {
			@Override
			public void onClick(int button) {
				getWorld().startWatchSelect();
			}
		};
		m.add(selectWatchedObject);

		addObject(m, WorldState.MENU);
	}

	/**
	 * Called when the World is constructed. Override this when creating a new
	 * World to add initial objects.
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
	public void addObject(WorldObject go) {
		WorldLayer layer = go.getLayer();
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

	public void addObject(InterfaceElement ie, WorldState state) {
		gameInterface.add(ie, state);
		ie.setWorld(this);
	}

	//
	// Getters
	//
	/**
	 * Returns all active solid objects in the world.
	 * 
	 * @return
	 */
	public List<WorldObject> getActiveSolids() {
		// Currently returns all solid objects in the world.
		// Modify to keep track of on screen objects.
		return activeSolids;
	}

	public WorldState getState() {
		return worldState;
	}

	/**
	 * Returns all interactable objects in the world.
	 * 
	 * @return
	 */
	public List<WorldObject> getAllInteractables() {
		// TODO
		// Currently returns all interactables in the world.
		// Modify to keep track of on screen objects.
		return interactables;
	}

	public Camera getCamera() {
		return camera;
	}

	public LucyGame getGame() {
		return game;
	}

	public String getName() {
		return name;
	}

	//
	// Setters
	//
	public void setCameraTarget(WorldObject go) {
		camera.setTarget(go);
	}

	/**
	 * Removes this GameObject from all active lists. The GameObject will no
	 * longer be updated or rendered.
	 * 
	 * @param go
	 */
	public void removeFromActiveLists(WorldObject go) {
		if (go instanceof Actor) {
			activeActors.remove((Actor) go);
		}
		if (go.isSolid()) {
			activeSolids.remove(go);
		}
	}

	/**
	 * Adds this GameObject to all applicable active lists. The GameObject will
	 * now be updated and rendered.
	 * 
	 * @param go
	 */
	public void addToActiveLists(WorldObject go) {
		if (go instanceof Actor) {
			activeActors.add((Actor) go);
		}
		if (go.isSolid()) {
			activeSolids.add(go);
		}
	}

	//
	// State transitions
	//
	// TODO: Make these check for validity.
	public void openMenu() {
		worldState = WorldState.MENU;
	}

	public void closeMenu() {
		worldState = WorldState.PLAYING;
	}

	public void startWatchSelect() {
		worldState = WorldState.WATCH_SELECT;
	}

	public void stopWatchSelect() {
		worldState = WorldState.MENU;
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
			ObjectLayer<WorldObject> ol = layers.get(l.ordinal());
			if (ol.isVisible()) {
				ol.render(getCamera());
			}
		}

		gameInterface.render(getState());
	}

	public void update(GameContainer gc, int delta) {
		Input input = gc.getInput();

		camera.update(gc, delta);
		gameInterface.update(gc, delta, getState());

		// Reset on D
		if (input.isKeyDown(Input.KEY_D)) {
			reset();
		}

		switch (worldState) {
			case PLAYING:
				playingUpdate(gc, delta);
				break;
			case MENU:
				break;
			case WATCH_SELECT:
				break;
		}
	}

	private void playingUpdate(GameContainer gc, int delta) {
		// Update all GameObjects
		for (WorldLayer l : WorldLayer.values()) {
			ObjectLayer<WorldObject> ol = layers.get(l.ordinal());
			if (ol.isVisible()) {
				ol.update(gc, delta);
			}
		}
	}

	public void keyPressed(int keycode) {
		switch (worldState) {
			case PLAYING:
				if (keycode == Input.KEY_ESCAPE) {
					openMenu();
				}
				break;
			case MENU:
				if (keycode == Input.KEY_ESCAPE) {
					closeMenu();
				}
				break;
			case WATCH_SELECT:
				if (keycode == Input.KEY_ESCAPE) {
					stopWatchSelect();
				}
				break;
		}
	}

	public void mousePressed(int button, int x, int y) {
		Point clickPoint = new Point(x,y);
		
		if (worldState == WorldState.WATCH_SELECT) {
			watchSelectMousePressed(button, clickPoint);
		}
		
		gameInterface.mousePressed(button, clickPoint, getState());
		
	}
	
	// TODO: Make this iterate in reverse order.
	private void watchSelectMousePressed(int button, Point p) {
		// For each world object, check whether it was clicked by the mouse
		Iterator<ObjectLayer<WorldObject>> iol = layers.iterator();
		WorldObject clicked = null;
		
		while(iol.hasNext()) {
			ObjectLayer<WorldObject> olwo = iol.next();
			WorldObject wo = olwo.findClickedObject(p, getCamera());
			if (wo != null) {
				clicked = wo;
			}
		}
		
		if (clicked != null) {
			System.out.println(clicked + " was clicked!");
			stopWatchSelect();
		}
	}

	//
	// Some helpful world creator tools
	//

	/**
	 * Draws a wall border around the world.
	 * 
	 * @param width
	 *            Number of wall blocks along x.
	 * @param height
	 *            Number of wall blocks along y.
	 */
	protected void drawWallBorder(int width, int height) {
		addObject(Wall.drawWall(Point.ZERO, Dir.EAST, width));
		addObject(Wall.drawWall(new Point(0, height - 1), Dir.EAST, width));
		addObject(Wall.drawWall(new Point(0, 1), Dir.SOUTH, height - 2));
		addObject(
				Wall.drawWall(new Point(width - 1, 1), Dir.SOUTH, height - 2));
	}

	protected void drawWallBorder() {
		drawWallBorder(GlobalOptions.WINDOW_WIDTH_GRID,
				GlobalOptions.WINDOW_HEIGHT_GRID);
	}

}
