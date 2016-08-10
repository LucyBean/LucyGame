package gameInterface;

import helpers.Point;
import helpers.Rectangle;
import objectLibrary.Button;
import options.Option;
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

		//
		// Root menu
		//
		Menu m = new Menu();
		MenuButton backToMainMenu = new MenuButton("Main menu") {
			@Override
			public void onClick(int button) {
				getWorld().getGame().loadMainMenu();
			}
		};
		m.add(backToMainMenu);
		
		MenuButton selectWatchedObject = new MenuButton("Select watch object") {
			@Override
			public void onClick(int button) {
				getWorld().startWatchSelect();
			}
		};
		m.add(selectWatchedObject);
		
		MenuButton openSubMenu = new MenuButton("Open a sub menu") {
			@Override
			public void onClick(int button) {
				getMenu().setSubMenu(1);
			}
		};
		m.add(openSubMenu);
		
		MenuButton openOptions = new MenuButton("Options") {
			@Override
			public void onClick(int button) {
				getMenu().setSubMenu(2);
			}
		};
		m.add(openOptions);
		
		//
		// A sub menu
		//
		MenuButton subMenuButton = new MenuButton("Go back") {
			@Override
			public void onClick(int button) {
				getMenu().setSubMenu(0);
			}
		};
		m.add(subMenuButton, 1);
		
		//
		// Options
		//
		for (Option o : Option.values()) {
			MenuButton mb = new OptionButton(o);
			m.add(mb,2);
		}
		add(m, WorldState.MENU);
	}
}