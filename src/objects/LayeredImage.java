package objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * A wrapper for Images that allows a Sprite to use multiple Images as a layer.
 * 
 * @author Lucy
 *
 */
public class LayeredImage {
	Image[] layers;
	private int width;
	private int height;

	public LayeredImage(int width, int height, int numLayers) {
		layers = new Image[numLayers];
		this.width = width;
		this.height = height;
	}

	public LayeredImage(Image img) {
		layers = new Image[1];
		layers[0] = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getTopLayerNumber() {
		return layers.length - 1;
	}

	public void draw(float x, float y, float scale) {
		for (int i = 0; i < layers.length; i++) {
			if (layers[i] != null) {
				layers[i].draw(x, y, scale);
			}
		}
	}

	public Image getLayer(int z) {
		if (z >= 0 && z < layers.length) {
			Image img = layers[z];
			if (img == null) {
				try {
					img = new Image(width, height);
					layers[z] = img;
				} catch (SlickException se) {
					se.printStackTrace();
				}
			}
			return img;
		} else {
			return null;
		}
	}

	/**
	 * Completely fills the layer with the Color c.
	 * 
	 * @param layer
	 *            The number of the layer to fill. 0 is the background.
	 * @param c
	 *            The color with which to fill the layer.
	 */
	public void fillLayer(int layer, Color c) {
		Image img = getLayer(layer);
		if (img != null) {
			try {
				Graphics g = img.getGraphics();
				g.setColor(c);
				g.fillRect(0, 0, width, height);
				g.flush();
			} catch (SlickException se) {
				System.err.println("Error while filling layer.");
				se.printStackTrace();
			}
		}
	}

	/**
	 * Sets the layer to show the text. The text will be centered.
	 * 
	 * @param layer
	 *            The number of the layer to fill. 0 is the background.
	 * @param text
	 *            The color with which to fill the layer.
	 */
	public void setText(int layer, String text) {
		Image img = getLayer(layer);
		if (img != null) {
			try {
				Graphics g = img.getGraphics();
				g.clear();
				
				float w = g.getFont().getWidth(text);
				float h = g.getFont().getHeight(text);
				
				g.setColor(Color.black);
				g.drawString(text, (width - w)/2, (height - h)/2);
				g.flush();
			} catch (SlickException se) {
				System.err.println("Error while adding text to image layer");
				se.printStackTrace();
			}
		}
	}
}
