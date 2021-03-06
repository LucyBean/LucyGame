package objects.gameInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import helpers.Pair;
import helpers.Point;
import objects.images.Sprite;
import objects.images.SpriteBuilder;
import options.GlobalOptions;
import worlds.World;

public class MenuSet extends InterfaceElement {
	private Map<Integer, Menu> menus;
	private Point startPoint;
	private int currentActive;
	private boolean useSelection = true;

	public MenuSet() {
		this(new Point((GlobalOptions.WINDOW_WIDTH - 360) / 2, 100), true);
	}

	public MenuSet(Point startPoint, boolean useSelection) {
		super(Point.ZERO);
		menus = new HashMap<Integer, Menu>();
		this.useSelection = useSelection;
		this.startPoint = startPoint;
		reset();
	}

	/**
	 * Resets the menu back to its original state (i.e. closes all sub menus)
	 */
	public void reset() {
		currentActive = 0;
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

	@Override
	public void keyPressed(int keycode) {
		IEList currentMenu = menus.get(currentActive);
		if (currentMenu != null) {
			currentMenu.keyPressed(keycode);
		}
	}

	/**
	 * Adds the MenuButton to the required Menu sub-menu.
	 * 
	 * @param mb
	 *            The MenuButton to add.
	 * 
	 */
	/**
	 * Adds this entry to the bottom of the required MenuSet sub-Menu.
	 * 
	 * @param text
	 *            A function that produces the text for the button.
	 * @param clickAction
	 *            The function that is run when the button is clicked.
	 * @param state
	 *            The sub-menu number to which the MenuButton should be added. 0
	 *            is the default root menu.
	 */
	public void add(Supplier<String> text, Consumer<Menu> clickAction, Consumer<Pair<Menu, String>> receiveInput,
			int state) {
		Menu subMenu = menus.get(state);
		if (subMenu == null) {
			subMenu = new Menu(startPoint, 10, this, useSelection) {
				@Override
				protected Sprite makeNewSprite() {
					return SpriteBuilder.makeMenuButton("");
				}
			};

			menus.put(state, subMenu);
		}

		subMenu.add(text, clickAction, receiveInput);
	}
	
	public void add(Supplier<String> text, Consumer<Menu> clickAction, int state) {
		this.add(text, clickAction, s -> {}, state);
	}

	public void add(Supplier<String> text, Consumer<Menu> clickAction) {
		add(text, clickAction, 0);
	}

	@Override
	protected void draw() {
		Menu currentMenu = menus.get(currentActive);
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
