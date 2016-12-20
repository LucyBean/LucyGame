package worlds;

import worlds.lib.ColliderDemoWorld;
import worlds.lib.EnemyArena;
import worlds.lib.HiddenObjectDemoWorld;
import worlds.lib.LevelBuildingWorld;
import worlds.lib.MenuDemoWorld;
import worlds.lib.PlatformerDemoWorld;
import worlds.lib.QuestDemoWorld;

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
