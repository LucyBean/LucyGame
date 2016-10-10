package quests;

import java.util.function.Function;

public class Objective {
	private String displayText;
	private Function<EventInfo, Boolean> satisfied;
	
	public Objective(String displayText, Function<EventInfo, Boolean> satisfied) {
		this.displayText = displayText;
		this.satisfied = satisfied;
	}
	
	public String getText() {
		return displayText;
	}
	
	public boolean isSatisfied(EventInfo ei) {
		return satisfied.apply(ei);
	}
	
	@Override
	public String toString() {
		return displayText;
	}
}
