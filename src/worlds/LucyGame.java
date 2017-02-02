package worlds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import io.ErrorLogger;
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
	private boolean gameCrashed = false;
	private static boolean devMode = false;

	private static String version;

	public LucyGame() {
		super("LucyGame");
	}

	public static void main(String[] args) {
		try {
			getSplashInfo();
			System.out.println(version);
			ErrorLogger.setVersion(version);
			AppGameContainer app = new AppGameContainer(new LucyGame());
			app.setDisplayMode(GlobalOptions.WINDOW_WIDTH,
					GlobalOptions.WINDOW_HEIGHT, false);
			app.setShowFPS(false);
			app.setVerbose(false);

			if (args.length >= 1) {
				String dev = args[0];
				if (dev.equals("dev")) {
					app.setVerbose(true);
					Controller.setDvorak();
					devMode = true;
				}
			}
			app.start();
		} catch (SlickException se) {
			se.printStackTrace();
		}
	}

	private static void getSplashInfo() {
		File buildInfo = new File("build_info.properties");
		version = "Version: ";
		if (buildInfo.exists()) {
			try {
				int major = 0;
				int minor = 0;
				int revision = 0;
				int build = 0;
				// Read the build info from the file
				BufferedReader br = new BufferedReader(
						new FileReader(buildInfo));
				Pattern p = Pattern.compile("build\\.(\\w+)\\.number=(\\d+)");

				String nextLine = br.readLine();
				while (nextLine != null) {
					Matcher m = p.matcher(nextLine);
					if (m.matches()) {
						String type = m.group(1);
						int val = Integer.parseInt(m.group(2));

						if (type.equals("major"))
							major = val;
						if (type.equals("minor"))
							minor = val;
						if (type.equals("revision"))
							revision = val;
						if (type.equals("build"))
							build = val;
					}
					nextLine = br.readLine();
				}
				version += String.format("v %d.%d.%02d build %04d", major,
						minor, revision, build);
				br.close();
			} catch (IOException ioe) {
				version += "unknown";
			}
		} else {
			version += "unknown";
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (world == null) {
			drawSplash();
		} else if (gameCrashed) {
			drawOops();
		} else {
			try {
				world.render(gc, g);
			} catch (Exception e) {
				gameCrashed(e);
			}
		}
	}

	private void drawSplash() {
		Font f = ImageBuilder.getFont();

		int x = 50;
		int y = 50;
		int spacing = f.getLineHeight() + 10;

		f.drawString(x, y, "LucyGame");
		y += spacing;
		Date d = new Date();
		f.drawString(x, y, d.toString());
		y += spacing;
		f.drawString(x, y, version);
	}

	private void drawOops() {
		Font f = ImageBuilder.getFont();

		int x = 50;
		int y = 50;
		int spacing = f.getLineHeight() + 10;

		f.drawString(x, y, "Oops, looks like the game crashed!");
		y += spacing;
		f.drawString(x, y, "The error has been logged and I will fix it ASAP.");
		y += spacing * 3;
		f.drawString(x, y, "Push space to restart the game.");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		ImageBuilder.initFont();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		try {
			if (!gameCrashed) {
				if (world != null) {
					world.update(gc, delta);
				} else if (!ImageBuilder.spriteSheetsInitialised()) {
					long start = System.currentTimeMillis();
					ImageBuilder.initSpriteSheets();
					GlobalOptions.loadFromFile();
					loadMainMenu();
					long end = System.currentTimeMillis();
					System.out.println("Load time: " + (end - start));
				}
			}
		} catch (Exception e) {
			gameCrashed(e);
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (world != null && !gameCrashed) {
			try {
				world.mousePressed(button, x, y);
			} catch (Exception e) {
				gameCrashed(e);
			}
		}
	}

	@Override
	public void keyPressed(int keycode, char c) {
		if (world != null && !gameCrashed) {
			try {
				world.keyPressed(keycode);
			} catch (Exception e) {
				gameCrashed(e);
			}
		} else if (gameCrashed) {
			if (keycode == Input.KEY_SPACE) {
				loadMainMenu();
			}
		}
	}

	private void gameCrashed(Exception e) {
		gameCrashed = true;
		ErrorLogger.log(e, 5);
		if (devMode) {
			e.printStackTrace();
		}
	}

	public void loadMainMenu() {
		gameCrashed = false;
		world = worldLoader.getMainMenu();
	}

	public void loadLevel(int i) {
		World w = worldLoader.getLevel(i);
		if (w != null) {
			world = w;
		}
	}

}
