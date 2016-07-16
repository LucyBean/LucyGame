package objectLibs;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;
import objects.Actor;
import objects.Collider;
import objects.GameObject;
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
					GameObject background = new Static(Point.ZERO, new Sprite(new Image("data/Desert.jpg")));
					addObject(background, WorldLayer.BACKGROUND);

					GameObject player = new Actor(new Point(40, 50), SpriteLibrary.PLAYER, new Collider(Point.ZERO, 40, 80), null) {
						float speed = 0.3f;
						@Override
						public void act(GameContainer gc, int delta) {
							Input input = gc.getInput();
							
							if (input.isKeyDown(Input.KEY_COMMA)) {
								move(Dir.NORTH, speed*delta);
							}
							if (input.isKeyDown(Input.KEY_O)) {
								move(Dir.SOUTH, speed*delta);
							}
							if (input.isKeyDown(Input.KEY_A)) {
								move(Dir.WEST, speed*delta);
							}
							if (input.isKeyDown(Input.KEY_E)) {
								move(Dir.EAST, speed*delta);
							}
						}
					};
					addObject(player, WorldLayer.PLAYER);
					
					for (int i = 0; i < 10; i++) {
						addObject(GameObjectLibrary.WALL(new Point(200 + 50*i, 200)), WorldLayer.WORLD);
					}
				}
			};
		}
		
		return worlds[0];
	}
	
	public static World getButtonWorld() {
		if(worlds[1] == null) {
			worlds[1] = new World() {
				@Override
				public void init() throws SlickException {
					Actor button = GameObjectLibrary.BUTTON(new Point(100, 100));
					addObject(button, WorldLayer.INTERFACE);
					
				}
			};
		}
		
		return worlds[1];
	}
	
	public static World getColliderWorld() {
		if(worlds[2] == null) {
			worlds[2] = new World() {
				@Override
				public void init() throws SlickException {
					GameObject mover = new Actor(new Point(80, 300), SpriteLibrary.PLAYER, new Collider(Point.ZERO, 40, 80), null) {
						float speed = 3.0f;
						@Override
						public void act(GameContainer gc, int delta) {
							Input input = gc.getInput();
							if (input.isKeyDown(Input.KEY_COMMA)) {
								move(Dir.NORTH, speed*delta);
							}
							if (input.isKeyDown(Input.KEY_O)) {
								move(Dir.SOUTH, speed*delta);
							}
							if (input.isKeyDown(Input.KEY_A)) {
								move(Dir.WEST, speed*delta);
							}
							if (input.isKeyDown(Input.KEY_E)) {
								move(Dir.EAST, speed*delta);
							}
						}
					};
					addObject(mover, WorldLayer.PLAYER);
					
					for (int i = 0; i < 13; i++) {
						addObject(GameObjectLibrary.WALL(new Point(0 + 50*i, 0)), WorldLayer.WORLD);
						addObject(GameObjectLibrary.WALL(new Point(0 + 50*i, 450)), WorldLayer.WORLD);
					}
					for (int i = 0; i < 8; i++) {
						addObject(GameObjectLibrary.WALL(new Point(0, 50 + 50*i)), WorldLayer.WORLD);
						addObject(GameObjectLibrary.WALL(new Point(600, 50 + 50*i)), WorldLayer.WORLD);
					}
					
					for (int i = 0; i < 1; i++) {
						Random r = new Random();
						Point position = new Point(r.nextFloat()*550 + 50, r.nextFloat()*400 + 50);
						GameObject go = new Static(position, null, new Collider(Point.ZERO, 20, 20), null);
						addObject(go, WorldLayer.WORLD);
					}
				}
			};
		}
		
		return worlds[2];
	}
}
