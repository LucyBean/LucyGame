package gameInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import helpers.Dir;
import helpers.Point;
import images.Sprite;
import worlds.World;

public abstract class IEList extends InterfaceElement {
	private Point nextPosition;
	private List<IEListItem> items;
	private int minItemDisplayed;

	public IEList(Point firstPoint, int numDisplayElems,
			Supplier<Sprite> spriteMaker) {
		nextPosition = firstPoint;
		items = new ArrayList<IEListItem>();
		minItemDisplayed = 0;

		for (int i = 0; i < numDisplayElems; i++) {
			Sprite s = spriteMaker.get();
			IEListItem ieli = new IEListItem(this, i, nextPosition, s);
			getElementSprite(i, s);
			items.add(ieli);
			nextPosition = nextPosition.move(Dir.SOUTH,
					s.getBoundingRectangle().getHeight());
		}
	}

	@Override
	final public void setWorld(World world) {
		super.setWorld(world);
		items.stream().forEach(i -> i.setWorld(world));
	}

	@Override
	final public void mousePressed(int button, Point clickPoint) {
		items.stream().forEach(i -> i.mousePressed(button, clickPoint));
	}

	@Override
	public void onClick(int button) {

	}

	@Override
	protected void draw() {
		items.stream().forEach(s -> s.render());
	}

	final public void buttonClicked(int buttonIndex) {
		elementClicked(buttonIndex + minItemDisplayed);
	}

	/**
	 * Updates the Sprites within the IEList. Call this if the IEList is used to
	 * represent any underlying implementation that has changed.
	 */
	final public void updateSprites() {
		for (int i = 0; i < items.size(); i++) {
			Sprite s = items.get(i).getSprite();
			getElementSprite(i, s);
		}
	}

	public abstract void elementClicked(int elementIndex);

	public abstract void getElementSprite(int elementIndex, Sprite s);
}
