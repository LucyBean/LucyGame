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
		CLIMB_TOP(1152),
		KICK_FRONT(288),
		KICK_BACK(384);
	
	private int duration;
	
	ActorState(int duration) {
		this.duration = duration;
	}
	
	ActorState() {
		
	}
	
	public boolean controlsEnabled() {
		return duration == 0;
	}
	
	public int getDuration() {
		return duration;
	}
	
	
}
