package images;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class StaticImage implements LucyImage {
	Image myImage;
	public StaticImage(int width, int height) throws SlickException {
		myImage = new Image(width, height);
	}
	
	public StaticImage(Image img) {
		myImage = img;
	}
	
	public Graphics getGraphics() throws SlickException {
		return myImage.getGraphics();
	}
	
	@Override
	public void draw(float x, float y, float scale) {
		myImage.draw(x,y,scale);
		
	}
	@Override
	public int getWidth() {
		return myImage.getWidth();
	}
	@Override
	public int getHeight() {
		return myImage.getHeight();
	}
}
