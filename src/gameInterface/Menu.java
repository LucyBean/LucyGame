package gameInterface;

import java.util.HashMap;
import java.util.Map;

import helpers.Dir;
import helpers.Point;
import objects.InterfaceElement;
import options.GlobalOptions;
import worlds.World;

public class Menu extends InterfaceElement {
	private ObjectLayerSet<MenuButton> menuButtons;
	private Map<Integer, Point> nextPositions;
	private static final Point DEFAULT_POINT = new Point(
			(GlobalOptions.WINDOW_WIDTH - MenuButton.WIDTH) / 2, 100);
	private int currentActive;

	public Menu() {
		menuButtons = new ObjectLayerSet<MenuButton>();
		nextPositions = new HashMap<Integer, Point>();
		reset();
	}

	/**
	 * Resets the menu back to its original state (i.e. closes all sub menus)
	 */
	public void reset() {
		currentActive = 0;
	}

	@Override
	public void onClick(int button) {

	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		menuButtons.applyToAllObjects(mb -> mb.setWorld(world));
	}

	@Override
	public void mousePressed(int button, Point clickPoint) {
		menuButtons.applyToLayerObjects(a -> a.mousePressed(button, clickPoint),
				currentActive);
	}

	/**
	 * Adds the MenuButton to the required Menu sub-menu.
	 * 
	 * @param mb
	 *            The MenuButton to add.
	 * @param state
	 *            The sub-menu number to which the MenuButton should be added. 0
	 *            is the default root menu.
	 */
	public void add(MenuButton mb, int state) {
		menuButtons.add(mb, state);

		// Get the next position
		nextPositions.putIfAbsent(state, DEFAULT_POINT.copy());
		Point nextPosition = nextPositions.get(state);

		mb.setPosition(nextPosition);
		mb.setWorld(getWorld());
		mb.setMenu(this);

		// Set the position of the next button
		nextPositions.put(state, nextPosition.move(Dir.SOUTH, 40));
	}

	/**
	 * Adds the MenuButton to the Menu's root menu.
	 * 
	 * @param mb
	 *            The MenuButton to add.
	 */
	public void add(MenuButton mb) {
		add(mb, 0);
	}

	@Override
	protected void draw() {
		menuButtons.render(currentActive);
	}

	/**
	 * Changes the state of the Menu to display different sub-menus.
	 * 
	 * @param index
	 *            The index of the sub-menu to display.
	 */
	public void setSubMenu(int index) {
		currentActive = index;
	}
}
