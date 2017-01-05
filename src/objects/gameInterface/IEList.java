package objects.gameInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import objects.images.LayeredImage;
import objects.images.SingleSprite;
import objects.images.Sprite;
import worlds.Controller;
import worlds.World;

public abstract class IEList extends InterfaceElement {
	private Point nextPosition;
	private List<IEListItem> buttons;
	private int minItemDisplayed;
	private Point selectedPoint;
	private Consumer<Sprite> setSelectedSprite;
	private boolean usingSelection;
	private int width;
	private int height;
	private int padding;

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
	 * @param height
	 *            The number of buttons to display up/down.
	 * @param width
	 *            The number of buttons to display across.
	 * @param spriteMaker
	 *            A Supplier that will produce a blank Sprite for each IEList
	 *            element.
	 * @param setSelectedSprite
	 *            A Consumer that takes a Sprite and applies some effect to
	 *            display that the corresponding button has been selected.
	 */
	public IEList(Point firstPoint, int height, int width, int padding,
			Consumer<Sprite> setSelectedSprite) {
		nextPosition = firstPoint;
		buttons = new ArrayList<IEListItem>();
		minItemDisplayed = 0;
		this.setSelectedSprite = setSelectedSprite;
		this.width = width;
		this.height = height;
		this.padding = padding;
		int numDisplayElems = height * width;
		usingSelection = (setSelectedSprite != null);
		selectedPoint = Point.ZERO;

		setPosition(firstPoint);

		int column = 0;
		for (int i = 0; i < numDisplayElems; i++) {
			Sprite s = makeNewSprite();
			Rectangle r = s.getRectangle();
			IEListItem ieli = new IEListItem(this, i, nextPosition, s);
			s.setObject(ieli);
			getElementSprite(i, s);
			buttons.add(ieli);

			column++;
			if (column >= width) {
				nextPosition = new Point(firstPoint.getX(),
						nextPosition.getY() + r.getHeight() + padding);
				column = 0;
			} else {
				nextPosition = nextPosition.move(Dir.EAST,
						r.getWidth() + padding);
			}
		}

		setSelectedButton(Point.ZERO);
	}

	/**
	 * Creates a new IEList which does not have the ability to select an
	 * element.
	 */
	public IEList(Point firstPoint, int height, int width, int padding) {
		this(firstPoint, height, width, padding, null);
	}

	@Override
	public void setPosition(Point position) {
		if (buttons != null && getPosition() != null) {
			// move all elements
			Point offset = position.move(getPosition().neg());
			buttons.stream().forEach(
					i -> i.setPosition(i.getPosition().move(offset)));
		}
		super.setPosition(position);
	}

	/**
	 * Sets the background to be the given sprite.
	 * 
	 * @param s
	 */
	public void setBackground(Sprite s) {
		setSprite(s);
	}

	/**
	 * Sets the background to be the given colour
	 * 
	 * @param c
	 */
	public void setBackground(Color c) {
		if (buttons.size() > 0) {
			if (getSprite() == null) {
				int w = getWidthPixels() + padding * 2;
				int h = getHeightPixels() + padding * 2;
				Point origin = new Point(-padding, -padding);

				LayeredImage limg = new LayeredImage(w, h, 1);
				setSprite(new SingleSprite(limg, origin, 1));
			}

			getSprite().getImage().fillLayer(0, c);
		}
	}

	/**
	 * Checks whether an element exists at this index.
	 * 
	 * @param elementIndex
	 * @return
	 */
	protected boolean elementExists(int elementIndex) {
		return elementIndex >= 0 && elementIndex < getNumElements();
	}

	private int buttonPointToIndex(Point buttonPoint) {
		int col = (int) buttonPoint.getX();
		int row = (int) buttonPoint.getY();

		if (col >= 0 && col < width && row >= 0 && row < height) {
			return row * width + col;
		} else {
			return -1;
		}
	}

	private void setSelectedButton(Point newButtonPoint) {
		if (usingSelection) {
			// check if the item to select exists
			int newButtonIndex = buttonPointToIndex(newButtonPoint);
			int newElementIndex = newButtonIndex + minItemDisplayed;
			if (elementExists(newElementIndex)) {
				if (usingSelection) {
					// deselect old index
					if (selectedPoint != null) {
						int oldButtonIndex = buttonPointToIndex(selectedPoint);
						int oldElementIndex = oldButtonIndex + minItemDisplayed;
						if (oldButtonIndex >= 0
								&& oldButtonIndex < buttons.size()) {
							getElementSprite(oldElementIndex,
									buttons.get(oldButtonIndex).getSprite());
						}
					}

					// select new index
					if (newButtonIndex >= 0
							&& newButtonIndex < buttons.size()) {
						setSelectedSprite.accept(
								buttons.get(newButtonIndex).getSprite());
					}
					selectedPoint = newButtonPoint;
				}
			}
		}
	}

