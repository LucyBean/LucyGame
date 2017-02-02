package quests;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.ErrorLogger;
import worlds.World;

public class Quest {
	private Iterator<Objective> iterator;
	private Objective current;
	private List<Objective> objectives;
	private int id;
	private World world;

	public Quest(int id) {
		objectives = new LinkedList<>();
		this.id = id;
	}

	public void add(Objective o) {
		objectives.add(o);
	}

	public void start(World world) {
		this.world = world;
		objectives.forEach(o -> o.setWorld(world));
		if (objectives.size() > 0) {
			world.questStarted(this);
			iterator = objectives.iterator();
			boolean finished = startNextObjective();
			if (finished) {
				world.questFinished(this);
			}
			System.out.println("Started quest " + id);
		} else {
			ErrorLogger.log("Attempting to start a quest with no objectives.", 1);
		}
	}

	public void signalEvent(EventInfo ei) {
		current.passEvent(ei);
		if (current.isSatisfied()) {
			boolean finished = startNextObjective();
			if (finished) {
				world.questFinished(this);
			}
		}
	}

	/**
	 * 
	 * @return Whether or not the Quest has finished
	 */
	private boolean startNextObjective() {
		// signal to do end effects to current
		
		do {
			if (current != null) {
				current.applyEndEffects();
			}
			if (iterator.hasNext()) {
				current = iterator.next();
				current.checkInitSatisfaction();
			} else {
				break;
			}
		} while (current.isSatisfied());
		
		return current.isSatisfied() && !iterator.hasNext();
	}

	public Objective getCurrent() {
		return current;
	}

	@Override
	public String toString() {
		return "Current:" + current + ". " + objectives.toString();
	}

}
