package quests;

import java.util.function.Function;

import objects.world.characters.NPC;
import objects.world.characters.Player;

public class TalkToObjective extends Objective {
	private static Function<EventInfo, Boolean> genEventSatisfy(int npcID) {
		Function<EventInfo, Boolean> fun = (ei -> {
			if (ei.getType() == EventType.INTERACT && ei.getSubject() instanceof Player && ei.getObject() instanceof NPC) {
				NPC npc = (NPC) ei.getObject();
				return npc.getNPCID() == npcID;
			}
			return false;
		});
		
		return fun;
	}

	public TalkToObjective(int npcID) {
		super("Talk to " + npcID, genEventSatisfy(npcID), w -> false);
	}

}
