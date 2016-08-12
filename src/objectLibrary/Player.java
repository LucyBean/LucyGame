package objectLibrary;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import objectLibs.SpriteBuilder;
import objects.Actor;
import objects.Collider;
import objects.PickUpItem;
import objects.WorldObject;
import worlds.WorldLayer;

public class Player extends Actor {
	float speed;
	float jumpStrength = 0.03f;
	List<PickUpItem> inventory;

	public Player(Point origin) {
		super(origin, WorldLayer.PLAYER, SpriteBuilder.PLAYER,
				new Collider(SpriteBuilder.PLAYER.getBoundingRectangle()), null);
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
		inventory = new LinkedList<PickUpItem>();
	}
	
	public void addToInventory(PickUpItem pui) {
		inventory.add(pui);
		
		System.out.println("Inventory: " + inventory);
	}
	
	public void removeFromInventory(PickUpItem pui) {
		inventory.remove(pui);
		
		System.out.println("Inventory: " + inventory);
	}
	
	@Override
	public void overlapStart(WorldObject wo) {
		if (wo instanceof PickUpItem) {
			PickUpItem pui = (PickUpItem) wo;
			pui.disable();
			addToInventory(pui);
		}
	}
	
	/**
	 * Checks whether this Player has picked up this PickUpItem.
	 * @param pui 
	 * @return
	 */
	public boolean has(PickUpItem pui) {
		return inventory.contains(pui);
	}
}
