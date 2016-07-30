package worlds;

import worldLibrary.*;

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
			default:
				return null;
		}
	}
}
