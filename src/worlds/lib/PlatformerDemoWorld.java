package worlds.lib;

import java.util.Collection;
import java.util.Optional;

import org.newdawn.slick.SlickException;

import helpers.Point;
import io.WorldMapImporterExporter;
import objects.world.ClimbingWallMarker;
import objects.world.WorldObject;
import objects.world.lib.MovingPlatform;
import objects.world.lib.PushableBlock;
import worlds.LucyGame;
import worlds.World;

public class PlatformerDemoWorld extends World {

	public PlatformerDemoWorld(LucyGame game) {
		super(game, "Platformer Demo");
	}

	@Override
	public void init() throws SlickException {
		// Import from file
		Optional<Collection<WorldObject>> oObjects = WorldMapImporterExporter.importObjects(
				"platformer");
		assert oObjects.isPresent();
		oObjects.ifPresent(objects -> getMap().addObjects(objects));

		ClimbingWallMarker cw = new ClimbingWallMarker(new Point(19, 4), 8);
		addObject(cw);

		MovingPlatform mp = new MovingPlatform(new Point(22, 4),
				new Point(22, 11), 3000);
		addObject(mp);

		PushableBlock pb = new PushableBlock(new Point(13, -5));
		addObject(pb);
	}

}
