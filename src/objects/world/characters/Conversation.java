package objects.world.characters;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.Input;

import helpers.Pair;
import io.ErrorLogger;

public class Conversation
		extends LinkedList<Pair<ConversationCharacter, String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 419051881480270030L;

	private Iterator<Pair<ConversationCharacter, String>> iterator;
	private int endState;

	public Optional<Pair<ConversationCharacter, String>> getNext() {
		// Need the iterator to persist between calls of getNext
		if (iterator == null) {
			iterator = iterator();
		}

		if (iterator.hasNext()) {
			Pair<ConversationCharacter, String> pcs = iterator.next();
			String s = pcs.getSecond();
			pcs.setSecond(parse(s));
			return Optional.of(pcs);
		} else {
			// Setting the iterator to null will 'reset' the conversation
			iterator = null;
			return Optional.empty();
		}
	}

	/**
	 * Replaces braces with their value
	 * 
	 * @param s
	 * @return
	 */
	private String parse(String s) {
		String t = new String(s);
		Pattern p = Pattern.compile("(\\{[^\\}\\{]*\\})");
		Matcher m = p.matcher(t);
		while (m.find()) {
			boolean replaced = false;
			Matcher m2 = Pattern.compile("\\{([^\\.]+)\\.([^\\.]+)\\}").matcher(
					t);
			if (m2.find()) {
				String cls = m2.group(1);
				String name = m2.group(2);
				if (cls.equals("Controller")) {
					Class<worlds.Controller> controller = worlds.Controller.class;
					Field[] dfs = controller.getDeclaredFields();
					// Find the corresponding key
					Field f = null;
					for (Field df : dfs) {
						if (df.getName().equals(name)) {
							f = df;
							break;
						}
					}
					try {
						if (f != null) {
							int keycode = f.getInt(null);
							String keyname = Input.getKeyName(keycode);
							// Replace parts inside braces with key name
							t = t.substring(0, m.start()) + keyname + t.substring(m.end());
							replaced = true;
						} else {
							ErrorLogger.log("Unknown controller key: " + name,
									3);
						}
					} catch (IllegalAccessException e) {
						ErrorLogger.log(e, 4);
					}
				}
			}
			if (!replaced) {
				ErrorLogger.log("Unknown pattern: " + m.group(1), 3);
			} else {
				// Need to reset the matcher
				m = p.matcher(t);
			}
		}
	return t;

	}

	public void add(ConversationCharacter c, String text) {
		add(new Pair<>(c, text));
	}

	/**
	 * The end state for the NPC after they have said this conversation.
	 * 
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
