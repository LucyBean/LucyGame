package quests;

import objects.world.characters.NPC;
import objects.world.characters.Player;
import worlds.World;

public class TalkToObjective extends Objective {
	private int npcID;

	public TalkToObjective(int npcID) {
		super("Talk to " + npcID);
		this.npcID = npcID;
	}

	@Override
	protected boolean eventSatisfy(EventInfo ei) {
		if (ei.getType() == EventType.INTERACT && ei.getSubject() instanceof Player && ei.getObject() instanceof NPC) {
			NPC npc = (NPC) ei.getObject();
			return npc.getNPCID() == npcID;
		}
		return false;
	}

	@Override
	protected boolean initSatisfy(World w) {
		return false;
	}

}
