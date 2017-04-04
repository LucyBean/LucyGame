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
	private int attachAttackBoxTimer;
	private Optional<AttackBox> waitingAttackBox = Optional.empty();
	private Optional<FighterState> queuedAttack = Optional.empty();

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
	
	@Override
	public final void actAlways(GameContainer gc, int delta) {
		if (waitingAttackBox.isPresent()) {
			attachAttackBoxTimer -= delta;
			if (attachAttackBoxTimer < 0) {
				attach(waitingAttackBox.get());
				waitingAttackBox = Optional.empty();
			}
		}
		if (kickNextFrame && getState() instanceof FighterState) {
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
		
		if (!(getState() instanceof FighterState)) {
			setState(FighterState.KICK_FRONT);
		}
		if (getState() == FighterState.KICK_FRONT) {
			queuedAttack = Optional.of(FighterState.KICK_BACK);
		}
	}
	
	@Override
	public void stateChanged(ActorState from, ActorState to) {
		super.stateChanged(from, to);
		if (to == FighterState.KICK_BACK || to == FighterState.KICK_FRONT) {
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
			waitingAttackBox = Optional.of(highFrontAttack);
			attachAttackBoxTimer = to.getDuration()/2;
		}

		if (from == FighterState.KICK_BACK || from == FighterState.KICK_FRONT) {
			detach(highFrontAttack);
			if (queuedAttack.isPresent()) {
				FighterState attack = queuedAttack.get();
				queuedAttack = Optional.empty();
				setState(attack);
				stateChanged(from, attack);
			}
		}
	}

}
