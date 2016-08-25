package gameInterface;

import java.util.ArrayList;
import java.util.List;
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
		buttons = new ArrayList<IEListItem>();
		minItemDisplayed = 0;

		for (int i = 0; i < numDisplayElems; i++) {
			Sprite s = spriteMaker.get();
			IEListItem ieli = new IEListItem(this, i, nextPosition, s);
			getElementSprite(i, s);
			buttons.add(ieli);
			nextPosition = nextPosition.move(Dir.SOUTH,
					s.getBoundingRectangle().getHeight());
		}
	}

	final public void buttonClicked(int buttonIndex) {
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
	}
	
	/**
	 * Displays the next element in the list, if any.
	 */
	public void moveDown() {
		minItemDisplayed++;
		int numButtons = buttons.size();
		
		Sprite first = buttons.get(0).getSprite();
		
		// Shift sprites up
		for (int i = 0; i < numButtons - 1; i++) {
			Sprite next = buttons.get(i+1).getSprite();
			buttons.get(i).setSprite(next);
		}
		
		// Set first sprite to last position and update it
		buttons.get(numButtons-1).setSprite(first);
		getElementSprite(numButtons - 1 + minItemDisplayed, first);
	}
	
	public void moveUp() {
		minItemDisplayed--;
		int numButtons = buttons.size();
		
		Sprite last = buttons.get(numButtons - 1).getSprite();
		
		// Shift sprites down
		for (int i = numButtons - 1; i >= 1; i--) {
			Sprite prev = buttons.get(i-1).getSprite();
			buttons.get(i).setSprite(prev);
		}
		
		// Set the last sprite to first position and update it
		buttons.get(0).setSprite(last);
		getElementSprite(minItemDisplayed, last);
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
		switch (keycode) {
			case Input.KEY_COMMA:
				moveUp();
				break;
			case Input.KEY_O:
				moveDown();
				break;
		}
	}

	@Override
	protected void draw() {
		buttons.stream().forEach(s -> s.render());
	}
	
	
}
