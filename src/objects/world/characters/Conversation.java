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
	
	public void setEndState(int endState) {
		// TODO: Make this do something
		this.endState = endState;
	}
	
	public int getEndState() {
		return endState;
	}
}
