package gameInterface;

import java.util.HashMap;
import java.util.Map;

import helpers.Point;
import options.GlobalOptions;
import worlds.World;

public class Menu extends InterfaceElement {
	private Map<Integer, IEList<MenuButton>> menus;
	private static final Point START_POINT = new Point(
			(GlobalOptions.WINDOW_WIDTH - 360) / 2, 100);
	private int currentActive;

	public Menu() {
		menus = new HashMap<Integer, IEList<MenuButton>>();
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
		menus.values().stream().forEach(li -> li.setWorld(world));
	}

	@Override
	public void mousePressed(int button, Point clickPoint) {
		IEList<MenuButton> currentMenu = menus.get(currentActive);
		if (currentMenu != null) {
			currentMenu.mousePressed(button, clickPoint);
		}
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
		IEList<MenuButton> subMenu = menus.get(state);
		if (subMenu == null) {
			subMenu = new IEList<MenuButton>(START_POINT);
			menus.put(state, subMenu);
		}
		subMenu.add(mb);
		mb.setMenu(this);
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
		IEList<MenuButton> currentMenu = menus.get(currentActive);
		if (currentMenu != null) {
			currentMenu.render();
		}
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
