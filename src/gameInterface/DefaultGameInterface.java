package gameInterface;

import java.util.Collection;

import exporting.WorldMapImporterExporter;
import helpers.Point;
import helpers.Rectangle;
import objects.WorldObject;
import options.GlobalOptions;
import options.Option;
import worlds.World;
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
				new Rectangle(new Point(200, 0), 100, 32)) {
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

		Button toolsButton = new Button(
				new Rectangle(new Point(200, 0), 100, 32)) {
			@Override
			public void onClick(int button, Point clickPoint) {
				World w = getWorld();
				if (w.getState() == WorldState.BUILDING) {
					w.openBuildMenu();
				} else if (w.getState() == WorldState.BUILDING_MENU) {
					w.closeBuildMenu();
				}
			}
		};
		toolsButton.setTextCentered("Tools");
		add(toolsButton, WorldState.BUILDING);
		add(toolsButton, WorldState.BUILDING_MENU);

		MenuSet buildingTools = new MenuSet();
		buildingTools.add(() -> "Export map", s -> {
			WorldMapImporterExporter.export(s.getWorld().getMap(), "test");
			s.getWorld().closeBuildMenu();
		});
		buildingTools.add(() -> "Import map", s -> {
			Collection<WorldObject> objects = WorldMapImporterExporter.importObjects(
					"test");
			if (objects != null) {
				s.getWorld().getMap().reset();
				s.getWorld().getMap().addObjects(objects);
				s.getWorld().closeBuildMenu();
			}
		});
		add(buildingTools, WorldState.BUILDING_MENU);

		//
		// Root menu
		//
		MenuSet gameMenu = new MenuSet();
		gameMenu.add(() -> "Main menu",
				s -> s.getWorld().getGame().loadMainMenu());
		gameMenu.add(() -> "Select watch object",
				s -> s.getWorld().startWatchSelect());
		gameMenu.add(() -> "Open a sub menu",
				s -> s.getMenuSet().setSubMenu(1));
		gameMenu.add(() -> "Open options", s -> s.getMenuSet().setSubMenu(2));
		gameMenu.add(() -> "Build!", s -> s.getWorld().startBuilding());
		//
		// A sub menu
		//
		gameMenu.add(() -> "Go back", s -> s.getMenuSet().setSubMenu(0), 1);
		//
		// Options
		//
		for (Option o : Option.values()) {
			gameMenu.add(() -> o.toString(), s -> {
				o.setToNextValue();
				s.updateSprites();
			}, 2);
		}
		gameMenu.add(() -> "Store settings", s -> GlobalOptions.saveToFile(),
				2);

		add(gameMenu, WorldState.MENU);
	}
}
