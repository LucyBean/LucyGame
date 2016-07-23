package worldLibrary;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import worlds.World;

public class MapImportingDemo extends World {
	@Override
	public void init() throws SlickException {
		buildMap(new TiledMap("data/exampleMap.tmx"));
	}

	private void buildMap(TiledMap tm) {
		for (int y = 0; y < tm.getHeight(); y++) {
			for (int x = 0; x < tm.getWidth(); x++) {
				int tileID = tm.getTileId(x, y, 0);
				
				
			}
		}
	}
}
