package worlds.lib;

import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import objects.attachments.Collider;
import objects.attachments.InteractBox;
import objects.images.Sprite;
import objects.images.SpriteBuilder;
import objects.world.Actor;
import objects.world.Static;
import options.GlobalOptions;
import worlds.LucyGame;
import worlds.World;
import worlds.WorldLayer;

public class CameraObjectLoadingDemo extends World {
	CameraBox cb;
	WorldSector[][] sectorMap;
	final int WORLD_WIDTH = 10;
	final int WORLD_HEIGHT = 10;

	public CameraObjectLoadingDemo(LucyGame game) {
		super(game, "Camera object loading");
	}

	@Override
	public void init() throws SlickException {
		cb = new CameraBox(new Point(2.5f, 2.5f));
		cb.useGravity(false);
		addObject(cb);

		buildSectors(WORLD_WIDTH, WORLD_HEIGHT);

		// Manually activate the initial sectors
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				sectorMap[i][j].activate();
			}
		}
	}

	private void buildSectors(int width, int height) {
		sectorMap = new WorldSector[WORLD_WIDTH][WORLD_HEIGHT];
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				// Create
				WorldSector ws = new WorldSector(new Point(x * 3, y * 3), x, y);
				sectorMap[x][y] = ws;
				addObject(ws);
			}
		}
	}

	/**
	 * Loads the required sectors for a player object travelling in
	 * playerDirection who has just entered the sector at sectorX sectorY
	 * 
	 * @param playerDirection
	 *            The direction that the player is moving.
	 * @param sectorX
	 *            The x position of the sector in sector co-ordinates.
	 * @param sectorY
	 *            The y position of the sector in sector co-ordinates.
	 */
	public void loadSectors(Dir playerDirection, int sectorX, int sectorY) {
		if (playerDirection == null) {
			return;
		}

		// if moving EAST or WEST, load due-, SOUTH-, and NORTH- sectors too
		Point[] toLoad = new Point[3];
		if (playerDirection == Dir.EAST || playerDirection == Dir.WEST) {
			int newX = sectorX + (int) playerDirection.asPoint().getX();
			toLoad[0] = new Point(newX, sectorY); // due
			toLoad[1] = new Point(newX, sectorY - 1); // north
			toLoad[2] = new Point(newX, sectorY + 1); // south
		}
		// if moving NORTH or SOUTH, load due-, EAST-, and WEST- sectors too
		else {
			int newY = sectorY + (int) playerDirection.asPoint().getY();
			toLoad[0] = new Point(sectorX, newY); // due
			toLoad[1] = new Point(sectorX - 1, newY); // west
			toLoad[2] = new Point(sectorX + 1, newY); // east
		}

		for (int i = 0; i < toLoad.length; i++) {
			if (toLoad[i] != null) {
				int x = (int) toLoad[i].getX();
				int y = (int) toLoad[i].getY();
				if (validSector(x, y)) {
					sectorMap[x][y].activate();
				}
			}
		}
	}

	/**
	 * Unloads sectors for a player object travelling in playerDirection who has
	 * just left the sector at sectorX sectorY
	 * 
	 * @param playerDirection
	 * @param sectorX
	 * @param sectorY
	 */
	public void unloadSectors(Dir playerDirection, int sectorX, int sectorY) {
		if (playerDirection == null) {
			return;
		}

		// if moving EAST or WEST, load due-, SOUTH-, and NORTH- sectors too
		Point[] toUnload = new Point[3];
		if (playerDirection == Dir.EAST || playerDirection == Dir.WEST) {
			int newX = sectorX - (int) playerDirection.asPoint().getX();
			toUnload[0] = new Point(newX, sectorY); // due
			toUnload[1] = new Point(newX, sectorY - 1); // north
			toUnload[2] = new Point(newX, sectorY + 1); // south
		}
		// if moving NORTH or SOUTH, load due-, EAST-, and WEST- sectors too
		else {
			int newY = sectorY - (int) playerDirection.asPoint().getY();
			toUnload[0] = new Point(sectorX, newY); // due
			toUnload[1] = new Point(sectorX - 1, newY); // west
			toUnload[2] = new Point(sectorX + 1, newY); // east
		}

		for (int i = 0; i < toUnload.length; i++) {
			if (toUnload[i] != null) {
				int x = (int) toUnload[i].getX();
				int y = (int) toUnload[i].getY();
				if (validSector(x, y)) {
					sectorMap[x][y].deactivate();
				}
			}
		}
	}

	private boolean validSector(int x, int y) {
		if (x < 0 || x >= WORLD_WIDTH || y < 0 || y >= WORLD_HEIGHT) {
			return false;
		}

		return true;
	}
}

class CameraBox extends Actor {
	static Sprite sprite = SpriteBuilder.getColouredRectangle(2,1,1,32);
	float speed;

	public CameraBox(Point origin) {
		super(origin, WorldLayer.PLAYER, null, sprite,
				Optional.of(new Collider(Point.ZERO, 2, 1)), null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void resetActorState() {
		speed = 0.008f;
	}

	@Override
	public void act(GameContainer gc, int delta) {
		Input input = gc.getInput();
		if (input.isKeyDown(Input.KEY_COMMA)) {
			move(Dir.NORTH, speed * delta);
		}
		if (input.isKeyDown(Input.KEY_O)) {
			move(Dir.SOUTH, speed * delta);
		}
		if (input.isKeyDown(Input.KEY_A)) {
			move(Dir.WEST, speed * delta);
		}
		if (input.isKeyDown(Input.KEY_E)) {
			move(Dir.EAST, speed * delta);
		}
	}
}

class WorldSector extends Static {
	static Rectangle spriteRect = new Rectangle(Point.ZERO, 3, 3);
	static Sprite active = SpriteBuilder.getColouredRectangle(spriteRect, 5, GlobalOptions.GRID_SIZE);
	static Sprite inactive = SpriteBuilder.getColouredRectangle(spriteRect, 0, GlobalOptions.GRID_SIZE);

	boolean activeSector;
	final WorldSector[] neighbours;
	final int x;
	final int y;

	public WorldSector(Point origin, int x, int y) {
		super(origin, WorldLayer.WORLD, null, null, null,
				Optional.of(new InteractBox(Point.ZERO, 3, 3)));
		neighbours = new WorldSector[Dir.values().length];
		this.x = x;
		this.y = y;
	}

	@Override
	protected void resetStaticState() {
		deactivate();
	}
/*
	@Override
	public void overlapStart(WorldObject a) {
		if (a instanceof CameraBox) {
			CameraBox cb = (CameraBox) a;
			// activate neighbour sectors
			CameraObjectLoadingDemo cw = (CameraObjectLoadingDemo) getWorld();
			//cw.loadSectors(cb.getLastDirectionMoved(), x, y);
		}
	}

	@Override
	public void overlapEnd(WorldObject a) {
		if (a instanceof CameraBox) {
			CameraBox cb = (CameraBox) a;
			// deactivate neighbour sectors
			CameraObjectLoadingDemo cw = (CameraObjectLoadingDemo) getWorld();
			//cw.unloadSectors(cb.getLastDirectionMoved(), x, y);
		}
	}
*/
	public void activate() {
		activeSector = true;
		setSprite(active);
	}

	public void deactivate() {
		activeSector = false;
		setSprite(inactive);
	}

	public void setNextSector(Dir d, WorldSector ws) {
		neighbours[d.ordinal()] = ws;
	}
}
