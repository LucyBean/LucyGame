package worlds;

import java.util.Collection;
import java.util.HashSet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;
import io.ConversationLoader;
import io.ErrorLogger;
import objects.CoOrdTranslator;
import objects.GameObject;
import objects.gameInterface.DefaultGameInterface;
import objects.gameInterface.GameInterface;
import objects.gameInterface.InterfaceElement;
import objects.images.LayeredImage;
import objects.world.ItemType;
import objects.world.WorldObject;
import objects.world.characters.Conversation;
import objects.world.characters.Inventory;
import objects.world.lib.Wall;
import options.GlobalOptions;
import quests.EventInfo;
import quests.Quest;

public class World {
	private Camera camera;
	private GameInterface gameInterface;
	private GameInterface worldInterface;
	private WorldState worldState;
	private WorldState prevState;
	private Collection<Quest> activeQuests = new HashSet<>();
	private Collection<Quest> newlyFinishedQuests;
	private Collection<GameObject> waitingForInput;
	private final LucyGame game;
	private final String name;
	private WorldMap map;
	private boolean ignoringInput;
	private boolean paused;
	private boolean stepFrame;
	private CoOrdTranslator worldCOT;

	private static final GameInterface defaultInterface = new DefaultGameInterface();

	public World(LucyGame game, String name) {
		this.game = game;
		this.name = name;
		map = new WorldMap(this);
		reset();

		// This object is being added just to get its co-ord translator
		// Hack hack hack
		WorldObject wo = new WorldObject(Point.ZERO, WorldLayer.WORLD,
				ItemType.NONE) {
			@Override
			protected void resetState() {

			}
		};
		wo.setWorld(this);
		worldCOT = wo.getCoOrdTranslator();
	}

