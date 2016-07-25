package objectLibrary;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Actor;
import objects.Sprite;
import worlds.WorldLayer;

public class Button extends Actor {
	public Button(Point origin) {
		super(origin, WorldLayer.INTERFACE, SpriteLibrary.BUTTON);
	}

	public void setText(String s) {
		try {
			Image img = SpriteLibrary.BUTTON.getImage();
			Image newImg = img.copy();
			Graphics g = newImg.getGraphics();
			float w = g.getFont().getWidth(s);
			// draw the string at (imgWidth - w)/2
			g.drawString(s, (newImg.getWidth()-w)/2, 5);
			g.flush();
			setSprite(new Sprite(newImg));
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
