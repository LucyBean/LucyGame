package worlds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import characters.Conversation;
import gameInterface.DefaultGameInterface;
import gameInterface.GameInterface;
import gameInterface.InterfaceElement;
import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import objectLibrary.Wall;
import objects.WorldObject;
import options.GlobalOptions;
import player.Inventory;
import player.Player;
import quests.EventInfo;
import quests.Quest;

public class World {
	private Camera camera;
	private GameInterface gameInterface;
	private GameInterface worldInterface;
	private WorldState worldState;
	private Collection<Quest> activeQuests;
	private final LucyGame game;
	private final String name;
	private WorldMap map;
	private boolean ignoringInput;

	private static final GameInterface defaultInterface = new DefaultGameInterface();

	public World(LucyGame game, String name) {
		this.game = game;
		this.name = name;
		map = new WorldMap(this);
		reset();
	}

	/**
	 * Sets the world to its initial state.
	 */
	private void reset() {
		try {
			camera = new Camera();
			worldState = WorldState.PLAYING;
			activeQuests = new HashSet<>();
			map.reset();

			gameInterface = defaultInterface;
			gameInterface.setWorld(this);
			worldInterface = new GameInterface();
			worldInterface.setWorld(this);
			ignoringInput = false;

			init();

		} catch (SlickException se) {
			System.err.println("Unable to initialise or reset world.");
			se.printStackTrace();
		}
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
		map.addObject(go);

		// If it is a Player, register their inventory with the inventory
		// displayer
		if (go instanceof Player) {
			Inventory i = ((Player) go).getInventory();
			gameInterface.setInventoryToDisplay(i);
		}
	}

	public void removeObject(WorldObject go) {
		map.removeObject(go);
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
	private Collection<WorldObject> getActiveSolids() {
		return map.getActiveSolids();
	}

	/**
	 * Finds the Colliders that are solid that overlap with the given rectangle.
	 * 
	 * @param rect
	 * @return
	 */
	public Collection<WorldObject> getOverlappingSolids(Rectangle rect) {
		Collection<WorldObject> solids = getActiveSolids();
		Collection<WorldObject> collidingSolids = new ArrayList<WorldObject>();
		Iterator<WorldObject> si = solids.iterator();
		while (si.hasNext()) {
			WorldObject go = si.next();
			if (go.getCollider().isSolid()) {
				// Get collider rectangle in relative co-ordinates
				Rectangle rectRel = go.getCollider().getRectangle();
				// Translate to world co-ords.
				Rectangle rectWorld = go.getCoOrdTranslator().objectToWorldCoOrds(
						rectRel);
				// If the collider overlaps with wholeArea, add to activeSolids
				// list.
				if (rectWorld.overlaps(rect)) {
					collidingSolids.add(go);
				}
			}
		}

		return collidingSolids;
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
		return map.getAllInteractables();
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

	public WorldMap getMap() {
		return map;
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
		map.removeFromActiveSets(go);
	}

	/**
	 * Adds this GameObject to all applicable active lists. The GameObject will
	 * now be updated and rendered.
	 * 
	 * @param go
	 */
	public void addToActiveSets(WorldObject go) {
		map.addToActiveSets(go);
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
		gameInterface.refreshInventoryDisplay();
		worldState = WorldState.INVENTORY;
	}

	public void closeInventoryDisplay() {
		worldState = WorldState.PLAYING;
	}

	public void conversationStarted() {
		worldState = WorldState.CONVERSATION;
	}

	public void conversationFinished() {
		worldState = WorldState.PLAYING;
	}

	public void startBuilding() {
		worldState = WorldState.BUILDING;
		ignoreInput(true);
	}

	public void stopBuilding() {
		worldState = WorldState.PLAYING;
	}

	public void openBuildMenu() {
		worldState = WorldState.BUILDING_MENU;
	}

	public void closeBuildMenu() {
		startBuilding();
	}

	protected void ignoreInput(boolean ignore) {
		ignoringInput = ignore;
	}

	protected boolean isIgnoringInput() {
		return ignoringInput;
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
		map.render();

		worldInterface.render(getState());
		gameInterface.render(getState());
	}

	public void update(GameContainer gc, int delta) {
		Input input = gc.getInput();

		// Reset on D
		if (input.isKeyDown(Input.KEY_D)) {
			reset();
		}

		if (!ignoringInput) {

			camera.update(gc, delta);
			gameInterface.update(gc, delta, getState());
			worldInterface.update(gc, delta, getState());

			switch (worldState) {
				case PLAYING:
					playingUpdate(gc, delta);
					break;
				case BUILDING:
					buildingUpdate(gc, delta);
					break;
				default:
					break;
			}
		}
	}

	private void playingUpdate(GameContainer gc, int delta) {
		// Pass update signal to all objects in the game.
		map.update(gc, delta);
	}

	private void buildingUpdate(GameContainer gc, int delta) {
		Input input = gc.getInput();
		Point mousePoint = new Point(input.getMouseX(), input.getMouseY());
		WorldObject wo = map.findClickedObject(mousePoint);
		boolean mouseOnMap = !gameInterface.mouseOver(mousePoint, getState())
				& !worldInterface.mouseOver(mousePoint, getState());

		if (mouseOnMap) {
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				if (wo == null) {
					int gridSize = GlobalOptions.GRID_SIZE;
					Point worldCoOrds = mousePoint.scale(
							1 / (camera.getScale() * gridSize)).move(
									camera.getLocation().scale(
											WorldLayer.WORLD.getParallaxX(),
											WorldLayer.WORLD.getParallaxY()));

					// Need to ensure that the objects are snapped to the grid!
					float snapX = (float) Math.floor(worldCoOrds.getX());
					float snapY = (float) Math.floor(worldCoOrds.getY());

					worldCoOrds = new Point(snapX, snapY);

					map.getPainter().paint(worldCoOrds);
				}
			} else if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
				if (wo != null) {
					removeObject(wo);
				}
			}
		}
	}

	public void keyPressed(int keycode) {
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
			case BUILDING_MENU:
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
				break;
			default:
				break;
		}

		gameInterface.keyPressed(keycode, worldState);
		worldInterface.keyPressed(keycode, worldState);
	}

	public void mousePressed(int button, int x, int y) {
		Point clickPoint = new Point(x, y);

		if (worldState == WorldState.WATCH_SELECT) {
			watchSelectMousePressed(button, clickPoint);
		}

		worldInterface.mousePressed(button, clickPoint, getState());
		gameInterface.mousePressed(button, clickPoint, getState());

	}

	private void watchSelectMousePressed(int button, Point p) {
		// For each world object, check whether it was clicked by the mouse
		WorldObject clicked = map.findClickedObject(p);

		if (clicked != null) {
			setWatchTarget(clicked);
			stopWatchSelect();
		}
	}

	public void showConversation(Conversation c) {
		gameInterface.showConversation(c);
		conversationStarted();
	}

	//
	// Quests
	//
	public void signalEvent(EventInfo ei) {
		activeQuests.stream().forEach(q -> q.signalEvent(ei));
	}

	public void startQuest(Quest q) {
		activeQuests.add(q);
		q.start();
	}

	public void stopQuest(Quest q) {
		activeQuests.remove(q);
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
