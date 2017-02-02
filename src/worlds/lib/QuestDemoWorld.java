package worlds.lib;

import java.util.Collection;

import org.newdawn.slick.SlickException;

import io.WorldMapImporterExporter;
import objects.world.WorldObject;
import worlds.LucyGame;
import worlds.World;

public class QuestDemoWorld extends World {
	public QuestDemoWorld(LucyGame game) {
		super(game, "Quest demo");
	}

	@Override
	public void init() throws SlickException {
		Collection<WorldObject> objects = WorldMapImporterExporter.importObjects("questDemo");
		getMap().addObjects(objects);
		
		loadScripts("questDemo");
	}
}
