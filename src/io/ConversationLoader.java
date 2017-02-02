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
import objects.world.characters.NPC;
import quests.Objective;
import quests.Quest;
import worlds.WorldMap;

public class ConversationLoader {
	private static int lineNumber;
	private static String fileName;
	private static BufferedReader reader;

	/**
	 * Loads conversations and quests from the given file and assigns them to
	 * characters
	 * 
	 * @param name
	 *            The name of the file (excluding the extension) within
	 *            data/conversations/
	 * @return A map of NPCid to ConversationSet
	 */
	public static void load(String name, WorldMap map) {
		lineNumber = 0;
		fileName = name;

		File f = new File("data/conversations/" + name + ".lucy");

		if (!f.exists()) {
			ErrorLogger.log("Attempted to load conversations for " + name + " but no file exists.", 2);
		} else {
			try {
				reader = new BufferedReader(new FileReader(f));
				loadConversations(map);

				loadQuests(map);

			} catch (IOException ioe) {
				ErrorLogger.log(ioe, 4);
			}
		}
	}
	
	private static void loadConversations(WorldMap map) throws IOException {
		String nextLine;

		// Find the conversations
		Pattern conversationsPattern = Pattern.compile("<conversations>");
		Map<Integer, ConversationSet> conversations = null;
		while ((nextLine = getNextLine()) != null) {
			Matcher m = conversationsPattern.matcher(nextLine);
			if (m.find()) {
				// Read the conversations
				conversations = loadConversations();
				break;
			}
		}

		// Assign conversations
		for (int npcID : conversations.keySet()) {
			ConversationSet cs = conversations.get(npcID);
			NPC npc = map.getNPC(npcID);
			if (npc == null) {
				System.err.println("Loaded quest for unknown NPCid: " + npcID);
			} else {
				npc.setConversations(cs);
			}
		}
	}
	
	private static void loadQuests(WorldMap map) throws IOException {
		// Find the quests
		String nextLine;
		Pattern questsPattern = Pattern.compile("<quests>");
		while ((nextLine = getNextLine()) != null) {
			Matcher m = questsPattern.matcher(nextLine);
			if (m.find()) {
				// Find the ID
				Pattern questIDPattern = Pattern.compile("<quest\\s+id\\s*=\\s*\"(\\d+)\">");
				while (!(m = questIDPattern.matcher(nextLine)).find()) {
					nextLine = getNextLine();
					if (nextLine == null || nextLine.matches("\\s*</quests>\\s*")) {
						return;
					}
				}
				int questID = Integer.parseInt(m.group(1));
				// Find the start condition
				Pattern startPattern = Pattern.compile("<startedby\\s+type\\s*=\\s*\"([\\w_]+)\"");
				while (!(m = startPattern.matcher(nextLine)).find()) {
					nextLine = getNextLine();
					if (nextLine == null || nextLine.matches("\\s*</quests>\\s*")) {
						logError("Unfinished quest script", 1);
						return;
					}
				}
				String questType = m.group(1);
				if (questType.equals("talkto")) {
					// If it is an NPC
					// Extract id and state
					int npcID = 0;
					int state = 0;
					if ((m = propertyExtractor("id").matcher(nextLine)).find()) {
						npcID = Integer.parseInt(m.group(1));
					} else {
						logError("Unspecified NPC id", 2);
					}
					if ((m = propertyExtractor("state").matcher(nextLine)).find()) {
						state = Integer.parseInt(m.group(1));
					} else {
						logError("Unspecified NPC state",2);
					}
					
					// TODO: Parse objectives
					Quest q = new Quest(questID);
					q.add(new Objective("Test", ei -> false));
					
					NPC npc = map.getNPC(npcID);
					if (npc == null) {
						logError("Quest specified for unknown NPC", 2);
					} else {
						npc.registerQuest(state, q);
					}
				}
				break;
			}
		}
	}

	private static String getNextLine() throws IOException {
		String nextLine;
		boolean getNext;
		// Use this to skip over comment lines
		do {
			nextLine = reader.readLine();
			lineNumber++;
			getNext = nextLine != null && (nextLine.matches("\\s*#.*") || nextLine.matches("\\s*"));
		} while (getNext);
		return nextLine;
	}

	private static Map<Integer, ConversationSet> loadConversations() throws IOException {
		Map<Integer, ConversationSet> conversations = new HashMap<>();
		String nextLine;
		Pattern npcPattern = Pattern.compile("<npc.*>");
		Pattern convEndPattern = Pattern.compile("</conversations>");
		while ((nextLine = getNextLine()) != null) {
			// Find the next NPC declaration line
			Matcher m = npcPattern.matcher(nextLine);
			if (m.find()) {
				// Find the NPC id
				Pattern npcIdPattern = propertyExtractor("id");
				Matcher npcIdMatcher = npcIdPattern.matcher(nextLine);
				int npcID = 0;
				if (npcIdMatcher.find()) {
					npcID = Integer.parseInt(npcIdMatcher.group(1));
					// Extract the conversation and add to the file
					ConversationSet cs = readConversationSet();
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

	private static ConversationSet readConversationSet() throws IOException {
		ConversationSet cs = new ConversationSet();

		Conversation c = null;
		Integer start = null;
		int end = 0;
		ConversationCharacter prev = null;
		String prevChat = null;

		Pattern breakPattern = Pattern.compile("<\\/npc>");
		Pattern convEndPattern = Pattern.compile("<\\/conv>");
		Pattern convStartPattern = Pattern.compile("<conv.*>");
		Pattern startStatePattern = propertyExtractor("start");
		Pattern endStatePattern = propertyExtractor("end");
		Pattern scriptWithNamePattern = Pattern.compile("\\s*(\\w+)\\s*:\\s*(.+)\\s*");
		Pattern scriptWithoutNamePattern = Pattern.compile("\\s*([^:<>]+)\\s*");

		String nextLine;
		while ((nextLine = getNextLine()) != null) {
			// If reach a </npc> line then stop reading lines
			Matcher m = breakPattern.matcher(nextLine);
			if (m.find()) {
				break;
			}

			// Check for the end of a conversation
			m = convEndPattern.matcher(nextLine);
			if (m.find()) {
				if (c == null) {
					logError("Badly formed conversation file: Too many </conv>", 1);
				} else {
					// Add the previous conversation to the set
					if (prevChat != null) {
						c.add(prev, prevChat);
						prevChat = null;
					}
					c.setEndState(end);
					Conversation cold = cs.put(start, c);
					if (cold != null) {
						logError("Duplicate conversations for start id " + start, 1);
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
					logError("Badly formatted conversation file: Conversation started before previous closed.", 1);
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
								+ "Conversation script given before valid conversation " + "declaration.", 1);
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
					logError("Badly formatted conversation file. First line " + "of script does not specify character.",
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
				logError("Badly formatted conversation file. " + "Conversation script given before conversation "
						+ "state declaration.", 1);
				return null;
			}
			c.add(prev, prevChat);
			c.setEndState(end);
			cs.put(start, c);
		}

		return cs;
	}

	private static Pattern propertyExtractor(String propName) {
		return Pattern.compile(propName + "\\s*=\\s*\"(\\d+)\"");
	}

	private static void logError(String message, int severity) {
		ErrorLogger.log(message + " on line " + lineNumber + " for file " + fileName, severity);
	}
}
