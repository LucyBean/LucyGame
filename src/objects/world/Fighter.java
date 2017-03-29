package objects.world;

import java.util.Optional;

import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;
import objects.attachments.AttackBox;
import objects.attachments.Collider;
import objects.attachments.InteractBox;
import objects.images.Sprite;
import worlds.WorldLayer;

public abstract class Fighter extends Actor {
	private AttackBox highFrontAttack;
	private boolean kickNextFrame;

	public Fighter(Point origin, WorldLayer layer, ItemType itemType,
			Sprite sprite, Optional<Collider> collider, Optional<InteractBox> interactBox) {
		super(origin, layer, itemType, sprite, collider, interactBox);

		if (getCollider().isPresent()) {
			Collider c = getCollider().get();
			highFrontAttack = new AttackBox(
					c.getTopRight().move(Dir.NORTH, c.getHeight() * 0.2f),
					c.getWidth() * 1.4f, c.getHeight() * 0.7f);
			highFrontAttack.affectsNPCs(true);
		}
	}

	@Override
	public final void act(GameContainer gc, int delta) {
		fighterAct(gc, delta);
		if (kickNextFrame) {
			kick();
			kickNextFrame = false;
		}
	}

	public abstract void fighterAct(GameContainer gc, int delta);

	public void signalKick() {
		kickNextFrame = true;
	}

	private void kick() {
		assert getCollider().isPresent();
		if (getState() != ActorState.KICK_FRONT) {
			setState(ActorState.KICK_FRONT);
		}
	}

	@Override
	public void stateChanged(ActorState from, ActorState to) {
		super.stateChanged(from, to);
		if (to == ActorState.KICK_FRONT) {
			assert getCollider().isPresent();
			float x;
			float y = highFrontAttack.getTopLeft().getY();
			if (getFacing() == Dir.EAST) {
				x = getCollider().get().getTopRight().getX();
			} else {
				assert getFacing() == Dir.WEST;
				x = getCollider().get().getTopLeft().getX()
						- highFrontAttack.getWidth();
			}
			highFrontAttack.setOrigin(new Point(x, y));
			attach(highFrontAttack);
		}

		if (from == ActorState.KICK_FRONT) {
			detach(highFrontAttack);
		}
	}

}
