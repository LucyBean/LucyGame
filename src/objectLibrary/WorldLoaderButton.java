package objectLibrary;

import helpers.Point;
import helpers.Rectangle;

public class WorldLoaderButton extends Button {
	int worldNumber;
	
	public WorldLoaderButton(Point origin, int worldNumber, String text) {
		super(new Rectangle(origin, 400, 32));
		this.worldNumber = worldNumber;
		setText(text);
	}

	@Override
	public void onClick(int button) {
		getWorld().getGame().loadLevel(worldNumber);
	}
}
