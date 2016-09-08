package worlds;

import worldLibrary.CameraObjectLoadingDemo;
import worldLibrary.ColliderDemoWorld;
import worldLibrary.HiddenObjectDemoWorld;
import worldLibrary.LevelBuildingWorld;
import worldLibrary.MapImportingDemo;
import worldLibrary.MenuDemoWorld;
import worldLibrary.PlatformerDemoWorld;

public class WorldLoader {
	LucyGame game;
	
	public WorldLoader(LucyGame game) {
		this.game = game;
	}
	
	public World getMainMenu() {
		return new MenuDemoWorld(game);
	}
	
	public World getLevel(int i) {
		switch(i) {
			case 0:
				return new CameraObjectLoadingDemo(game);
			case 1:
				return new ColliderDemoWorld(game);
			case 2:
				return new HiddenObjectDemoWorld(game);
			case 3:
				return new MapImportingDemo(game);
			case 4:
				return new PlatformerDemoWorld(game);
			case 5:
				return new LevelBuildingWorld(game);
			default:
				return null;
		}
	}
}
