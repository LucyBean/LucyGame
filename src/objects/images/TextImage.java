package objects.images;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

public class TextImage implements LucyImage {
	private String[] lines;
	private Font f;
	private int width = 0;
	private int height = 0;
	private int hAlign = 0;
	private int vAlign = 0;

	public TextImage(String text) {
		setText(text);
	}

	public void setText(String text) {
		if (text != null) {
			width = 0;
			height = 0;
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

	/**
	 * @param hAlign
	 *            The horizontal alignment for the layer relative to the origin.
	 *            0 = left, 1 = center, 2 = right
	 * @param vAlign
	 *            The vertical alignment for the layer relative to the origin. 0
	 *            = top, 1 = middle, 2 = bottom
	 */
	public void setAlignment(int hAlign, int vAlign) {
		if (hAlign >= 0 && hAlign <= 2) {
			this.hAlign = hAlign;
		} else {
			this.hAlign = 0;
		}
		if (vAlign >= 0 && vAlign <= 2) {
			this.vAlign = vAlign;
		} else {
			this.vAlign = 0;
		}
	}

	@Override
	public void draw(float x, float y, float scale) {
		// Translate x and y according to alignment
		int w = getWidth();
		int h = getHeight();

		x -= (w * hAlign) / 2;
		y += (h * vAlign) / 2;

		if (lines != null) {
			for (int i = 0; i < lines.length; i++) {
				f.drawString(x, y, lines[i], Color.black);
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
