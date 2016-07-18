package objectLibrary;

import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Actor;

public class Button extends Actor {
	public Button(Point origin) {
		super(origin, SpriteLibrary.BUTTON);
	}
	
	@Override
	public void onClick() {
		System.out.println("Button has been clicked!");
	}
}
