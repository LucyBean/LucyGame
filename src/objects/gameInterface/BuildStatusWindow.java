package objects.gameInterface;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Point;
import helpers.Rectangle;
import objects.CoOrdTranslator;
import objects.world.ItemType;
import objects.world.WorldObject;
import worlds.WorldLayer;

public class BuildStatusWindow extends InterfaceElement {
	private static CoOrdTranslator worldCOT;
	private static Point textTopLeft = new Point(5,5);

	public BuildStatusWindow(Point origin) {
		super(new Rectangle(origin, 200, 100));
		setBackground(3);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		if (worldCOT == null) {
			WorldObject wo = new WorldObject(Point.ZERO, WorldLayer.WORLD, ItemType.NONE) {
				@Override
				protected void resetState() {
					// TODO Auto-generated method stub

				}
			};
			getWorld().addObject(wo);
			worldCOT = wo.getCoOrdTranslator();
		}
		Input input = gc.getInput();
		Point mouseScreen = new Point(input.getMouseX(), input.getMouseY());
		Point mouseWorld = worldCOT.screenToWorldCoOrds(mouseScreen);
		WorldObject hoverObject = getWorld().getMap().findObjectScreen(mouseScreen);
		
		String text = mouseWorld.toString();
		if (hoverObject != null) {
			text += "\n" + hoverObject.toString();
		}
		setText(text, textTopLeft);
	}
}
