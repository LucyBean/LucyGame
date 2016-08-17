package objectLibrary;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import inventoryItems.InventoryItem;
import objectLibs.SpriteBuilder;
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

		System.out.println("Inventory: " + inventory);
	}

	public void removeFromInventory(InventoryItem ii) {
		if (ii != null) {
			inventory.removeOne(ii);
		}

		System.out.println("Inventory: " + inventory);
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
}

class Inventory {
	Map<InventoryItem, Integer> items;

	public Inventory() {
		items = new HashMap<InventoryItem, Integer>();
	}

	/**
	 * Checks whether this Inventory contains this InventoryItem.
	 * 
	 * @param ii
	 * @return
	 */
	public boolean has(InventoryItem ii) {
		Integer n = items.get(ii);

		return n != null && n > 0;
	}

	/**
	 * Adds an InventoryItem to this Inventory.
	 * 
	 * @param ii
	 */
	public void add(InventoryItem ii) {
		items.putIfAbsent(ii, 0);
		int newAmount = items.get(ii) + 1;
		items.put(ii, newAmount);
	}

	/**
	 * Removes one of the InventoryItem from this Inventory, if it is in this
	 * Inventory.
	 * 
	 * @param ii
	 */
	public void removeOne(InventoryItem ii) {
		Integer n = items.get(ii);

		if (n != null && n > 0) {
			int newAmount = n - 1;
			if (newAmount > 0) {
				items.put(ii, newAmount);
			} else {
				items.remove(ii);
			}
		}
	}

	@Override
	public String toString() {
		return items.toString();
	}
}
