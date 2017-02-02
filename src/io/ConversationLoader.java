package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import objects.world.characters.Conversation;
import objects.world.characters.ConversationCharacter;
import objects.world.characters.ConversationSet;

public class ConversationLoader {

	private static int lineNumber;
	private static String fileName;

	/**
	 * 
	 * @param name
	 *            The name of the file (excluding the extension) within
	 *            data/conversations/
	 * @return A map of NPCid to ConversationSet
	 */
	public static Map<Integer, ConversationSet> load(String name) {
		lineNumber = 0;
		fileName = name;

		Map<Integer, ConversationSet> conversations = null;
		File f = new File("data/conversations/" + name + ".lucy");

		if (!f.exists()) {
			ErrorLogger.log("Attempted to load conversations for " + name
					+ " but no file exists.", 2);
		} else {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String nextLine;
				Pattern conversationsPattern = Pattern.compile("<conversations>");
				// Find the conversations declaration line
				while ((nextLine = br.readLine()) != null) {
					lineNumber++;
					Matcher m = conversationsPattern.matcher(nextLine);
					if (m.find()) {
						// Read the conversations
						conversations = loadConversations(br);
					}
				}
				
			} catch (IOException ioe) {
				ErrorLogger.log(ioe, 4);
			}
		}

		return conversations;
	}
	
	private static Map<Integer, ConversationSet> loadConversations(BufferedReader br) throws IOException {
		Map<Integer, ConversationSet> conversations = new HashMap<>();
		String nextLine;
		Pattern npcPattern = Pattern.compile("<npc.*>");
		Pattern convEndPattern = Pattern.compile("</conversations>");
		while ((nextLine = br.readLine()) != null) {
			lineNumber++;
			// Find the next NPC declaration line
			Matcher m = npcPattern.matcher(nextLine);
			if (m.find()) {
				// Find the NPC id
				Pattern npcIdPattern = Pattern.compile(
						"id\\s*=\\s*\"(\\d+)\"");
				Matcher npcIdMatcher = npcIdPattern.matcher(nextLine);
				int npcID = 0;
				if (npcIdMatcher.find()) {
					npcID = Integer.parseInt(npcIdMatcher.group(1));
					// Extract the conversation and add to the file
					ConversationSet cs = readConversationSet(br);
					if (cs != null && cs.size() > 0) {
						conversations.put(npcID, cs);
					}
				} else {
					logError("No NPC ID specified", 1);
				}
			}
			// Check for end of conversations
			m = convEndPattern.matcher(nextLine);
			if (m.find()) {
				break;
			}
		}
		return conversations;
	}

	private static ConversationSet readConversationSet(BufferedReader br)
			throws IOException {
		ConversationSet cs = new ConversationSet();

		Conversation c = null;
		Integer start = null;
		int end = 0;
		ConversationCharacter prev = null;
		String prevChat = null;

		Pattern breakPattern = Pattern.compile("<\\/npc>");
		Pattern convEndPattern = Pattern.compile("<\\/conv>");
		Pattern convStartPattern = Pattern.compile("<conv.*>");
		Pattern startStatePattern = Pattern.compile("start\\s*=\\s*\"(\\d+)\"");
		Pattern endStatePattern = Pattern.compile("start\\s*=\\s*\"(\\d+)\"");
		Pattern scriptWithNamePattern = Pattern.compile(
				"\\s*(\\w+)\\s*:\\s*(.+)\\s*");
		Pattern scriptWithoutNamePattern = Pattern.compile("\\s*([^:<>]+)\\s*");

		String nextLine;
		while ((nextLine = br.readLine()) != null) {
			lineNumber++;
			// If reach a </npc> line then stop reading lines
			Matcher m = breakPattern.matcher(nextLine);
			if (m.find()) {
				break;
			}

			// Check for the end of a conversation
			m = convEndPattern.matcher(nextLine);
			if (m.find()) {
				if (c == null) {
					logError("Badly formed conversation file: Too many </conv>",
							1);
				} else {
					// Add the previous conversation to the set
					if (prevChat != null) {
						c.add(prev, prevChat);
						prevChat = null;
					}
					c.setEndState(end);
					Conversation cold = cs.put(start, c);
					if (cold != null) {
						logError(
								"Duplicate conversations for start id " + start,
								1);
					}
				}
				start = null;
			}

			// Check for the start of a declaration
			m = convStartPattern.matcher(nextLine);
			if (m.find()) {
				// Find start state
				m = startStatePattern.matcher(nextLine);
				if (m.find()) {
					start = Integer.parseInt(m.group(1));
				} else {
					start = 0;
				}
				// Find end state, if any
				m = endStatePattern.matcher(nextLine);
				if (m.find()) {
					end = Integer.parseInt(m.group(1));
				} else {
					// If no end state specified then set end to same as
					// start
					end = start;
				}
				if (c != null) {
					logError(
							"Badly formatted conversation file: Conversation started before previous closed.",
							1);
				}

				c = new Conversation();
			}

			// Check for a script start with NAME:
			m = scriptWithNamePattern.matcher(nextLine);
			if (m.matches()) {
				// Put the previously parsed chat in the conversation
				if (prevChat != null) {
					if (c == null) {
						logError("Badly formatted conversation file. "
								+ "Conversation script given before valid conversation "
								+ "declaration.", 1);
						return null;
					}
					c.add(prev, prevChat);
				}

				// This is the start of a character's conversation
				String charName = m.group(1).toUpperCase();
				try {
					prev = ConversationCharacter.valueOf(charName);
				} catch (IllegalArgumentException iae) {
					logError("Unknown character: " + charName, 1);
					prev = null;
				}
				prevChat = m.group(2);
			}

			// Check for a script without a NAME
			m = scriptWithoutNamePattern.matcher(nextLine);
			if (m.matches()) {
				if (prev == null) {
					logError(
							"Badly formatted conversation file. First line "
									+ "of script does not specify character.",
							1);
				}
				// This is a continuation of the previous chat
				prevChat += " " + m.group(1);
			}
		}

		// Add in the final chat if any left
		if (prevChat != null) {
			logError("Unterminated conversation tag", 1);
			if (c == null) {
				logError("Badly formatted conversation file. "
						+ "Conversation script given before conversation "
						+ "state declaration.", 1);
				return null;
			}
			c.add(prev, prevChat);
			c.setEndState(end);
			cs.put(start, c);
		}

		return cs;
	}

	private static void logError(String message, int severity) {
		ErrorLogger.log(
				message + " on line " + lineNumber + " for file " + fileName,
				severity);
	}
}
