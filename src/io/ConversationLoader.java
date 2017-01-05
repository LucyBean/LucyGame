package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import objects.images.ImageBuilder;
import objects.world.characters.Conversation;
import objects.world.characters.ConversationCharacter;
import objects.world.characters.ConversationSet;

public class ConversationLoader {

	/**
	 * 
	 * @param name
	 *            The name of the file (excluding the extension) within
	 *            data/conversations/
	 * @return A map of NPCid to ConversationSet
	 */
	public static Map<Integer, ConversationSet> loadConversations(String name) {
		Map<Integer, ConversationSet> conversations = new HashMap<>();

		File f = new File("data/conversations/" + name + ".conv");

		if (!f.exists()) {
			System.err.println("Attempted to load conversations for " + name
					+ " but no file exists.");
		} else {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String nextLine = br.readLine();
				Pattern npcLinePattern = Pattern.compile("NPCid:\\s*(\\d+)");
				// Read the whole file
				while (nextLine != null) {
					// Try to find an NPCid line
					Matcher npcLineMatcher = npcLinePattern.matcher(nextLine);
					if (npcLineMatcher.matches()) {
						Integer npcID = Integer.parseInt(
								npcLineMatcher.group(1));
						ConversationSet cs = readConversationSet(br);
						conversations.put(npcID, cs);
					}
					nextLine = br.readLine();
				}
			} catch (IOException ioe) {
				System.err.println("IOException occurred while reading " + f);
				ioe.printStackTrace();
			}
		}

		return conversations;
	}

	private static ConversationSet readConversationSet(BufferedReader br)
			throws IOException {
		ConversationSet cs = new ConversationSet();

		String nextLine = br.readLine();
		Pattern stateLinePattern = Pattern.compile("STATE:\\s*(\\d+)");
		Pattern endLinePattern = Pattern.compile("END:\\s*(\\d+)");
		Pattern scriptWithNamePattern = Pattern.compile(
				"\\s*(\\w+)\\s*:\\s*(.+)\\s*");
		Pattern scriptWithoutNamePattern = Pattern.compile("\\s*([^:]+)\\s*");

		Conversation c = null;
		Integer state = null;
		int end = 0;
		ConversationCharacter prev = null;
		String prevChat = null;

		// This loop terminates at the end of file or when an empty line is
		// reached
		while (nextLine != null && !nextLine.equals("")) {
			Matcher stateLineMatcher = stateLinePattern.matcher(nextLine);
			Matcher endLineMatcher = endLinePattern.matcher(nextLine);
			Matcher scriptWithNameMatcher = scriptWithNamePattern.matcher(
					nextLine);
			Matcher scriptWithoutNameMatcher = scriptWithoutNamePattern.matcher(
					nextLine);
			if (stateLineMatcher.matches()) {
				// If this is a state line then need to make a new conversation
				// The previous conversation (if any) is done and can be added
				// to the set
				if (c != null) {
					c.setEndState(end);
					cs.put(state, c);
				}
				state = Integer.parseInt(stateLineMatcher.group(1));
				c = new Conversation();
				// By default, the ending state will be the start state.
				end = state;
			} else if (endLineMatcher.matches()) {
				// This is an END: line
				end = Integer.parseInt(endLineMatcher.group(1));
			} else if (scriptWithNameMatcher.matches()) {
				// Put the previously parsed chat in the conversation
				if (prevChat != null) {
					if (c == null) {
						System.err.println("Badly formatted conversation file.");
						System.err.println("Conversation script given before conversation "
								+ "state declaration.");
						return null;
					}
					c.add(prev, prevChat);
				}

				// This is the start of a character's conversation
				String charName = scriptWithNameMatcher.group(1).toUpperCase();
				try {
					prev = ConversationCharacter.valueOf(charName);
				} catch (IllegalArgumentException iae) {
					System.err.println("Unknown character: " + charName);
					prev = null;
				}
				prevChat = scriptWithNameMatcher.group(2);
			} else if (scriptWithoutNameMatcher.matches()) {
				if (prev == null) {
					System.err.println("Badly formatted conversation file.");
					System.err.println("First line of script does not specify character.");
				}
				// This is a continuation of the previous chat
				prevChat += " " + scriptWithoutNameMatcher.group(1);
			}

			nextLine = br.readLine();
		}
		// Add in the final chat
		if (prevChat != null) {
			if (c == null) {
				System.err.println("Badly formatted conversation file.");
				System.err.println("Conversation script given before conversation "
						+ "state declaration.");
				return null;
			}
			c.add(prev, prevChat);
			c.setEndState(end);
			cs.put(state, c);
		}

		return cs;
	}
}
