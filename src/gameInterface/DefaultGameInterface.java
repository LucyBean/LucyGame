package gameInterface;

import helpers.Point;
import helpers.Rectangle;
import options.GlobalOptions;
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

		//
		// Root menu
		//
		MenuSet m = new MenuSet();
		m.add(() -> "Main menu", s -> s.getWorld().getGame().loadMainMenu());
		m.add(() -> "Select watch object",
				s -> s.getWorld().startWatchSelect());
		m.add(() -> "Open a sub menu", s -> s.getMenuSet().setSubMenu(1));
		m.add(() -> "Open options", s -> s.getMenuSet().setSubMenu(2));
		//
		// A sub menu
		//
		m.add(() -> "Go back", s -> s.getMenuSet().setSubMenu(0), 1);
		//
		// Options
		//
		for (Option o : Option.values()) {
			m.add(() -> o.toString(), s -> {
				o.setToNextValue();
				s.updateSprites();
			}, 2);
		}
		m.add(() -> "Store settings", s -> GlobalOptions.saveToFile(), 2);

		add(m, WorldState.MENU);
	}
}
