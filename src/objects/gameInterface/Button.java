package objects.gameInterface;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import objects.images.LucyImage;

public abstract class Button extends InterfaceElement {
	public Button(Rectangle button) {
		super(button);
		setBackground(0);
	}

	public Button(Point origin, LucyImage backgroundImage) {
		super(new Rectangle(origin, backgroundImage.getWidth(),
				backgroundImage.getHeight()));
		getSprite().getImage().setLayer(0, backgroundImage);
	}

	@Override
	public void update(GameContainer gc, int delta) {

	}
}
