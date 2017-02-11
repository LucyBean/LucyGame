package worlds.lib;

import java.util.Collection;
import java.util.Optional;

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
		Optional<Collection<WorldObject>> oObjects = WorldMapImporterExporter.importObjects(
				"questDemo");

		assert oObjects.isPresent();
		oObjects.ifPresent(objects -> getMap().addObjects(objects));

		loadScripts("questDemo");

		showWorldConversation(1);
	}
}
