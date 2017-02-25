package objects.gameInterface;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.newdawn.slick.Font;

import helpers.Pair;
import helpers.Point;
import helpers.Rectangle;
import objects.images.ConversationBlockSprite;
import objects.images.ImageBuilder;
import objects.images.TextImage;
import objects.world.characters.Conversation;
import objects.world.characters.ConversationCharacter;
import options.GlobalOptions;

public class ConversationDisplay extends InterfaceElement {
	Iterator<String> wordListIterator;
	String savedWord;
	Conversation current;

	public ConversationDisplay() {
		super(new Point(50, GlobalOptions.WINDOW_HEIGHT - ConversationBlockSprite.getImageHeight()),
				new ConversationBlockSprite());
	}

	public void setConversation(Conversation c) {
		current = c;
		showNextSpeech();
	}

	private void showNextSpeech() {
		Optional<Pair<ConversationCharacter, String>> oNext = current.getNext();
		
		
		if (oNext.isPresent()) {
			Pair<ConversationCharacter, String> next = oNext.get();
			setCharacter(next.getFirst());
			setText(next.getSecond());
			showNextTextLine();
		} else {
			current = null;
			getWorld().conversationFinished();
		}
	}

	private void setCharacter(ConversationCharacter cc) {
		ConversationBlockSprite cbs = (ConversationBlockSprite) getSprite().get();
		cbs.setCharacter(cc);
	}

	private void setText(String s) {
		s = s.replace("\n", "");

		String[] words = s.split(" ");
		List<String> wordList = new LinkedList<String>();
		for (String word : words) {
			wordList.add(word);
		}

		wordListIterator = wordList.iterator();
	}

	public boolean wordsRemaining() {
		if (savedWord != null) {
			return true;
		} else if (wordListIterator == null) {
			return false;
		} else {
			return wordListIterator.hasNext();
		}
	}

	public void showNextTextLine() {
		ConversationBlockSprite cbs = (ConversationBlockSprite) getSprite().get();
		Rectangle textBoxSize = cbs.getTextBoxSize();
		Font f = ImageBuilder.getFont();
		String nextLine = "";
		int textWidth = 0;
		int textHeight = 0;

		while (wordsRemaining()) {
			String nextWord;
			if (savedWord != null) {
				nextWord = savedWord;
				savedWord = null;
			} else {
				nextWord = wordListIterator.next();
			}
			int wordWidth = f.getWidth(nextWord);
			int newWidth = textWidth + wordWidth;

			// If we have reached the end of the line, wrap
			if (newWidth > textBoxSize.getWidth()) {
				nextLine += "\n";
				textWidth = 0;
				textHeight += f.getLineHeight();
				// If we have run out of space, stop
				if (textHeight + f.getLineHeight() > textBoxSize.getHeight()) {
					savedWord = nextWord;
					break;
				}
			}
			nextLine += nextWord + " ";
			textWidth += f.getWidth(" " + nextWord);
		}
		TextImage timg = new TextImage(nextLine);
		cbs.setText(timg);

	}

	@Override
	public void keyPressed(int keycode) {
		if (current != null) {
			if (wordsRemaining()) {
				showNextTextLine();
			} else {
				showNextSpeech();
			}
		}
	}
}
