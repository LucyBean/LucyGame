package gameInterface;

import helpers.Point;
import images.SingleSprite;

public class IEListItem extends InterfaceElement {
	private IEList parent;
	private int index;
	
	public IEListItem(IEList parent, int index, Point position, SingleSprite s) {
		super(position, s);
		this.parent = parent;
		this.index = index;
	}

	@Override
	public void onClick(int button, Point clickPoint) {
		parent.buttonClicked(index, clickPoint);
	}
}
