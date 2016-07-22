package worldLibrary;

import java.util.Random;

import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.HiddenSquare;
import objectLibrary.Player;
import objects.GameObject;
import worlds.World;
import worlds.WorldLayer;

public class HiddenObjectDemoWorld extends World {
	@Override
	public void init() throws SlickException {
		GameObject mover = new Player(new Point(80, 300));
		addObject(mover, WorldLayer.PLAYER);

		drawWallBorder();
		
		for (int i = 0; i < 10; i++) {
			Random r = new Random();
			Point position = new Point(r.nextFloat() * 550 + 50,
					r.nextFloat() * 400 + 50);
			GameObject go = new HiddenSquare(position);
			addObject(go, WorldLayer.WORLD);
		}
	}
}
