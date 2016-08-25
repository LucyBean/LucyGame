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

	/**
	 * Creates a new IEList. An IEList is used to display a number of elements
	 * on screen in an ordered manner. The IEList can scroll up and down to
	 * display a large number of elements using a small number of buttons.
	 * 
	 * This constructor will initialise all IEListItem sprites using the
	 * provided Sprite Supplier.
	 * 
	 * @param firstPoint
	 *            The Point for the first object in the list.
	 * @param numDisplayElems
	 *            The number of elements that are displayed on the screen at any
	 *            one time. The IEList can scroll to permit showing large lists.
	 * @param spriteMaker
	 *            A Supplier that will produce a blank Sprite for each IEList
	 *            element.
	 */
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
			getElementSprite(i + minItemDisplayed, s);
		}
	}

	public abstract void elementClicked(int elementIndex);

	/**
	 * Updates the IEList buttons to display the correct elements.
	 * 
	 * @param elementIndex
	 *            The index of the element that is to be displayed in this
	 *            IEList button.
	 * @param s
	 *            The current sprite of the IEList button. This should be
	 *            modified to show information for the correct element.
	 * @param spriteMaker
	 *            A function to produce an empty Sprite. This should be called
	 *            if there is element information to display but s is null.
	 */
	public abstract void getElementSprite(int elementIndex, Sprite s);
}
