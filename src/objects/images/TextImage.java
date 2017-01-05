package objects.images;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

public class TextImage implements LucyImage {
	private String[] lines;
	private Font f;
	private int width = 0;
	private int height = 0;

	public TextImage(String text) {
		if (text != null) {
			lines = text.split("\n");
			f = ImageBuilder.getFont();
			
			if (lines.length > 0) {
				for (int i = 0; i < lines.length; i++) {
					width = Math.max(width, f.getWidth(lines[i]));
					height += f.getHeight(lines[i]);
				}
			}
		}
	}

	@Override
	public void draw(float x, float y, float scale) {
		if (lines != null) {
			for (int i = 0; i < lines.length; i++) {
				f.drawString(x,y, lines[i], Color.black);
				y += f.getHeight(lines[i]);
			}
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setMirrored(boolean mirrored) {
		// This cannot be done with text!
		// It doesn't make sense!
	}

}
