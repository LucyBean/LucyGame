package worlds.lib;

import java.util.Collection;

import org.newdawn.slick.SlickException;

import helpers.Point;
import helpers.WorldMapImporterExporter;
import objects.library.MovingPlatform;
import objects.world.ClimbingWallMarker;
import objects.world.WorldObject;
import objects.world.characters.Player;
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
		
		ClimbingWallMarker cw = new ClimbingWallMarker(new Point(19, 4), 8);
		addObject(cw);
		
		MovingPlatform mp = new MovingPlatform(new Point(22, 4), 6);
		addObject(mp);
	}

}
