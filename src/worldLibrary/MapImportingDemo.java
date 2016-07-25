package worldLibrary;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import helpers.Point;
import objectLibrary.GravityPlayer;
import objectLibrary.Wall;
import objects.GameObject;
import worlds.LucyGame;
import worlds.World;

public class MapImportingDemo extends World {

	public MapImportingDemo(LucyGame game) {
		super(game);
	}

	@Override
	public void init() throws SlickException {
		buildMap(new TiledMap("data/exampleMap.tmx"));
	}

	private void buildMap(TiledMap tm) {
		for (int y = 0; y < tm.getHeight(); y++) {
			for (int x = 0; x < tm.getWidth(); x++) {
				int tileID = tm.getTileId(x, y, 0);
				String s = tm.getTileProperty(tileID, "block_id", "0");
				int id = Integer.parseInt(s);

				if (id == 1) {
					addObject(new Wall(new Point(x, y)));
				}
				if (id == 2) {
					GameObject gp = new GravityPlayer(new Point(x, y));
					addObject(gp);
					setCameraTarget(gp);
				}
			}
		}
	}
}
