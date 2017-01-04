package worlds;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import objects.images.ImageBuilder;
import options.GlobalOptions;

/**
 * Welcome to the game.
 * 
 * @author Lucy
 * @version 0.0.1
 *
 */
public class LucyGame extends BasicGame {
	private World world;
	private WorldLoader worldLoader = new WorldLoader(this);
	private Image splashScreen;
	private Thread loadingThread;

	public LucyGame() {
		super("LucyGame");
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new LucyGame());
			app.setDisplayMode(GlobalOptions.WINDOW_WIDTH,
					GlobalOptions.WINDOW_HEIGHT, false);
			app.setShowFPS(false);
			app.setVerbose(false);
			app.start();
		} catch (SlickException se) {
			se.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (world == null) {
			splashScreen.draw();
		} else {
			world.render(gc, g);
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		splashScreen = ImageBuilder.getSplash();

		loadingThread = new Thread() {
			@Override
			public void run() {
				ImageBuilder.loadImageData();
			}
		};
		
		loadingThread.start();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if (world != null) {
			world.update(gc, delta);
		} else if (!ImageBuilder.spriteSheetsInitialised()) {
			ImageBuilder.initSpriteSheets();
			GlobalOptions.loadFromFile();
			loadMainMenu();
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (world != null) {
			world.mousePressed(button, x, y);
		}
	}

	@Override
	public void keyPressed(int keycode, char c) {
		if (world != null) {
			world.keyPressed(keycode);
		}
	}

	public void loadMainMenu() {
		world = worldLoader.getMainMenu();
	}

	public void loadLevel(int i) {
		World w = worldLoader.getLevel(i);
		if (w != null) {
			world = w;
		}
	}

}
