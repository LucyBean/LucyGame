package characters;

import java.util.Iterator;
import java.util.LinkedList;

import helpers.Pair;

public class Conversation
		extends LinkedList<Pair<ConversationCharacter, String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 419051881480270030L;

	Iterator<Pair<ConversationCharacter, String>> iterator;

	public Pair<ConversationCharacter, String> getNext() {
		if (iterator == null) {
			iterator = iterator();
		}

		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			return null;
		}
	}

	public void add(ConversationCharacter c, String text) {
		add(new Pair<>(c, text));
	}
}
