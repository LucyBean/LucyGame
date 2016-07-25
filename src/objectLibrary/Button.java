package objectLibrary;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Rectangle;
import objectLibs.SpriteLibrary;
import objects.Actor;
import objects.Sprite;
import worlds.WorldLayer;

public class Button extends Actor {
	public Button(Rectangle button) {
		super(button.getTopLeft(), WorldLayer.INTERFACE, SpriteLibrary.createRectangle(button,
				new Color(230, 130, 230)));
	}

	public void setText(String s) {
		try {
			Image img = getSprite().getImage();
			Graphics g = img.getGraphics();
			float w = g.getFont().getWidth(s);
			float h = g.getFont().getHeight(s);
			// draw the string at (imgWidth - w)/2
			g.drawString(s, (img.getWidth()-w)/2, (img.getHeight()-h)/2);
			g.flush();
			setSprite(new Sprite(img));
		} catch (SlickException se) {
			System.err.println("Tried to add text " + s + " to button and failed");
			se.printStackTrace();
		}
	}

	@Override
	public void onClick() {
		System.out.println("Button has been clicked!");
	}

	@Override
	protected void resetActorState() {

	}

	@Override
	public void act(GameContainer gc, int delta) {
		// TODO Auto-generated method stub

	}
}
