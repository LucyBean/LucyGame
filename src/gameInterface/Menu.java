package gameInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import helpers.Pair;
import helpers.Point;
import images.Sprite;
import images.SpriteBuilder;
import options.GlobalOptions;
import worlds.World;

public class Menu extends InterfaceElement {
	private Map<Integer, IEList> menus;
	private static final Point START_POINT = new Point(
			(GlobalOptions.WINDOW_WIDTH - 360) / 2, 100);
	private int currentActive;
	// TODO: Move to sub menu class
	private Map<Integer, List<Consumer<Menu>>> clickActions;
	private Map<Integer, List<String>> labels;

	public Menu() {
		menus = new HashMap<Integer, IEList>();
		clickActions = new HashMap<Integer, List<Consumer<Menu>>>();
		labels = new HashMap<Integer, List<String>>();
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
		IEList currentMenu = menus.get(currentActive);
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
	public void add(String text, Consumer<Menu> clickAction, int state) {
		IEList subMenu = menus.get(state);
		if (subMenu == null) {
			subMenu = new IEList(START_POINT, 10,
					(() -> SpriteBuilder.makeMenuButton("hello"))) {
				@Override
				public void elementClicked(int elementIndex) {
					// TODO Auto-generated method stub

				}

				@Override
				public void getElementSprite(int elementIndex, Sprite s) {
					// TODO Auto-generated method stub
					
				}

			};
			menus.put(state, subMenu);
		}

		// TODO: Add the menu choice and action to the element list
		labels.putIfAbsent(state, new ArrayList<String>());
		labels.get(state).add(text);
		
		clickActions.putIfAbsent(state, new ArrayList<Consumer<Menu>>());
		clickActions.get(state).add(clickAction);
	}
	
	public void add(String text, Consumer<Menu> clickAction) {
		add(text, clickAction, 0);
	}

	@Override
	protected void draw() {
		IEList currentMenu = menus.get(currentActive);
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
