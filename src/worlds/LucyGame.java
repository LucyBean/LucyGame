package worlds;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import objectLibs.WorldLibrary;

public class LucyGame extends BasicGame {
	World world;

	public LucyGame() {
		super("LucyGame");
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new LucyGame());
			app.setDisplayMode(GlobalOptions.WINDOW_WIDTH, GlobalOptions.WINDOW_HEIGHT, false);
			app.start();
		} catch (SlickException se) {
			se.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		world.render(gc, g);
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		world = WorldLibrary.getColliderWorld();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		world.update(gc, delta);
	}

}
