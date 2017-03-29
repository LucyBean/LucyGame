package quests;

import objects.world.ItemType;
import objects.world.WorldObject;
import objects.world.characters.Player;
import worlds.World;

public class PickUpObjective extends Objective {
	private ItemType it;


	public PickUpObjective(ItemType it) {
		super("Obtain a " + it.toString());
		this.it = it;
	}

	@Override
	protected boolean eventSatisfy(EventInfo ei) {
		if (ei.getType() == EventType.PICK_UP && ei.getSubject() instanceof Player) {
			WorldObject obj = ei.getObject();
			return obj.getType() == it;
		}
		return false;
	}

	@Override
	protected boolean initSatisfy(World w) {
		if (w != null && w.getMap() != null && w.getMap().getPlayer() != null) {
			Player p = w.getMap().getPlayer();
			return p.has(it);
		}
		return false;
	}

}
