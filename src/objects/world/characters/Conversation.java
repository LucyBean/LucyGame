package objects.world.characters;

import java.util.Iterator;
import java.util.LinkedList;

import helpers.Pair;

public class Conversation
		extends LinkedList<Pair<ConversationCharacter, String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 419051881480270030L;

	private Iterator<Pair<ConversationCharacter, String>> iterator;
	private int endState;

	public Pair<ConversationCharacter, String> getNext() {
		// Need the iterator to persist between calls of getNext
		if (iterator == null) {
			iterator = iterator();
		}

		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			// Setting the iterator to null will 'reset' the conversation
			iterator = null;
			return null;
		}
	}

	public void add(ConversationCharacter c, String text) {
		add(new Pair<>(c, text));
	}
	
	/**
	 * The end state for the NPC after they have said this conversation.
	 * @param endState
	 */
	public void setEndState(int endState) {
		this.endState = endState;
	}
	
	public int getEndState() {
		return endState;
	}
	
	@Override
	public String toString() {
		if (size() <= 0) {
			return "No conversation :(";
		} else {
			Pair<ConversationCharacter, String> first = get(0);
			String name = "Unknown";
			if (first.getFirst() != null) {
				name = first.getFirst().getName();
			}
			String text = first.getSecond();
			return name + ": " + text;
		}
	}
}
