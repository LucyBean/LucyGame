package objectLibs;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.Button;
import objectLibrary.Player;
import objectLibrary.Wall;
import objects.Actor;
import objects.Collider;
import objects.GameObject;
import objects.InteractBox;
import objects.Sprite;
import objects.Static;
import worlds.World;
import worlds.WorldLayer;

public class WorldLibrary {
	public static World[] worlds = new World[10];

	public static World getPlatformDemoWorld() {
		if (worlds[0] == null) {
			worlds[0] = new World() {
				@Override
				public void init() throws SlickException {
					GameObject background = new Static(Point.ZERO,
							new Sprite(new Image("data/Desert.jpg")));
					addObject(background, WorldLayer.BACKGROUND);

					GameObject player = new Player(new Point(40, 50));
					addObject(player, WorldLayer.PLAYER);

					for (int i = 0; i < 10; i++) {
						addObject(new Wall(new Point(200 + 50 * i, 200)), WorldLayer.WORLD);
					}
				}
			};
		}

		return worlds[0];
	}

	public static World getButtonWorld() {
		if (worlds[1] == null) {
			worlds[1] = new World() {
				@Override
				public void init() throws SlickException {
					Actor button = new Button(new Point(100, 100));
					addObject(button, WorldLayer.INTERFACE);

				}
			};
		}

		return worlds[1];
	}

	public static World getColliderWorld() {
		if (worlds[2] == null) {
			worlds[2] = new World() {
				@Override
				public void init() throws SlickException {
					GameObject mover = new Player(new Point(80, 300));
					addObject(mover, WorldLayer.PLAYER);

					for (int i = 0; i < 13; i++) {
						addObject(new Wall(new Point(0 + 50 * i, 0)), WorldLayer.WORLD);
						addObject(new Wall(new Point(0 + 50 * i, 450)), WorldLayer.WORLD);
					}
					for (int i = 0; i < 8; i++) {
						addObject(new Wall(new Point(0, 50 + 50 * i)), WorldLayer.WORLD);
						addObject(new Wall(new Point(600, 50 + 50 * i)), WorldLayer.WORLD);
					}

					for (int i = 0; i < 10; i++) {
						Random r = new Random();
						Point position = new Point(r.nextFloat() * 550 + 50,
								r.nextFloat() * 400 + 50);
						GameObject go = new Static(position, null, new Collider(Point.ZERO, 20, 20),
								new InteractBox(new Point(20,0), 20, 20));
						addObject(go, WorldLayer.WORLD);
					}
				}
			};
		}

		return worlds[2];
	}
}
