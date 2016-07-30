package objectLibrary;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Rectangle;
import objectLibs.SpriteLibrary;
import objects.InterfaceElement;
import objects.Sprite;

public class Button extends InterfaceElement {
	public Button(Rectangle button) {
		super(button.getTopLeft(), SpriteLibrary.createRectangle(button, 1,
				new Color(230, 130, 230)));
	}

	public void setText(String s) {
		try {
			Image img = getSprite().getImage();
			Graphics g = img.getGraphics();
			float w = g.getFont().getWidth(s);
			float h = g.getFont().getHeight(s);
			// draw the string centered
			g.setColor(Color.black);
			g.drawString(s, (img.getWidth() - w) / 2,
					(img.getHeight() - h) / 2);
			g.flush();
			setSprite(new Sprite(img));
		} catch (SlickException se) {
			System.err.println(
					"Tried to add text " + s + " to button and failed");
			se.printStackTrace();
		}
	}

	@Override
	public void onClick(int button) {
		System.out.println("Button has been clicked!");
	}

	@Override
	public void update(GameContainer gc, int delta) {
		// TODO Auto-generated method stub

	}
}
