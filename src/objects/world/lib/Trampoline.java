package objects.world.lib;

import helpers.Point;
import objects.world.ActorState;
import objects.world.ItemType;
import objects.world.Static;
import objects.world.WorldObject;
import objects.world.characters.Player;
import worlds.WorldLayer;

public class Trampoline extends Static {

	public Trampoline(Point origin) {
		super(origin, WorldLayer.WORLD, ItemType.TRAMPOLINE);
		setInteractBoxFromSprite();
	}

	@Override
	protected void resetStaticState() {
		
	}
	
	@Override
	public void overlapStart(WorldObject wo) {
		if (wo instanceof Player) {
			Player p = (Player) wo;
			if (p.getState() == ActorState.FALL) {
				p.resetMidAirJump();
				float jumpSpeed = Math.min(2.6f, 75.0f * p.getVSpeed());
				p.signalJump(jumpSpeed);
			}
		}
	}

}
