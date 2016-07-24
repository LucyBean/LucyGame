package worldLibrary;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import helpers.Point;
import objectLibrary.GravityPlayer;
import objectLibrary.Wall;
import worlds.World;
import worlds.WorldLayer;

public class MapImportingDemo extends World {

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
					addObject(new Wall(new Point(x, y)), WorldLayer.WORLD);
				}
				if (id == 2) {
					addObject(new GravityPlayer(new Point(x, y)), WorldLayer.PLAYER);
				}
			}
		}
	}
}
