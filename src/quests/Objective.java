package quests;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import worlds.World;

public abstract class Objective {
	private String displayText;
	private List<Consumer<World>> endEffects = new LinkedList<>();
	private List<Consumer<World>> startEffects = new LinkedList<>();
	private World world;
	private boolean satisfied = false;

	/**
	 * Create a new Objective.
	 * 
	 * @param displayText
	 *            The helper text.
	 * @param eventSatisfy
	 *            A function that determines satisfaction of this objective due
	 *            to an event
	 * @param initSatisfy
	 *            A function that determines satisfaction of this objective when
	 *            starting the objective
	 */
	public Objective(String displayText) {
		this.displayText = displayText;
	}
	
	protected abstract boolean eventSatisfy(EventInfo ei);
	protected abstract boolean initSatisfy(World w);

	/**
	 * Sets the World. This must be done before the objective is started.
	 * @param w
	 */
	public void setWorld(World w) {
		world = w;
	}
	
	public void addEndEffect(Consumer<World> cw) {
		endEffects.add(cw);
	}
	
	public void addStartEffect(Consumer<World> cw) {
		startEffects.add(cw);
	}
	
	public void applyEndEffects() {
		endEffects.forEach(cw -> cw.accept(world));
	}
	
	public void applyStartEffects() {
		startEffects.forEach(cw -> cw.accept(world));
	}

	public String getText() {
		return displayText;
	}

	/**
	 * Applies the initial satisfaction rule
	 * 
	 * @return Whether this Objective is now satisfied
	 */
	public boolean checkInitSatisfaction() {
		satisfied = satisfied || initSatisfy(world);
		return satisfied;
	}

	public void passEvent(EventInfo ei) {
		satisfied = satisfied || eventSatisfy(ei);
	}

	public boolean isSatisfied() {
		return satisfied;
	}

	@Override
	public String toString() {
		return displayText;
	}
}
