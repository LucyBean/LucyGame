package worldLibrary;

import java.util.Random;

import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.HiddenSquare;
import objectLibrary.Player;
import objects.WorldObject;
import worlds.LucyGame;
import worlds.World;

public class HiddenObjectDemoWorld extends World {

	public HiddenObjectDemoWorld(LucyGame game) {
		super(game, "Hidden objects");
	}

	@Override
	public void init() throws SlickException {
		WorldObject mover = new Player(new Point(2, 2));
		addObject(mover);

		drawWallBorder();

		for (int i = 0; i < 10; i++) {
			Random r = new Random();
			Point position = new Point(r.nextFloat() * 17 + 1,
					r.nextFloat() * 12 + 1);
			WorldObject go = new HiddenSquare(position);
			addObject(go);
		}
	}
}
