package objects.gameInterface;

import org.newdawn.slick.GameContainer;

import helpers.Rectangle;

public abstract class Button extends InterfaceElement {
	public Button(Rectangle button) {
		super(button);
		setBackground(0);
	}

	@Override
	public void update(GameContainer gc, int delta) {

	}
}
