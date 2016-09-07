package gameInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import images.Sprite;
import worlds.World;

public abstract class IEList extends InterfaceElement {
	private Point nextPosition;
	private List<IEListItem> buttons;
	private int minItemDisplayed;
	private int selectedIndex;
	private Consumer<Sprite> setSelectedSprite;
	private boolean usingSelection;

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
	 * @param setSelectedSprite
	 *            A Consumer that takes a Sprite and applies some effect to
	 *            display that the corresponding button has been selected.
	 */
	public IEList(Point firstPoint, int numDisplayElems,
			Supplier<Sprite> spriteMaker, Consumer<Sprite> setSelectedSprite) {
		nextPosition = firstPoint;
		buttons = new ArrayList<IEListItem>();
		minItemDisplayed = 0;
		this.setSelectedSprite = setSelectedSprite;
		usingSelection = (setSelectedSprite != null);

		for (int i = 0; i < numDisplayElems; i++) {
			Sprite s = spriteMaker.get();
			IEListItem ieli = new IEListItem(this, i, nextPosition, s);
			getElementSprite(i, s);
			buttons.add(ieli);
			nextPosition = nextPosition.move(Dir.SOUTH,
					s.getBoundingRectangle().getHeight());
		}

		setSelectedIndex(0);
	}

	/**
	 * Creates a new IEList which does not have the ability to select an
	 * element.
	 * 
	 * @param firstPoint
	 * @param numDisplayElems
	 * @param spriteMaker
	 */
	public IEList(Point firstPoint, int numDisplayElems,
			Supplier<Sprite> spriteMaker) {
		this(firstPoint, numDisplayElems, spriteMaker, null);
	}

	/**
	 * Checks whether an element exists at this index.
	 * 
	 * @param elementIndex
	 * @return
	 */
	public boolean elementExists(int elementIndex) {
		return elementIndex >= 0 && elementIndex < getNumElements();
	}

	private void setSelectedIndex(int newButtonIndex) {
		if (usingSelection) {
			// check if the item to select exists
			int newElementIndex = newButtonIndex + minItemDisplayed;
			if (elementExists(newElementIndex)) {

				if (usingSelection) {
					// deselect old index
					int oldButtonIndex = selectedIndex;
					int oldElementIndex = selectedIndex + minItemDisplayed;
					if (oldButtonIndex >= 0
							&& oldButtonIndex < buttons.size()) {
						getElementSprite(oldElementIndex,
								buttons.get(oldButtonIndex).getSprite());
					}

					// select new index
					if (newButtonIndex >= 0
							&& newButtonIndex < buttons.size()) {
						setSelectedSprite.accept(
								buttons.get(newButtonIndex).getSprite());
					}
					selectedIndex = newButtonIndex;
				}
			}
		}
	}

	public void buttonClicked(int buttonIndex) {
		elementClicked(buttonIndex + minItemDisplayed);
	}

	/**
	 * Updates the Sprites within the IEList. Call this if the IEList is used to
	 * represent any underlying implementation that has changed.
	 */
	final public void updateSprites() {
		for (int i = 0; i < buttons.size(); i++) {
			Sprite s = buttons.get(i).getSprite();
			getElementSprite(i + minItemDisplayed, s);
		}
		setSelectedIndex(selectedIndex);
	}

	/**
	 * Displays the next element in the list, if any.
	 */
	public void moveDown() {
		int numButtons = buttons.size();
		int maxItemDisplayed = minItemDisplayed + numButtons - 1;

		if (selectedIndex < numButtons - 2 || selectedIndex < numButtons - 1
				&& maxItemDisplayed == getNumElements() - 1) {
			setSelectedIndex(selectedIndex + 1);
		} else if (minItemDisplayed < getNumElements() - buttons.size()) {
			minItemDisplayed++;
			updateSprites();
		}
	}

	public void moveUp() {
		if (selectedIndex > 1 || selectedIndex > 0 && minItemDisplayed == 0) {
			setSelectedIndex(selectedIndex - 1);
		} else if (minItemDisplayed >= 1) {
			minItemDisplayed--;
			updateSprites();
		}
	}

	/**
	 * Updates the IEList buttons to display the correct elements.
	 * 
	 * @param elementIndex
	 *            The index of the element that is to be displayed in this
	 *            IEList button.
	 * @param s
	 *            The current sprite of the IEList button. This should be
	 *            modified to show information for the correct element in its
	 *            unselected state.
	 */
	public abstract void getElementSprite(int elementIndex, Sprite s);

	/**
	 * @return The number of elements within the list being displayed.
	 */
	public abstract int getNumElements();

	public abstract void elementClicked(int elementNumber);

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		buttons.stream().forEach(i -> i.setWorld(world));
	}

	@Override
	public void mousePressed(int button, Point clickPoint) {
		buttons.stream().forEach(i -> i.mousePressed(button, clickPoint));
	}

	@Override
	public void keyPressed(int keycode) {
		if (usingSelection) {
			switch (keycode) {
				case Input.KEY_COMMA:
					moveUp();
					break;
				case Input.KEY_O:
					moveDown();
					break;
				case Input.KEY_ENTER:
				case Input.KEY_SPACE:
					elementClicked(selectedIndex);
					break;
			}
		}
	}

	@Override
	protected void draw() {
		buttons.stream().forEach(s -> s.render());
	}

}
