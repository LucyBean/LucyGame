package objectLibrary;

import java.util.Iterator;

import helpers.Dir;
import helpers.Point;
import objects.InterfaceElement;
import worlds.GlobalOptions;
import worlds.ObjectLayer;
import worlds.World;

public class Menu extends InterfaceElement {
	private ObjectLayer<MenuButton> menuButtons;
	private Point nextPosition = new Point(
			(GlobalOptions.WINDOW_WIDTH - MenuButton.WIDTH) / 2, 100);

	public Menu() {
		super(null, null);
		menuButtons = new ObjectLayer<MenuButton>();
	}

	@Override
	public void onClick(int button) {
		
	}
	
	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		Iterator<MenuButton> mbi = menuButtons.iterator();
		while (mbi.hasNext()) {
			MenuButton mb = mbi.next();
			mb.setWorld(world);
		}
	}
	
	@Override
	public void mousePressed(int button, Point clickPoint) {
		// The mousePressed signal must be propagated to all the menu buttons
		Iterator<MenuButton> mbi = menuButtons.iterator();
		while (mbi.hasNext()) {
			MenuButton mb = mbi.next();
			mb.mousePressed(button, clickPoint);
		}
	}

	public void add(MenuButton mb) {
		menuButtons.add(mb);
		mb.setPosition(nextPosition);
		mb.setWorld(getWorld());
		nextPosition = nextPosition.move(Dir.SOUTH, 32);
	}

	@Override
	protected void draw() {
		menuButtons.render();
	}
}
