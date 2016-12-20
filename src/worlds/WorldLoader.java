package worlds;

import worlds.library.ColliderDemoWorld;
import worlds.library.EnemyArena;
import worlds.library.HiddenObjectDemoWorld;
import worlds.library.LevelBuildingWorld;
import worlds.library.MenuDemoWorld;
import worlds.library.PlatformerDemoWorld;
import worlds.library.QuestDemoWorld;

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
				return new EnemyArena(game);
			case 1:
				return new ColliderDemoWorld(game);
			case 2:
				return new HiddenObjectDemoWorld(game);
			case 3:
				return new PlatformerDemoWorld(game);
			case 4:
				return new QuestDemoWorld(game);
			case 5:
				return new LevelBuildingWorld(game);
			default:
				return null;
		}
	}
}
