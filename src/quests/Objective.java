package quests;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import worlds.World;

public class Objective {
	private String displayText;
	private Function<EventInfo, Boolean> eventSatisfy;
	private Function<World, Boolean> initSatisfy;
	private List<Consumer<World>> endEffects;
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
	public Objective(String displayText, Function<EventInfo, Boolean> eventSatisfy,
			Function<World, Boolean> initSatisfy) {
		this.displayText = displayText;
		this.eventSatisfy = eventSatisfy;
		this.initSatisfy = initSatisfy;
		endEffects = new LinkedList<>();
	}

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
	
	public void applyEndEffects() {
		endEffects.forEach(cw -> cw.accept(world));
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
		satisfied = satisfied || initSatisfy.apply(world);
		return satisfied;
	}

	public void passEvent(EventInfo ei) {
		satisfied = satisfied || eventSatisfy.apply(ei);
	}

	public boolean isSatisfied() {
		return satisfied;
	}

	@Override
	public String toString() {
		return displayText;
	}
}
