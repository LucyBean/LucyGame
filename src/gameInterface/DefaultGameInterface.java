package gameInterface;

import java.util.Collection;

import exporting.WorldMapImporterExporter;
import helpers.Point;
import helpers.Rectangle;
import objects.WorldObject;
import options.GlobalOptions;
import options.Option;
import worlds.WorldMap;
import worlds.WorldState;

public class DefaultGameInterface extends GameInterface {
	public DefaultGameInterface() {
		Button openMenuButton = new Button(
				new Rectangle(new Point(200, 0), 100, 32)) {
			@Override
			public void onClick(int button, Point clickPoint) {
				getWorld().openMenu();
			}
		};
		openMenuButton.setTextCentered("Open menu");
		add(openMenuButton, WorldState.PLAYING);

		Button closeMenuButton = new Button(
				new Rectangle(new Point(300, 0), 100, 32)) {
			@Override
			public void onClick(int button, Point clickPoint) {
				getWorld().closeMenu();
			}
		};
		closeMenuButton.setTextCentered("Close menu");
		add(closeMenuButton, WorldState.MENU);

		Button clickToStopSelect = new Button(
				new Rectangle(new Point(150, 0), 340, 32)) {
			@Override
			public void onClick(int button, Point clickPoint) {
				getWorld().stopWatchSelect();
			}
		};
		clickToStopSelect.setTextCentered("Click here to stop selecting");
		add(clickToStopSelect, WorldState.WATCH_SELECT);

		Button play = new Button(new Rectangle(Point.ZERO, 100, 32)) {
			@Override
			public void onClick(int button, Point clickPoint) {
				getWorld().stopBuilding();
			}
		};
		play.setTextCentered("Play!");
		add(play, WorldState.BUILDING);

		Button export = new Button(new Rectangle(new Point(104, 0), 100, 32)) {
			@Override
			public void onClick(int button, Point clickPoint) {
				WorldMap wm = getWorld().getMap();
				WorldMapImporterExporter.export(wm, "test");
			}
		};
		export.setTextCentered("Export map");
		add(export, WorldState.BUILDING);

		Button importer = new Button(
				new Rectangle(new Point(208, 0), 100, 32)) {
			@Override
			public void onClick(int button, Point clickPoint) {
				Collection<WorldObject> objects = WorldMapImporterExporter.importObjects(
						"test");
				getWorld().getMap().reset();
				getWorld().getMap().addObjects(objects);
			}
		};
		add(importer, WorldState.BUILDING);

		//
		// Root menu
		//
		MenuSet m = new MenuSet();
		m.add(() -> "Main menu", s -> s.getWorld().getGame().loadMainMenu());
		m.add(() -> "Select watch object",
				s -> s.getWorld().startWatchSelect());
		m.add(() -> "Open a sub menu", s -> s.getMenuSet().setSubMenu(1));
		m.add(() -> "Open options", s -> s.getMenuSet().setSubMenu(2));
		m.add(() -> "Build!", s -> s.getWorld().startBuilding());
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
