package objects.world;

public enum ActorState {
	IDLE,
		WALK,
		RUN,
		JUMP,
		FALL,
		WALL_SLIDE,
		CLIMB,
		CROUCH,
		CROUCH_WALK,
		PUSH_PULL_IDLE,
		PUSH,
		PULL,
		CLIMB_TOP(false),
		KICK_FRONT(false),
		KICK_BACK(false);
	
	private boolean controlsEnabled;
	
	ActorState(boolean controlsEnabled) {
		this.controlsEnabled = controlsEnabled;
	}
	
	ActorState() {
		this(true);
	}
	
	public boolean controlsEnabled() {
		return controlsEnabled;
	}
}