	public void buttonClicked(int buttonIndex, Point clickPoint) {
		int elementIndex = buttonIndex + minItemDisplayed;
		if (elementExists(elementIndex)) {
			elementClicked(buttonIndex + minItemDisplayed);
		}
	}

	protected int getMinItemDisplayed() {
		return minItemDisplayed;
	}

	protected IEListItem getButton(int buttonIndex) {
		if (buttons != null && buttonIndex >= 0
				&& buttonIndex < buttons.size()) {
			return buttons.get(buttonIndex);
		} else {
			return null;
		}
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
		setSelectedButton(selectedPoint);
	}

	/**
	 * Displays the next element in the list, if any.
	 */
	public void moveDown() {
		int maxItemDisplayed = minItemDisplayed + height * width;
		int numElems = getNumElements();
		int selY = (int) selectedPoint.getY();

		if (selY < height - 2
				|| selY < height - 1 && maxItemDisplayed == numElems) {
			// Move the selected button down by one
			setSelectedButton(selectedPoint.move(Dir.SOUTH, 1));
		} else if (minItemDisplayed < numElems - buttons.size()) {
			// Scroll through the sprites
			minItemDisplayed += width;
			updateSprites();
		}
	}

	public void moveUp() {
		if (selectedPoint.getY() > 1
				|| selectedPoint.getY() >= 0 && minItemDisplayed == 0) {
			setSelectedButton(selectedPoint.move(Dir.NORTH, 1));
		} else if (minItemDisplayed >= 1) {
			minItemDisplayed -= width;
			updateSprites();
		}
	}

	public void moveLeft() {
		int selX = (int) selectedPoint.getX();

		if (selX > 0) {
			setSelectedButton(selectedPoint.move(Dir.WEST, 1));
			updateSprites();
		}
	}

	public void moveRight() {
		int selX = (int) selectedPoint.getX();

		if (selX < width - 1) {
			setSelectedButton(selectedPoint.move(Dir.EAST, 1));
			updateSprites();
		}
	}

	/**
	 * Updates the IEList buttons to display the correct elements.
	 * 
	 * @param elementIndex
	 *            The index of the element that is to be displayed in this
	 *            IEList button.
	 * @param sprite
	 *            The current sprite of the IEList button. This should be
	 *            modified to show information for the correct element in its
	 *            unselected state.
	 */
	protected abstract void getElementSprite(int elementIndex, Sprite sprite);

	/**
	 * @return The number of elements within the list being displayed.
	 */
	protected abstract int getNumElements();

	protected abstract void elementClicked(int elementNumber);

	protected abstract Sprite makeNewSprite();

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
			if (keycode == Controller.UP)
				moveUp();
			if (keycode == Controller.DOWN)
				moveDown();
			if (keycode == Controller.LEFT)
				moveLeft();
			if (keycode == Controller.RIGHT)
				moveRight();
			if (keycode == Input.KEY_ENTER || keycode == Input.KEY_SPACE)
				elementClicked(buttonPointToIndex(selectedPoint));

		}
	}

	@Override
	protected void draw() {
		if (getSprite() != null) {
			getSprite().draw();
		}
		buttons.stream().forEach(s -> s.render());
	}

	/**
	 *
	 * @return The width of the elemnt in pixels
	 */
	@Override
	public int getWidthPixels() {
		if (getSprite() != null) {
			return (int) getSprite().getRectangle().getWidth();
		} else if (buttons != null && !buttons.isEmpty()) {
			Rectangle spriteBound = buttons.get(0).getSprite().getRectangle();
			return (int) (spriteBound.getWidth() * width
					+ padding * (width - 1));
		}

		return 0;
	}

	/**
	 * @return The height of the element in pixels
	 */
	@Override
	public int getHeightPixels() {
		if (getSprite() != null) {
			return (int) getSprite().getRectangle().getHeight();
		} else if (buttons != null && !buttons.isEmpty()) {
			Rectangle spriteBound = buttons.get(0).getSprite().getRectangle();
			return (int) (spriteBound.getHeight() * height
					+ padding * (height - 1));
		}

		return 0;
	}

	@Override
	public void update(GameContainer gc, int delta) {
		buttons.stream().forEach(b -> b.update(gc, delta));
	}
}
