package objects.world.characters;

import java.util.function.Consumer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import objects.attachments.AttackBox;
import objects.attachments.Collider;
import objects.images.SpriteBuilder;
import objects.world.Actor;
import objects.world.ActorState;
import objects.world.ItemType;
import objects.world.WorldObject;
import worlds.Controller;
import worlds.WorldLayer;

public class Player extends Actor {
	private Inventory inventory;
	private AttackBox fallAttack;

	public Player(Point origin) {
		super(origin, WorldLayer.PLAYER, ItemType.PLAYER,
				SpriteBuilder.getCharacterSprite(0),
				new Collider(Point.ZERO, 0.8f, 1.90f), null);

		fallAttack = new AttackBox(getCollider().getBottomLeft(), getCollider().getWidth(), 0.5f) {
			@Override
			public void effectOnPlayer() {
				resetMidAirJump();
				signalJumpRelative(0.5f);
			}
			
			@Override
			public void effectOnEnemy(Enemy e) {
				e.damage(1);
			}
		};
		
		getSprite().setAlpha(0.3f);
	}

	@Override
	public void act(GameContainer gc, int delta) {
		Input input = gc.getInput();
		Consumer<Dir> moveFunction;
		
		if (input.isKeyDown(Controller.CROUCH)) {
			startCrouch();
		}

		if (input.isKeyDown(Controller.WALK)) {
			moveFunction = (d -> walk(d, delta));
		} else {
			moveFunction = (d -> run(d, delta));
		}

		if (input.isKeyDown(Controller.LEFT) && !input.isKeyDown(Controller.RIGHT)) {
			moveFunction.accept(Dir.WEST);
		}
		if (input.isKeyDown(Controller.RIGHT) && !input.isKeyDown(Controller.LEFT)) {
			moveFunction.accept(Dir.EAST);
		}

		if (!gravityEnabled()) {
			if (input.isKeyDown(Controller.UP)) {
				moveFunction.accept(Dir.NORTH);
			}
			if (input.isKeyDown(Controller.DOWN)) {
				moveFunction.accept(Dir.SOUTH);
			}
		} else if (getState() == ActorState.CLIMB) {
			if (input.isKeyDown(Controller.UP)) {
				climb(Dir.NORTH, delta);
			}
			if (input.isKeyDown(Controller.DOWN)) {
				climb(Dir.SOUTH, delta);
			}
		} else {
			if (input.isKeyDown(Controller.JUMP)) {
				signalJumpSustain();
			}
		}
	}
	
	@Override
	public void keyPressed (int keycode) {
		if (gravityEnabled() && keycode == Controller.JUMP) {
			signalJump();
		}
		if (keycode == Controller.INTERACT) {
			signalInteract();
		}
	}

	@Override
	protected void resetActorState() {
		inventory = new Inventory();
	}

	public void addToInventory(ItemType ii) {
		if (ii != null) {
			inventory.add(ii);
		}
	}

	@Override
	public void stateChanged(ActorState from, ActorState to) {
		super.stateChanged(from, to);
		
		if (to == ActorState.FALL) {
			// Add the fall attack box
			attach(fallAttack);
		}
		
		if (from == ActorState.FALL) {
			detach(fallAttack);
		}
	}

	@Override
	public void overlapStart(WorldObject wo) {

	}

	/**
	 * Checks whether this Player has picked up this InventoryItem.
	 * 
	 * @param ii
	 * @return
	 */
	public boolean has(ItemType ii) {
		return inventory.has(ii);
	}

	public boolean has(ItemType ii, int quantity) {
		return inventory.has(ii, quantity);
	}
	
	public void use(ItemType it) {
		use(it, 1);
	}
	
	public void use(ItemType it, int quantity) {
		inventory.remove(it, quantity);
	}

	public Inventory getInventory() {
		return inventory;
	}
}
