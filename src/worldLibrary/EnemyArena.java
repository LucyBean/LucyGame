package worldLibrary;

import java.util.Collection;

import org.newdawn.slick.SlickException;

import characters.DogEnemy;
import characters.Enemy;
import exporting.WorldMapImporterExporter;
import helpers.Point;
import objects.WorldObject;
import player.Player;
import worlds.LucyGame;
import worlds.World;

public class EnemyArena extends World {

	public EnemyArena(LucyGame game) {
		super(game, "Enemy arena");
	}

	@Override
	public void init() throws SlickException {
		// Import from file
		Collection<WorldObject> objects = WorldMapImporterExporter.importObjects(
				"arena");
		getMap().addObjects(objects);
		
		Player p = getMap().getPlayer();
		if (p != null) {
			getCamera().setTarget(p);
		}
		
		Enemy de = new DogEnemy(new Point(14, 0));
		addObject(de);
	}

}
