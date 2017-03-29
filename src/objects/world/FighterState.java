package objects.world;

public class FighterState extends ActorState {
	public static FighterState KICK_FRONT = new FighterState("KICK_FRONT", 288);
	public static FighterState KICK_BACK = new FighterState("KICK_BACK", 384);
	
	public static void init() {
		
	}
	
	FighterState(String name, int duration) {
		super(name, duration);
	}
}
