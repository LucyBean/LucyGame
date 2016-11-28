package player;

import java.util.function.Consumer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import images.SpriteBuilder;
import objects.Actor;
import objects.ActorState;
import objects.Collider;
import objects.ItemType;
import objects.WorldObject;
import worlds.WorldLayer;

public class Player extends Actor {
	private float jumpStrength = 0.019f;
	private Inventory inventory;
	private boolean jumpNextAct = false;

	public Player(Point origin) {
		super(origin, WorldLayer.PLAYER, ItemType.PLAYER,
				SpriteBuilder.getCharacterSprite(0),
				new Collider(Point.ZERO, 0.8f, 1.99f), null);
	}

	@Override
	public void act(GameContainer gc, int delta) {
		Input input = gc.getInput();
		Consumer<Dir> moveFunction;

		if (input.isKeyDown(Input.KEY_LSHIFT)) {
			moveFunction = (d -> walk(d, delta));
		} else {
			moveFunction = (d -> run(d, delta));
		}

		if (gravityEnabled()) {
			if (jumpNextAct) {
				jump(jumpStrength);
				jumpNextAct = false;
			}
		} else {
			if (input.isKeyDown(Input.KEY_COMMA)) {
				moveFunction.accept(Dir.NORTH);
			}
			if (input.isKeyDown(Input.KEY_O)) {
				moveFunction.accept(Dir.SOUTH);
			}
		}

		if (input.isKeyDown(Input.KEY_A)) {
			moveFunction.accept(Dir.WEST);
		}
		if (input.isKeyDown(Input.KEY_E)) {
			moveFunction.accept(Dir.EAST);
		}
		
		if (input.isKeyDown(Input.KEY_PERIOD)) {
			interactWithAll();
		}
	}
	
	@Override
	public void keyPressed (int keycode) {
		if (keycode == Input.KEY_SPACE) {
			jumpNextAct = true;
		}
	}

	@Override
	protected void resetActorState() {
		inventory = new Inventory();
	}

	public void addToInventory(InventoryItem ii) {
		if (ii != null) {
			inventory.add(ii);
		}
	}

	@Override
	public void stateChanged(ActorState from, ActorState to) {
		super.stateChanged(from, to);
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
	public boolean has(InventoryItem ii) {
		return inventory.has(ii);
	}

	public boolean has(InventoryItem ii, int quantity) {
		return inventory.has(ii, quantity);
	}

	public boolean hasKey(int keyID) {
		InventoryItem key = InventoryItem.getKeyByID(keyID);
		return has(key);
	}

	public void useKey(int keyID) {
		InventoryItem key = InventoryItem.getKeyByID(keyID);
		inventory.remove(key, 1);
	}

	public Inventory getInventory() {
		return inventory;
	}
}
