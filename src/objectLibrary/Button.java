package objectLibrary;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import helpers.Rectangle;
import objects.InterfaceElement;

public class Button extends InterfaceElement {
	public Button(Rectangle button) {
		super(button);
		setBackground(new Color(240, 120, 180));
	}

	@Override
	public void onClick(int button) {
		System.out.println("Button has been clicked!");
	}

	@Override
	public void update(GameContainer gc, int delta) {

	}
}
