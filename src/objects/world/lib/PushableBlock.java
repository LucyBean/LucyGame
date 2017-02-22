package objects.world.lib;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.attachments.ActorSticker;
import objects.attachments.InteractBox;
import objects.world.Actor;
import objects.world.ItemType;
import worlds.WorldLayer;

public class PushableBlock extends Actor {

	public PushableBlock(Point origin) {
		super(origin, WorldLayer.WORLD, ItemType.PUSHABLE_BLOCK);
		setColliderFromSprite();
		getCollider().get().setSolid(true);
		setPushable(true);
		
		InteractBox ib = new InteractBox(new Point(-0.3f, 0), 2.6f, 2);
		attach(ib);
		
		ActorSticker as = new ActorSticker(new Point(0, -0.2f), 2, 0.2f);
		attach(as);
	}

	@Override
	protected void resetActorState() {
		// TODO Auto-generated method stub

	}

	@Override
	public void act(GameContainer gc, int delta) {
		
	}
	
	@Override
	public void interactedBy(Actor a) {
		super.interactedBy(a);
	}

}
