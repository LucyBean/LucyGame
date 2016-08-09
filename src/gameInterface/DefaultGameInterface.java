package gameInterface;

import helpers.Point;
import helpers.Rectangle;
import objectLibrary.Button;
import worlds.WorldState;

public class DefaultGameInterface extends GameInterface {
	public DefaultGameInterface() {
		Button openMenuButton = new Button(
				new Rectangle(new Point(200, 0), 100, 32)) {
			@Override
			public void onClick(int button) {
				getWorld().openMenu();
			}
		};
		openMenuButton.setText("Open menu");
		add(openMenuButton, WorldState.PLAYING);

		Button closeMenuButton = new Button(
				new Rectangle(new Point(300, 0), 100, 32)) {
			@Override
			public void onClick(int button) {
				getWorld().closeMenu();
			}
		};
		closeMenuButton.setText("Close menu");
		add(closeMenuButton, WorldState.MENU);

		Button clickToStopSelect = new Button(
				new Rectangle(new Point(150, 0), 340, 32)) {
			@Override
			public void onClick(int button) {
				getWorld().stopWatchSelect();
			}
		};
		clickToStopSelect.setText("Click here to stop selecting");
		add(clickToStopSelect, WorldState.WATCH_SELECT);

		Menu m = new Menu();
		MenuButton backToMainMenu = new MenuButton("Main menu") {
			@Override
			public void onClick(int button) {
				getWorld().getGame().loadMainMenu();
			}
		};
		m.add(backToMainMenu, 0);
		
		MenuButton selectWatchedObject = new MenuButton("Select watch object") {
			@Override
			public void onClick(int button) {
				getWorld().startWatchSelect();
			}
		};
		m.add(selectWatchedObject, 0);
		
		MenuButton openSubMenu = new MenuButton("Open a sub menu") {
			@Override
			public void onClick(int button) {
				getMenu().setState(1);
			}
		};
		m.add(openSubMenu, 0);
		
		MenuButton subMenuButton = new MenuButton("Go back") {
			@Override
			public void onClick(int button) {
				getMenu().setState(0);
			}
		};
		m.add(subMenuButton, 1);

		add(m, WorldState.MENU);
	}
}