package objectLibrary;

import gameInterface.Button;
import helpers.Point;
import objectLibs.ImageBuilder;

public class WorldLoaderButton extends Button {
	int worldNumber;
	
	public WorldLoaderButton(Point origin, int worldNumber, String text) {
		super(origin, ImageBuilder.getWorldButtonBackground());
		this.worldNumber = worldNumber;
		setTextCentered(text);
	}

	@Override
	public void onClick(int button) {
		getWorld().getGame().loadLevel(worldNumber);
	}
}
