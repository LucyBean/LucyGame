package objects.world.characters;

import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;
import objects.world.ActorSensors;
import objects.world.ItemType;

public class DogEnemy extends Enemy {
	private Dir facing;
	private int health = 2;

	public DogEnemy(Point origin) {
		super(origin, ItemType.DOG_ENEMY);
		setMoveSpeed(0.005f);
	}

	@Override
	protected void resetActorState() {
		facing = Dir.EAST;
	}

	@Override
	public void act(GameContainer gc, int delta) {
		walk(facing, delta);
		
		if (!getSensor(ActorSensors.FLOOR).isOverlappingSolid()) {
			facing = facing.neg();
		}
	}
	
	@Override
	public void damage(int amount) {
		health--;
		if (health <= 0) {
			disable();
		}
	}

}
