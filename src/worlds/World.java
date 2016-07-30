package worlds;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import objectLibrary.Button;
import objectLibrary.Wall;
import objects.Actor;
import objects.InterfaceElement;
import objects.WorldObject;

public class World implements MouseListener {
	private Camera camera;
	private List<ObjectLayer> layers;
	private GameInterface gameInterface;
	private List<Actor> actors;
	private List<Actor> activeActors;
	private List<WorldObject> solids;
	private List<WorldObject> activeSolids;
	private List<WorldObject> interactables;
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
			layers = new ArrayList<ObjectLayer>();
			gameInterface = new GameInterface();
			actors = new ArrayList<Actor>();
			activeActors = new ArrayList<Actor>();
			solids = new ArrayList<WorldObject>();
			activeSolids = new ArrayList<WorldObject>();
			interactables = new ArrayList<WorldObject>();

			for (@SuppressWarnings("unused")
			WorldLayer l : WorldLayer.values()) {
				layers.add(new ObjectLayer());
			}

			init();
			
			Button menuButton = new Button(new Rectangle(new Point(200,0),100,32)){
				@Override
				public void onClick(int button) {
					getWorld().getGame().loadMainMenu();
				}
			};
			menuButton.setText("Main menu");
			addObject(menuButton);
		} catch (SlickException se) {
			System.err.println("Unable to initialise or reset world.");
			se.printStackTrace();
		}
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

	public void addObject(InterfaceElement ie) {
		gameInterface.add(ie);
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
				ol.render(gc, g, getCamera());
			}
		}

		gameInterface.render(gc, g, camera);
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

		camera.update(gc, delta);
		
		// Update all GameObjects
		for (WorldLayer l : WorldLayer.values()) {
			ObjectLayer ol = layers.get(l.ordinal());
			if (ol.isVisible()) {
				ol.update(gc, delta);
			}
		}

		gameInterface.update(gc, delta);
	}

	//
	// Some helpful world creator tools
	//
	protected void drawWall(Point start, Dir d, int length) {
		final int x_step = (int) d.asPoint().getX();
		final int y_step = (int) d.asPoint().getY();
		for (int i = 0; i < length; i++) {
			addObject(new Wall(new Point(start.getX() + i * x_step,
					start.getY() + i * y_step)));
		}
	}

	/**
	 * Draws a wall border around the world.
	 * 
	 * @param width
	 *            Number of wall blocks along x.
	 * @param height
	 *            Number of wall blocks along y.
	 */
	protected void drawWallBorder(int width, int height) {
		drawWall(Point.ZERO, Dir.EAST, width);
		drawWall(new Point(0, height - 1), Dir.EAST, width);
		drawWall(new Point(0, 1), Dir.SOUTH, height - 2);
		drawWall(new Point(width - 1, 1), Dir.SOUTH, height - 2);
	}

	protected void drawWallBorder() {
		drawWallBorder(GlobalOptions.WINDOW_WIDTH_GRID,
				GlobalOptions.WINDOW_HEIGHT_GRID);
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		gameInterface.mousePressed(button, new Point(x, y));
	}

	//
	// MouseListener methods
	//
	@Override
	public void inputEnded() {
	}

	@Override
	public void inputStarted() {
	}

	@Override
	public boolean isAcceptingInput() {
		return false;
	}

	@Override
	public void setInput(Input arg0) {
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
	}

	@Override
	public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void mouseReleased(int arg0, int arg1, int arg2) {

	}

	@Override
	public void mouseWheelMoved(int arg0) {
	}
}
