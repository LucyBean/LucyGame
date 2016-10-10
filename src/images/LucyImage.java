package images;

public interface LucyImage {	
	public void draw(float x, float y, float scale);
	public int getWidth();
	public int getHeight();
	public void setMirrored(boolean mirrored);
}
