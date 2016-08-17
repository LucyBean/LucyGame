package gameInterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

import helpers.Point;
import helpers.Rectangle;

public class Button extends InterfaceElement {
	public Button(Rectangle button) {
		super(button);
		setBackground(new Color(240, 120, 180));
	}
	
	public Button(Point origin, Image backgroundImage) {
		super(new Rectangle(origin, backgroundImage.getWidth(), backgroundImage.getHeight()));
		setBackground(backgroundImage);
	}

	@Override
	public void onClick(int button) {
		System.out.println("Button has been clicked!");
	}

	@Override
	public void update(GameContainer gc, int delta) {

	}
}
