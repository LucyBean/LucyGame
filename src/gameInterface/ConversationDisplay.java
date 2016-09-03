package gameInterface;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;
import images.ConversationBlockSprite;

public class ConversationDisplay extends InterfaceElement {
	Iterator<String> wordListIterator;
	String savedWord;

	public ConversationDisplay() {
		super(new Point(50, 350), new ConversationBlockSprite());
	}

	public void setText(String s) {
		s = s.replace("\n", "");

		String[] words = s.split(" ");
		List<String> wordList = new LinkedList<String>();
		for (String word : words) {
			wordList.add(word);
		}

		wordListIterator = wordList.iterator();

		showNext();
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

	public void showNext() {
		try {
			ConversationBlockSprite cbs = (ConversationBlockSprite) getSprite();
			Image textLayer = cbs.getTextImage();
			Graphics g = textLayer.getGraphics();
			g.clear();
			g.setColor(Color.black);
			final int spaceWidth = 8;
			final int lineHeight = g.getFont().getLineHeight();
			Point textPoint = Point.ZERO;

			while (wordsRemaining()) {
				String nextWord;
				if (savedWord != null) {
					nextWord = savedWord;
					savedWord = null;
				} else {
					nextWord = wordListIterator.next();
				}
				int wordWidth = g.getFont().getWidth(nextWord);
				float newWidth = textPoint.getX() + wordWidth;

				// If we have reached the end of the line, wrap
				if (newWidth > textLayer.getWidth()) {
					textPoint = new Point(0, textPoint.getY() + lineHeight);
					// If we have run out of space, stop
					if (textPoint.getY() + lineHeight > textLayer.getHeight()) {
						savedWord = nextWord;
						break;
					}
				}

				g.drawString(nextWord, textPoint.getX(), textPoint.getY());
				textPoint = textPoint.move(Dir.EAST, wordWidth + spaceWidth);
			}

			g.flush();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void keyPressed(int keycode) {
		if(wordsRemaining()) {
			showNext();
		} else {
			getWorld().conversationFinished();
		}
	}
}