	/**
	 * Sets the world to its initial state.
	 */
	private void reset() {
		try {
			camera = new Camera();
			setWorldState(WorldState.PLAYING);
			prevState = getWorldState();
			map.reset();

			gameInterface = defaultInterface;
			gameInterface.setWorld(this);
			worldInterface = new GameInterface();
			worldInterface.setWorld(this);
			ignoringInput = false;
			paused = false;

			init();

		} catch (SlickException se) {
			ErrorLogger.log(se, "Unable to initialise or reset world.", 5);
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
	 * Loads conversations and quests from the file and registers them with
	 * NPCs. This should be done after all NPCs have been added to the map.
	 * 
	 * @param name
	 *            The name of the file that holds the conversations and quests.
	 *            It should not include the path or extension. E.g. to load the
	 *            file "data/conversations/level4.lucy" then name = "level4"
	 * 
	 */
	public void loadScripts(String name) {
		// Load the conversations and set them to the correct NPCs
		ConversationLoader.load(name, map);
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
	}

	public void setInventoryToDisplay(Inventory i) {
		gameInterface.setInventoryToDisplay(i);
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
	public WorldState getState() {
		return getWorldState();
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

	/**
	 * @return the worldState
	 */
	private WorldState getWorldState() {
		return worldState;
	}

	/**
	 * @param worldState
	 *            the worldState to set
	 */
	private void setWorldState(WorldState worldState) {
		prevState = this.worldState;
		this.worldState = worldState;
	}

	//
	// State transitions
	//
	// TODO: Make these check for validity.
	// TODO: Change to a single "set state" method?
	public void openMenu() {
		setWorldState(WorldState.MENU);
	}

	public void closeMenu() {
		setWorldState(WorldState.PLAYING);
		gameInterface.resetMenus();
	}

	public void startWatchSelect() {
		setWorldState(WorldState.WATCH_SELECT);
	}

	public void stopWatchSelect() {
		closeMenu();
	}

	public void openInventoryDisplay() {
		gameInterface.refreshInventoryDisplay();
		setWorldState(WorldState.INVENTORY);
	}

	public void closeInventoryDisplay() {
		setWorldState(WorldState.PLAYING);
	}

	public void conversationStarted() {
		setWorldState(WorldState.CONVERSATION);
	}

	public void conversationFinished() {
		setWorldState(WorldState.PLAYING);
	}

	public void startBuilding() {
		setWorldState(WorldState.BUILDING);
		ignoreInput(true);
	}

	public void stopBuilding() {
		setWorldState(WorldState.PLAYING);
	}

	public void openBuildMenu() {
		setWorldState(WorldState.BUILDING_MENU);
	}

	public void closeBuildMenu() {
		startBuilding();
	}

	public void getInput(GameObject go) {
		setWorldState(WorldState.INPUT);
		gameInterface.focusTextPrompt();
		if (waitingForInput == null) {
			waitingForInput = new HashSet<>();
			waitingForInput.add(go);
		}
	}

	public void acceptInput(String s) {
		setWorldState(prevState);
		if (waitingForInput != null) {
			waitingForInput.forEach(go -> go.acceptInput(s));
		}
	}

	protected void ignoreInput(boolean ignore) {
		ignoringInput = ignore;
	}

	protected boolean isIgnoringInput() {
		return ignoringInput;
	}

	/**
	 * Set the paused state of the World. If the world is paused then the map
	 * will not update but the game interface will.
	 * 
	 * @param paused
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isPaused() {
		return paused;
	}

	/**
	 * If the game is paused then advances the paused elements by one frame.
	 * This has no effect if the game is not paused.
	 */
	public void step() {
		stepFrame = true;
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

		if (getWorldState() == WorldState.BUILDING) {
			// Add a block at the position of the mouse
			Input i = gc.getInput();
			Point mouseScreen = new Point(i.getMouseX(), i.getMouseY());
			Point mouseWorld = screenToWorldCoOrds(mouseScreen);
			mouseWorld = new Point((int) mouseWorld.getX(),
					(int) mouseWorld.getY());
			mouseScreen = worldToScreenCoOrds(mouseWorld);
			ItemType current = getMap().getPainter().getItemType();
			LayeredImage limg = current.getSprite().getImage();
			limg.setAlpha(0.3f);
			limg.draw(mouseScreen.getX(), mouseScreen.getY(),
					getCamera().getScale());
		}

		worldInterface.render(getState());
		gameInterface.render(getState());

	}

	public void update(GameContainer gc, int delta) {
		if (!ignoringInput) {

			if (getWorldState() != WorldState.INPUT) {
				camera.update(gc, delta);
			}
			gameInterface.update(gc, delta, getState());
			worldInterface.update(gc, delta, getState());

			if (!paused || stepFrame) {
				switch (getWorldState()) {
					case PLAYING:
						playingUpdate(gc, delta);
						break;
					case BUILDING:
						buildingUpdate(gc, delta);
						break;
					default:
						break;
				}
				stepFrame = false;
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
		WorldObject wo = map.findObjectScreen(mousePoint);
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
		// Reset on D
		if (getWorldState() != WorldState.INPUT
				&& keycode == Controller.WORLD_RESET) {
			reset();
			return;
		}

		switch (getWorldState()) {
			case PLAYING:
				if (keycode == Input.KEY_ESCAPE) {
					openMenu();
				}
				if (keycode == Controller.INVENTORY) {
					openInventoryDisplay();
				}
				if (keycode == Controller.WORLD_PAUSE) {
					setPaused(!isPaused());
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
				if (keycode == Controller.INVENTORY) {
					closeInventoryDisplay();
				}
				break;
			default:
				break;
		}

		gameInterface.keyPressed(keycode, getWorldState());
		worldInterface.keyPressed(keycode, getWorldState());
		map.keyPressed(keycode);
	}

	public void mousePressed(int button, int x, int y) {
		Point clickPoint = new Point(x, y);

		if (getWorldState() == WorldState.WATCH_SELECT) {
			watchSelectMousePressed(button, clickPoint);
		}

		worldInterface.mousePressed(button, clickPoint, getState());
		gameInterface.mousePressed(button, clickPoint, getState());
	}

	public Point screenToWorldCoOrds(Point p) {
		return worldCOT.screenToWorldCoOrds(p);
	}

	public Point worldToScreenCoOrds(Point p) {
		return worldCOT.objectToScreenCoOrds(p);
	}

	private void watchSelectMousePressed(int button, Point p) {

		// For each world object, check whether it was clicked by the mouse
		WorldObject clicked = map.findObjectScreen(p);

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
		if (newlyFinishedQuests != null) {
			newlyFinishedQuests.forEach(q -> activeQuests.remove(q));
			newlyFinishedQuests = null;
		}
	}

	public void questStarted(Quest q) {
		activeQuests.add(q);
	}

	public void questFinished(Quest q) {
		if (newlyFinishedQuests == null) {
			newlyFinishedQuests = new HashSet<>();
		}
		newlyFinishedQuests.add(q);
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
