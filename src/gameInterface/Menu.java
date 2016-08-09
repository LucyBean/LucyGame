package gameInterface;

import java.util.HashMap;
import java.util.Map;

import helpers.Dir;
import helpers.Point;
import objects.InterfaceElement;
import worlds.GlobalOptions;
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

	public void add(MenuButton mb, int state) {
		menuButtons.add(mb, state);
		
		// Get the next position
		nextPositions.putIfAbsent(state, DEFAULT_POINT.copy());
		Point nextPosition = nextPositions.get(state);
		
		mb.setPosition(nextPosition);
		mb.setWorld(getWorld());
		mb.setMenu(this);
		
		// Set the position of the next button
		nextPositions.put(state, nextPosition.move(Dir.SOUTH, 32));
	}

	@Override
	protected void draw() {
		menuButtons.render(currentActive);
	}
	
	public void setState(int state) {
		currentActive = state;
	}
}
