package objects.gameInterface;

import java.util.Optional;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Point;
import helpers.Rectangle;
import objects.world.WorldObject;

public class BuildStatusWindow extends InterfaceElement {
	private static Point textTopLeft = new Point(5,5);

	public BuildStatusWindow(Point origin) {
		super(new Rectangle(origin, 200, 100));
		setBackground(3);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		Input input = gc.getInput();
		Point mouseScreen = new Point(input.getMouseX(), input.getMouseY());
		Point mouseWorld = getWorld().screenToWorldCoOrds(mouseScreen);
		Optional<WorldObject> hoverObject = getWorld().getMap().findObjectScreen(mouseScreen);
		
		String text = "MOUSE: " + mouseWorld.toString() + "\n";
		if (hoverObject.isPresent()) {
			text += hoverObject.get().getInfo();
		}
		setText(text, textTopLeft);
	}
}
