package worlds.lib;

import java.util.Collection;
import java.util.Optional;

import org.newdawn.slick.SlickException;

import io.WorldMapImporterExporter;
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
		Optional<Collection<WorldObject>> oObjects = WorldMapImporterExporter.importObjects(
				"arena");
		assert oObjects.isPresent();
		oObjects.ifPresent(objects -> getMap().addObjects(objects));

		Player p = getMap().getPlayer();
		if (p != null) {
			getCamera().setTarget(p);
		}
	}

}
