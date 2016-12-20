package objects.gameInterface;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import objects.images.LucyImage;

public abstract class Button extends InterfaceElement {
	public Button(Rectangle button) {
		super(button);
		setBackground(new Color(240, 120, 180));
	}

	public Button(Point origin, LucyImage backgroundImage) {
		super(new Rectangle(origin, backgroundImage.getWidth(),
				backgroundImage.getHeight()));
		setBackground(backgroundImage);
	}

	@Override
	public void update(GameContainer gc, int delta) {

	}
}
