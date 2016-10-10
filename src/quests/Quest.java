package quests;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import options.GlobalOptions;

public class Quest {
	private Iterator<Objective> iterator;
	private Objective current;
	private List<Objective> objectives;

	public Quest() {
		objectives = new LinkedList<>();
	}

	public void add(Objective o) {
		objectives.add(o);
	}

	public void start() {
		if (objectives.size() > 0) {
			iterator = objectives.iterator();
			current = iterator.next();
			System.out.println("Started quest.");
			System.out.println(toString());
		} else if (GlobalOptions.debug()) {
			System.err.println(
					"Attempting to start a quest with no objectives.");
		}
	}

	public void signalEvent(EventInfo ei) {
		if (current.isSatisfied(ei)) {
			if (iterator.hasNext()) {
				current = iterator.next();
				System.out.println("I received a signal!");
				System.out.println(toString());
			} else {
				System.out.println("Quest finished.");
			}
		}
	}

	public Objective getCurrent() {
		return current;
	}

	@Override
	public String toString() {
		return "Current:" + current + ". " + objectives.toString();
	}

}
