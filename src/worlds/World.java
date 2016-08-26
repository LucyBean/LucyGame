package worlds;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import gameInterface.DefaultGameInterface;
import gameInterface.GameInterface;
import gameInterface.InterfaceElement;
import helpers.Dir;
import helpers.Point;
import objectLibrary.Wall;
import objects.Actor;
import objects.ObjectLayerSet;
import objects.WorldObject;
import options.GlobalOptions;

public class World {
	private Camera camera;
	private GameInterface gameInterface;
	private GameInterface worldInterface;
	private Collection<Actor> actors;
	private Set<Actor> activeActors;
	private Collection<WorldObject> solids;
	private Set<WorldObject> activeSolids;
	private Collection<WorldObject> interactables;
	private WorldState worldState;
	private final LucyGame game;
	private final String name;
	private ObjectLayerSet<WorldObject> layers;
	
	private static final GameInterface defaultInterface = new DefaultGameInterface();

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
			layers = new ObjectLayerSet<WorldObject>();
			actors = new HashSet<Actor>();
			activeActors = new HashSet<Actor>();
			solids = new HashSet<WorldObject>();
			activeSolids = new HashSet<WorldObject>();
			interactables = new HashSet<WorldObject>();
			worldState = WorldState.PLAYING;
			
			setGameInterface(defaultInterface);
			worldInterface = new GameInterface();

			init();

		} catch (SlickException se) {
			System.err.println("Unable to initialise or reset world.");
			se.printStackTrace();
		}
	}

	private void setGameInterface(GameInterface gi) {
		gameInterface = gi;
		gi.setWorld(this);
	}

	protected void enableStatusWindow() {
		if (worldInterface != null) {
			worldInterface.enableStatusWindow();
		}
	}

	/**
	 * Sets the in-game StatusWindow to display the status of the given
	 * WorldObject.
	 * 
	 * @param a
	 *            The WorldObject whose status should be displayed.
	 */
	public void setWatchTarget(WorldObject a) {
		if (GlobalOptions.debug()) {
			System.out.println("Now watching: " + a);
		}
		worldInterface.setWatchTarget(a);
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
		layers.add(go, layer.ordinal());

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
			addToActiveSets(go);
		}

		go.setWorld(this);
	}

	public void addObject(InterfaceElement ie, WorldState state) {
		worldInterface.add(ie, state);
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
	public Collection<WorldObject> getActiveSolids() {
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
	public Collection<WorldObject> getAllInteractables() {
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
	public void addToActiveSets(WorldObject go) {
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
	// TODO: Change to a single "set state" method?
	public void openMenu() {
		worldState = WorldState.MENU;
	}

	public void closeMenu() {
		worldState = WorldState.PLAYING;
		gameInterface.resetMenus();
	}

	public void startWatchSelect() {
		worldState = WorldState.WATCH_SELECT;
	}

	public void stopWatchSelect() {
		closeMenu();
	}
	
	public void openInventoryDisplay() {
		worldState = WorldState.INVENTORY;
	}
	
	public void closeInventoryDisplay() {
		worldState = WorldState.PLAYING;
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
		layers.render();

		worldInterface.render(getState());
		gameInterface.render(getState());
	}

	public void update(GameContainer gc, int delta) {
		Input input = gc.getInput();

		camera.update(gc, delta);
		gameInterface.update(gc, delta, getState());
		worldInterface.update(gc, delta, getState());

		// Reset on D
		if (input.isKeyDown(Input.KEY_D)) {
			reset();
		}

		switch (worldState) {
			case PLAYING:
				playingUpdate(gc, delta);
				break;
			default:
				break;
		}
	}

	private void playingUpdate(final GameContainer gc, final int delta) {
		// Pass update signal to all objects in the game.
		layers.update(gc, delta);
	}

	public void keyPressed(int keycode) {
		gameInterface.keyPressed(keycode, worldState);
		
		switch (worldState) {
			case PLAYING:
				if (keycode == Input.KEY_ESCAPE) {
					openMenu();
				}
				if (keycode == Input.KEY_I) {
					openInventoryDisplay();
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
			case INVENTORY:
				if (keycode == Input.KEY_I) {
					closeInventoryDisplay();
				}
			default:
				break;
		}
	}

	public void mousePressed(int button, int x, int y) {
		Point clickPoint = new Point(x, y);

		if (worldState == WorldState.WATCH_SELECT) {
			watchSelectMousePressed(button, clickPoint);
		}

		worldInterface.mousePressed(button, clickPoint, getState());
		gameInterface.mousePressed(button, clickPoint, getState());

	}

	// TODO: Make this iterate in reverse order.
	private void watchSelectMousePressed(int button, Point p) {
		// For each world object, check whether it was clicked by the mouse
		WorldObject clicked = layers.findClickedObject(p);

		if (clicked != null) {
			setWatchTarget(clicked);
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
