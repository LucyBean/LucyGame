package worlds.lib;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import objects.images.ImageBuilder;
import objects.images.StaticImage;
import worlds.LucyGame;
import worlds.World;

public class ImageDrawingWorld extends World {
	Collection<StaticImage> images;

	public ImageDrawingWorld(LucyGame game) {
		super(game, "Image drawing world");
		
		images = new LinkedList<>();
		for (int i = 1; i < 20; i++) {
			images.add(ImageBuilder.makeRectangle(i*4, 8, Color.red));
		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		float x = 20;
		float y = 20;
		float padding = 10;
		
		Iterator<StaticImage> is = images.iterator();
		while (is.hasNext()) {
			StaticImage img = is.next();
			img.draw(x, y, 1);
			y += padding + img.getHeight();
		}
	}

}
