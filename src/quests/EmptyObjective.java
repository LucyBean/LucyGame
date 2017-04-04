package quests;

import worlds.World;

public class EmptyObjective extends Objective {

	public EmptyObjective() {
		super("Empty objective.");
	}

	@Override
	protected boolean eventSatisfy(EventInfo ei) {
		return true;
	}

	@Override
	protected boolean initSatisfy(World w) {
		return true;
	}

}
