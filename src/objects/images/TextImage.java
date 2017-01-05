package objects.images;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

public class TextImage implements LucyImage {

	private String text;
	Font f;
	
	public TextImage(String text) {
		this.text = text;
		f = ImageBuilder.getFont();
	}
	
	@Override
	public void draw(float x, float y, float scale) {
		f.drawString(x, y, text, Color.black);
	}

	@Override
	public int getWidth() {
		return f.getWidth(text);
	}

	@Override
	public int getHeight() {
		return f.getHeight(text);
	}

	@Override
	public void setMirrored(boolean mirrored) {
		// This cannot be done
	}

}
