package gameInterface;

import helpers.Point;
import helpers.Rectangle;
import options.GlobalOptions;
import options.Option;
import player.Inventory;
import player.InventoryItem;
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
		openMenuButton.setTextCentered("Open menu");
		add(openMenuButton, WorldState.PLAYING);

		Button closeMenuButton = new Button(
				new Rectangle(new Point(300, 0), 100, 32)) {
			@Override
			public void onClick(int button) {
				getWorld().closeMenu();
			}
		};
		closeMenuButton.setTextCentered("Close menu");
		add(closeMenuButton, WorldState.MENU);

		Button clickToStopSelect = new Button(
				new Rectangle(new Point(150, 0), 340, 32)) {
			@Override
			public void onClick(int button) {
				getWorld().stopWatchSelect();
			}
		};
		clickToStopSelect.setTextCentered("Click here to stop selecting");
		add(clickToStopSelect, WorldState.WATCH_SELECT);

//		//
//		// Root menu
//		//
		Menu m = new Menu();
		m.add("Main menu", s -> s.getWorld().getGame().loadMainMenu());
//		MenuButton backToMainMenu = new MenuButton("Main menu") {
//			@Override
//			public void onClick(int button) {
//				getWorld().getGame().loadMainMenu();
//			}
//		};
//		m.add(backToMainMenu);
//
//		MenuButton selectWatchedObject = new MenuButton("Select watch object") {
//			@Override
//			public void onClick(int button) {
//				getWorld().startWatchSelect();
//			}
//		};
//		m.add(selectWatchedObject);
//
//		MenuButton openSubMenu = new MenuButton("Open a sub menu") {
//			@Override
//			public void onClick(int button) {
//				getMenu().setSubMenu(1);
//			}
//		};
//		m.add(openSubMenu);
//
//		MenuButton openOptions = new MenuButton("Options") {
//			@Override
//			public void onClick(int button) {
//				getMenu().setSubMenu(2);
//			}
//		};
//		m.add(openOptions);
//
//		//
//		// A sub menu
//		//
//		MenuButton subMenuButton = new MenuButton("Go back") {
//			@Override
//			public void onClick(int button) {
//				getMenu().setSubMenu(0);
//			}
//		};
//		m.add(subMenuButton, 1);
//
//		//
//		// Options
//		//
//		for (Option o : Option.values()) {
//			MenuButton mb = new OptionButton(o);
//			m.add(mb, 2);
//		}
//		MenuButton storeSettings = new MenuButton("Store settings") {
//			@Override
//			public void onClick(int button) {
//				GlobalOptions.saveToFile();
//			}
//		};
//		m.add(storeSettings, 2);
//
		add(m, WorldState.MENU);
		
		Inventory i = new Inventory();
		i.add(InventoryItem.getGem(), 4);
		i.add(InventoryItem.getKeyByID(2), 30);
		i.add(InventoryItem.getKeyByID(3), 100);
		i.add(InventoryItem.getKeyByID(1), 23);
		i.add(InventoryItem.getKeyByID(-1), 2);
		i.add(InventoryItem.getKeyByID(4), 1);
		InventoryDisplay id = new InventoryDisplay(new Point(400, 40), i.getItems());
		add(id, WorldState.INVENTORY);
	}
}
