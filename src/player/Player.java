package player;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import images.SpriteBuilder;
import objects.Actor;
import objects.Collider;
import objects.Locker;
import objects.PickUpItem;
import objects.WorldObject;
import worlds.WorldLayer;

public class Player extends Actor {
	float speed;
	float jumpStrength = 0.03f;
	Inventory inventory;
	Map<Integer, Integer> keys;

	public Player(Point origin) {
		super(origin, WorldLayer.PLAYER, SpriteBuilder.getPlayerImg(),
				new Collider(
						SpriteBuilder.getPlayerImg().getBoundingRectangle()),
				null);
	}

	@Override
	public void act(GameContainer gc, int delta) {
		Input input = gc.getInput();
		if (gravityEnabled()) {
			if (input.isKeyDown(Input.KEY_SPACE)) {
				jump(jumpStrength);
			}
		} else {
			if (input.isKeyDown(Input.KEY_COMMA)) {
				move(Dir.NORTH, speed * delta);
			}
			if (input.isKeyDown(Input.KEY_O)) {
				move(Dir.SOUTH, speed * delta);
			}
		}

		if (input.isKeyDown(Input.KEY_A)) {
			move(Dir.WEST, speed * delta);
		}
		if (input.isKeyDown(Input.KEY_E)) {
			move(Dir.EAST, speed * delta);
		}
		if (input.isKeyDown(Input.KEY_PERIOD)) {
			interactWithAll();
		}
	}

	@Override
	protected void resetActorState() {
		speed = 0.01f;
		inventory = new Inventory();
		keys = new HashMap<Integer, Integer>();
	}

	private void addToInventory(InventoryItem ii) {
		if (ii != null) {
			inventory.add(ii);
		}
	}

	public void removeFromInventory(InventoryItem ii) {
		if (ii != null) {
			inventory.removeOne(ii);
		}
	}

	@Override
	public void overlapStart(WorldObject wo) {
		if (wo instanceof PickUpItem) {
			PickUpItem pui = (PickUpItem) wo;
			pui.disable();
			addToInventory(pui.getInventoryItem());
		} else if (wo instanceof Locker) {
			Locker lock = (Locker) wo;
			lock.unlock(this);
		}
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

	public boolean hasKey(int keyID) {
		InventoryItem key = InventoryItem.getKeyByID(keyID);
		return has(key);
	}

	public void useKey(int keyID) {
		InventoryItem key = InventoryItem.getKeyByID(keyID);
		removeFromInventory(key);
	}

	public Inventory getInventory() {
		return inventory;
	}
}

