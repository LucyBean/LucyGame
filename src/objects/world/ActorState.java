package objects.world;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ActorState {
	private static List<ActorState> states = new LinkedList<>();
	
	public static ActorState IDLE = new ActorState("IDLE");
	public static ActorState WALK = new ActorState("WALK");
	public static ActorState RUN = new ActorState("RUN");
	public static ActorState JUMP = new ActorState("JUMP");
	public static ActorState FALL = new ActorState("FALL");
	public static ActorState WALL_SLIDE = new ActorState("WALL_SLIDE");
	public static ActorState CLIMB = new ActorState("CLIMB");
	public static ActorState CROUCH = new ActorState("CROUCH");
	public static ActorState CROUCH_WALK = new ActorState("CROUCH_WALK");
	public static ActorState PUSH_PULL_IDLE = new ActorState("PUSH_PULL_IDLE");
	public static ActorState PUSH = new ActorState("PUSH");
	public static ActorState PULL = new ActorState("PULL");
	public static ActorState CLIMB_TOP = new ActorState("CLIMB_TOP", 1152);
	
	private int duration;
	private String name;
	
	protected ActorState(String name, int duration) {
		this(name);
		FighterState.init();
		this.duration = duration;
	}
	
	protected ActorState(String name) {
		this.name = name;
		states.add(this);
	}
	
	public boolean controlsEnabled() {
		return duration == 0;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int ordinal() {
		int ord = 0;
		for(ActorState state : states) {
			if (this == state) {
				return ord;
			} else {
				ord++;
			}
		}
		
		throw new IllegalStateException("Disallowed value for ActorState.");
	}
	
	private String getName() {
		return name;
	}

	public static Optional<ActorState> valueOf(String name) {
		for (ActorState state: states) {
			if (name.equals(state.getName())) {
				return Optional.of(state);
			}
		}
		
		return Optional.empty();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
