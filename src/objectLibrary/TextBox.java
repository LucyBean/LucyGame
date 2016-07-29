package objectLibrary;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Point;
import objects.InterfaceElement;
import objects.Sprite;

public class TextBox extends InterfaceElement {

	public TextBox(Point origin, int width, int height) {
		super(origin, null);
		Image img;
		try {
			img = new Image(width, height);
			Graphics g = img.getGraphics();
			g.setColor(Color.white);
			g.drawString("Hello, world!", 0, 0);
			g.flush();
			g.setColor(Color.white);
			g.fillRect(0, 0, width - 1, height - 1);
			g.setColor(Color.cyan);
			g.drawRect(0, 0, width - 1, height - 1);
			g.drawRect(1, 1, width - 3, height - 3);
			g.flush();
			setSprite(new Sprite(img));
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(int button) {
		
	}

}
