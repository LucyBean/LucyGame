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
				// TODO: Fix this
				// The trampoline reflects people
				// This is an approximate value for this gravity.
				// Magic, magic numbers.
				// The 'magic jump' strength
				final float targetJumpStrength = 0.03441f;
				// The fall speed for this 'magic jump'
				final float targetVSpeed = 0.02645f;

				float vSpeed = p.getVSpeed();
				float delta = (1 - targetVSpeed + vSpeed);
				float power = 10;
				float ratio = (float) Math.pow(power, Math.pow(delta, power))/power;
				float jumpStrength = ratio * targetJumpStrength;
				
				p.signalJumpAbs(jumpStrength);
			}
		}
	}

}
