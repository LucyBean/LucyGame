package objectLibrary;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Actor;
import worlds.WorldLayer;

public class Button extends Actor {
	public Button(Point origin) {
		super(origin, WorldLayer.INTERFACE, SpriteLibrary.BUTTON);
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
