package characters;

import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;
import objects.ItemType;

public class DogEnemy extends Enemy {
	private Dir facing;

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
		
		if (!getFloorSensor().isOverlappingSolid()) {
			facing = facing.neg();
		}
	}

}
