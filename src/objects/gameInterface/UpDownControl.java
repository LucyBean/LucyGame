package objects.gameInterface;

import helpers.Point;
import helpers.Rectangle;
import objects.images.SpriteBuilder;

public class UpDownControl extends InterfaceElement {
	IEList controlledElement;

	public UpDownControl(Point point, IEList controlledElement) {
		super(point, SpriteBuilder.makeUpDownControl());
		this.controlledElement = controlledElement;
	}

	private void up() {
		if (controlledElement != null) {
			controlledElement.moveUp();
		}
	}

	private void down() {
		if (controlledElement != null) {
			controlledElement.moveDown();
		}
	}

	@Override
	public void onClick(int button, Point clickPoint) {
		Rectangle upBound = getSprite().getImage().getLayer(0).getRectangle();
		Rectangle downBound = getSprite().getImage().getLayer(1).getRectangle();

		if (upBound.contains(clickPoint)) {
			up();
		}
		if (downBound.contains(clickPoint)) {
			down();
		}
	}
}
