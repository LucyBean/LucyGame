package player;

import java.util.HashMap;
import java.util.Map;
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
import objects.Sensor;
import objects.WorldObject;
import worlds.WorldLayer;

public class Player extends Actor {
	float jumpStrength = 0.02f;
	Inventory inventory;
	Map<Integer, Integer> keys;
	Sensor floorSensor;

	public Player(Point origin) {
		super(origin, WorldLayer.PLAYER, ItemType.PLAYER,
				SpriteBuilder.getCharacterSprite(0),
				new Collider(Point.ZERO, 0.8f, 1.99f), null);
		setSpritePosition();
	}

	private void setSpritePosition() {
		// Set position of Player's sprite such that bottom-middle points of
		// sprite and collider coincide.
		float newX = (getCollider().getWidth()
				- getSprite().getRectangle().getWidth()) / 2;
		float newY = (getCollider().getHeight()
				- getSprite().getRectangle().getHeight());
		getSprite().setOrigin(new Point(newX, newY));
	}

	@Override
	public void act(GameContainer gc, int delta) {
		Input input = gc.getInput();
		Consumer<Dir> mf;
		
		if (input.isKeyDown(Input.KEY_LSHIFT)) {
			mf = (d -> walk(d, delta));
		} else {
			mf = (d -> run(d, delta));
		}
		
		if (gravityEnabled()) {
			if (input.isKeyDown(Input.KEY_SPACE)) {
				jump(jumpStrength);
			}
		} else {
			if (input.isKeyDown(Input.KEY_COMMA)) {
				mf.accept(Dir.NORTH);
			}
			if (input.isKeyDown(Input.KEY_O)) {
				mf.accept(Dir.SOUTH);
			}
		}

		if (input.isKeyDown(Input.KEY_A)) {
			mf.accept(Dir.WEST);
		}
		if (input.isKeyDown(Input.KEY_E)) {
			mf.accept(Dir.EAST);
		}
		if (input.isKeyDown(Input.KEY_PERIOD)) {
			interactWithAll();
		}
	}

	@Override
	protected void resetActorState() {
		inventory = new Inventory();
		keys = new HashMap<Integer, Integer>();
	}

	public void addToInventory(InventoryItem ii) {
		if (ii != null) {
			inventory.add(ii);
		}
	}
	
	@Override
	public void stateChanged(ActorState from, ActorState to) {
		super.stateChanged(from, to);
		setSpritePosition();
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
