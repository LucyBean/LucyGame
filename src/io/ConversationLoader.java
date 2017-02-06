package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import helpers.Point;
import objects.world.ItemType;
import objects.world.ObjectMaker;
import objects.world.WorldObject;
import objects.world.characters.Conversation;
import objects.world.characters.ConversationCharacter;
import objects.world.characters.ConversationSet;
import objects.world.characters.NPC;
import objects.world.characters.Player;
import quests.Objective;
import quests.PickUpObjective;
import quests.Quest;
import quests.TalkToObjective;
import worlds.World;
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
	public static void load(String name, World w) {
		lineNumber = 0;
		fileName = name;

		File f = new File("data/conversations/" + name + ".lucy");

		if (!f.exists()) {
			ErrorLogger.log("Attempted to load conversations for " + name
					+ " but no file exists.", 2);
		} else {
			try {
				reader = new BufferedReader(new FileReader(f));
				loadConversations(w);

				loadQuests(w);

			} catch (IOException ioe) {
				ErrorLogger.log(ioe, 4);
			}
		}
	}

	private static void loadConversations(World w) throws IOException {
		String nextLine;
		WorldMap map = w.getMap();

		// Find the conversations
		Pattern conversationsPattern = Pattern.compile("<conversations>");
		while ((nextLine = getNextLine()) != null) {
			Matcher m = conversationsPattern.matcher(nextLine);
			if (m.find()) {
				// Read the conversations
				Pattern npcPattern = Pattern.compile("<npc.*>");
				Pattern worldPattern = Pattern.compile("<world>");
				Pattern convEndPattern = Pattern.compile("</conversations>");
				while ((nextLine = getNextLine()) != null) {
					// Find the next NPC declaration line
					m = npcPattern.matcher(nextLine);
					if (m.find()) {
						// Find the NPC id
						Pattern npcIdPattern = propertyExtractor("id", "\\d+");
						Matcher npcIdMatcher = npcIdPattern.matcher(nextLine);
						int npcID = 0;
						if (npcIdMatcher.find()) {
							npcID = Integer.parseInt(npcIdMatcher.group(1));
							// Extract the conversation and add to the file
							ConversationSet cs = readConversationSet();
							NPC npc = map.getNPC(npcID);
							if (npc == null) {
								System.err.println("Loaded quest for unknown NPCid: " + npcID);
							} else {
								npc.setConversations(cs);
							}
						} else {
							logError("No NPC ID specified", 1);
						}
					}
					m = worldPattern.matcher(nextLine);
					if (m.find()) {
						ConversationSet cs = readConversationSet();
						w.setConversations(cs);
					}
					// Check for end of conversations
					m = convEndPattern.matcher(nextLine);
					if (m.find()) {
						return;
					}
				}
			}
		}
	}

	private static void loadQuests(World w) throws IOException {
		WorldMap map = w.getMap();
		// Find the quests
		String nextLine;
		Pattern questsPattern = Pattern.compile("<quests>");
		while ((nextLine = getNextLine()) != null) {
			Matcher m = questsPattern.matcher(nextLine);
			if (m.find()) {
				// Find the ID
				Pattern questIDPattern = Pattern.compile(
						"<quest\\s+id\\s*=\\s*\"(\\d+)\">");
				while (!(m = questIDPattern.matcher(nextLine)).find()) {
					nextLine = getNextLine();
					if (nextLine == null
							|| nextLine.matches("\\s*</quests>\\s*")) {
						return;
					}
				}
				int questID = Integer.parseInt(m.group(1));
				// Find the start condition
				Pattern startPattern = Pattern.compile(
						"<startedby\\s+type\\s*=\\s*\"([\\w_]+)\"");
				while (!(m = startPattern.matcher(nextLine)).find()) {
					nextLine = getNextLine();
					if (nextLine == null
							|| nextLine.matches("\\s*</quests>\\s*")) {
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
					if ((m = propertyExtractor("id", "\\d+").matcher(
							nextLine)).find()) {
						npcID = Integer.parseInt(m.group(1));
					} else {
						logError("Unspecified NPC id", 2);
					}
					if ((m = propertyExtractor("state", "\\d+").matcher(
							nextLine)).find()) {
						state = Integer.parseInt(m.group(1));
					} else {
						logError("Unspecified NPC state", 2);
					}

					// TODO: Parse objectives
					Quest q = new Quest(questID);
					List<Objective> objectives = loadObjectives();
					objectives.forEach(o -> q.add(o));

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
			getNext = nextLine != null && (nextLine.matches("\\s*#.*")
					|| nextLine.matches("\\s*"));
		} while (getNext);
		return nextLine;
	}

	private static List<Objective> loadObjectives() throws IOException {
		// Find a line that starts an objective
		String nextLine;
		Pattern objStartPattern = Pattern.compile("<objective");
		Matcher m;
		LinkedList<Objective> objectives = new LinkedList<>();

		while ((nextLine = getNextLine()) != null) {
			if (nextLine.matches("\\s*</quest>\\s*")) {
				break;
			}
			m = objStartPattern.matcher(nextLine);
			if (m.find()) {
				Objective o = loadObjective(nextLine);
				if (o != null) {
					objectives.add(o);
				}
			}
		}
		return objectives;
	}

	private static Objective loadObjective(String firstLine)
			throws IOException {
		String nextLine = firstLine;
		// Find the type
		Matcher m = propertyExtractor("type", "\\w+").matcher(nextLine);
		String type;
		Objective o = null;

		if (m.find()) {
			type = m.group(1).toLowerCase();
		} else {
			logError("Objective with no type", 2);
			return null;
		}

		// Create objective of correct type
		if (type.equals("pickup")) {
			// Find item type to pick up
			m = propertyExtractor("id", "\\w+").matcher(nextLine);
			String itemType;
			if (m.find()) {
				itemType = m.group(1);
			} else {
				logError("Pick up objective with no item specified", 2);
				return null;
			}
			ItemType it = null;
			try {
				it = ItemType.valueOf(itemType.toUpperCase());
			} catch (IllegalArgumentException e) {
				logError("Unknown object specified: " + itemType, 2);
				return null;
			}
			o = new PickUpObjective(it);
		} else if (type.equals("talkto")) {
			m = propertyExtractor("id", "\\d+").matcher(nextLine);
			int npcID;
			if (m.find()) {
				npcID = Integer.parseInt(m.group(1));
			} else {
				logError("Talk to objective with no NPC specified", 2);
				return null;
			}
			o = new TalkToObjective(npcID);
		} else {
			// TODO: Fill in other types
			logError("Unknown type of objective: " + type, 2);
			return null;
		}

		// Check for short objective
		m = Pattern.compile("/>").matcher(nextLine);
		if (m.find()) {
			return o;
		}

		loadQuestEffects(o, nextLine);
		return o;
	}

	/**
	 * Adds any effects described in the script to the objective
	 * 
	 * @param o
	 * @throws IOException
	 */
	private static void loadQuestEffects(Objective o, String thisLine)
			throws IOException {
		String nextLine = thisLine;
		Matcher m;
		// Find any effects
		do {
			Pattern effectPattern = Pattern.compile("<effect");
			if ((m = effectPattern.matcher(nextLine)).find()) {
				// Find the location
				m = propertyExtractor("loc", "\\w+").matcher(nextLine);
				String loc = "end";
				if (m.find()) {
					loc = m.group(1);
				}
				// Create a function that adds the effect in the create place
				Consumer<Consumer<World>> addEffect = null;
				if (loc.equals("end")) {
					addEffect = (ccw -> o.addEndEffect(ccw));
				} else if (loc.equals("start")) {
					addEffect = (ccw -> o.addStartEffect(ccw));
				} else {
					logError("Unknown effect location: " + loc, 2);
					return;
				}

				while (!(nextLine = getNextLine()).matches(
						"\\s*</effect>\\s*")) {
					// Find all effects
					Pattern setStatePattern = Pattern.compile(
							"setState\\((\\d+),(\\d+)\\)");
					m = setStatePattern.matcher(nextLine);
					if (m.find()) {
						int npcID = Integer.parseInt(m.group(1));
						int newState = Integer.parseInt(m.group(2));

						addEffect.accept(cw -> {
							if (cw != null && cw.getMap() != null) {
								NPC npc = cw.getMap().getNPC(npcID);
								if (npc != null) {
									npc.setStoryState(newState);
								}
							}
						});
					}

					Pattern addObjectPattern = Pattern.compile(
							"addObject\\(([\\w_]+),(\\d+),(\\d+),(\\d+),(\\d+)\\)");
					m = addObjectPattern.matcher(nextLine);
					if (m.find()) {
						String itemType = m.group(1).toUpperCase();
						int x = Integer.parseInt(m.group(2));
						int y = Integer.parseInt(m.group(3));
						int lockID = Integer.parseInt(m.group(4));
						int npcID = Integer.parseInt(m.group(5));

						try {
							ItemType it = ItemType.valueOf(itemType);
							addEffect.accept(cw -> {
								WorldObject wo = ObjectMaker.makeFromType(it,
										new Point(x, y), lockID, npcID);
								cw.addObject(wo);
							});
						} catch (IllegalArgumentException e) {
							logError("Unknown item type: " + itemType, 2);
						}
					}

					Pattern useObjectPattern = Pattern.compile(
							"use\\(([\\w_]+),(\\d+)\\)");
					m = useObjectPattern.matcher(nextLine);
					if (m.find()) {
						String itemType = m.group(1).toUpperCase();
						int quant = Integer.parseInt(m.group(2));

						try {
							ItemType it = ItemType.valueOf(itemType);
							addEffect.accept(cw -> {
								if (cw != null && cw.getMap() != null
										&& cw.getMap().getPlayer() != null) {
									Player p = (Player) cw.getMap().getPlayer();
									p.use(it, quant);
								}
							});
						} catch (IllegalArgumentException e) {
							logError("Unknown item type: " + itemType, 2);
						}
					}
				}
			}
		} while ((nextLine = getNextLine()) != null
				&& !nextLine.matches("\\s*</objective>\\s*"));

	}

	private static ConversationSet readConversationSet() throws IOException {
		ConversationSet cs = new ConversationSet();

		Conversation c = null;
		Integer start = null;
		int end = 0;
		ConversationCharacter prev = null;
		String prevChat = null;

		Pattern breakPattern = Pattern.compile("(<\\/npc>)|(<\\/world>)");
		Pattern convEndPattern = Pattern.compile("<\\/conv>");
		Pattern convStartPattern = Pattern.compile("<conv.*>");
		Pattern startStatePattern = propertyExtractor("start", "\\d+");
		Pattern endStatePattern = propertyExtractor("end", "\\d+");
		Pattern scriptWithNamePattern = Pattern.compile(
				"\\s*(\\w+)\\s*:\\s*(.+)\\s*");
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
					c = null;
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

	private static Pattern propertyExtractor(String propName, String pattern) {
		return Pattern.compile(propName + "\\s*=\\s*\"(" + pattern + ")\"");
	}

	private static void logError(String message, int severity) {
		ErrorLogger.log(
				message + " on line " + lineNumber + " for file " + fileName,
				severity);
	}
}
