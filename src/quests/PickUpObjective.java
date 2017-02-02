package quests;

import java.util.function.Function;

import objects.world.ItemType;
import objects.world.WorldObject;
import objects.world.characters.Player;
import worlds.World;

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
	
	private static Function<World, Boolean> genInitSatisfy(ItemType it) {
		Function<World, Boolean> fun = (w -> {
			if (w != null && w.getMap() != null && w.getMap().getPlayer() != null) {
				Player p = w.getMap().getPlayer();
				return p.has(it);
			}
			return false;
		});
		return fun;
	}
	
	public PickUpObjective(ItemType it) {
		super("Obtain a " + it.toString(), genEventSatisfy(it), genInitSatisfy(it));
		// TODO Auto-generated constructor stub
	}

}
