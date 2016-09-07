package quests;

import objects.WorldObject;

public class EventInfo {
	private EventType type;
	private WorldObject subject;
	private WorldObject object;
	
	public EventInfo(EventType type, WorldObject subject, WorldObject object) {
		this.type = type;
		this.subject = subject;
		this.object = object;
	}
	
	public EventType getType() {
		return type;
	}
	
	public WorldObject getSubject() {
		return subject;
	}
	
	public WorldObject getObject() {
		return object;
	}
	
	@Override
	public String toString() {
		return "Type: " + type + ", Subject: " + subject + ", Object: " + object;
	}
}
