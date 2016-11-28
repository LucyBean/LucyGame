package worldLibrary;

import java.util.Collection;

import org.newdawn.slick.SlickException;

import exporting.WorldMapImporterExporter;
import objects.WorldObject;
import player.Player;
import worlds.LucyGame;
import worlds.World;

public class PlatformerDemoWorld extends World {

	public PlatformerDemoWorld(LucyGame game) {
		super(game, "Platformer Demo");
	}

	@Override
	public void init() throws SlickException {
		// Import from file
		Collection<WorldObject> objects = WorldMapImporterExporter.importObjects(
				"platformer");
		getMap().addObjects(objects);
		
		Player p = getMap().getPlayer();
		if (p != null) {
			getCamera().setTarget(p);
		}
	}

}
