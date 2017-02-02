package quests;

import java.util.function.Function;

import objects.world.ItemType;
import objects.world.WorldObject;
import objects.world.characters.Player;

public class PickUpObjective extends Objective {

	private static Function<EventInfo, Boolean> genEventSatisfy(ItemType it) {
		Function<EventInfo, Boolean> fun = (ei -> {
			if (ei.getType() == EventType.PICK_UP && ei.getSubject() instanceof Player) {
				WorldObject obj = ei.getObject();
				return obj.getType() == it;
			}
			return false;
		});
		
		return fun;
	}
	
	public PickUpObjective(ItemType it) {
		super("Obtain a " + it.toString(), genEventSatisfy(it), w -> false);
		// TODO Auto-generated constructor stub
	}

}
