package worlds.lib;

import java.util.Collection;

import org.newdawn.slick.SlickException;

import helpers.WorldMapImporterExporter;
import objects.world.WorldObject;
import objects.world.characters.Player;
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
	}

}