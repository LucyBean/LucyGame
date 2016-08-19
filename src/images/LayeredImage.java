package images;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Point;
import options.GlobalOptions;

/**
 * A wrapper for Images that allows a Sprite to use multiple Images as a layer.
 * 
 * @author Lucy
 *
 */
public class LayeredImage {
	List<PositionedImage> layers;
	private int width;
	private int height;
	private int numLayers = 0;

	public LayeredImage(int width, int height, int numLayers) {
		layers = new LinkedList<PositionedImage>();
		addLayers(numLayers);
		this.width = width;
		this.height = height;
	}

	public LayeredImage(Image img) {
		layers = new LinkedList<PositionedImage>();
		layers.add(new PositionedImage(Point.ZERO, img));
		this.numLayers = 1;
		this.width = img.getWidth();
		this.height = img.getHeight();
	}

	public LayeredImage(List<PositionedImage> imgs) {
		layers = new LinkedList<PositionedImage>(imgs);
		numLayers = imgs.size();
		// TODO: Find the maximum width and height of the biggest layer
		Stream<Float> widths = imgs.stream().map(i -> i.getWidth());
		Stream<Float> heights = imgs.stream().map(i -> i.getHeight());

		if (numLayers > 0) {
			this.width = (int) imgs.get(0).getWidth();
			this.height = (int) imgs.get(0).getHeight();
		} else if (GlobalOptions.debug()) {
			System.err.println("Attempted to create a new LayeredImage "
					+ "from an empty list of images.");
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTopLayerNumber() {
		return numLayers - 1;
	}

	public void draw(float x, float y, float scale) {
		layers.stream().filter(i -> i != null).forEach(
				i -> i.getImage().draw(x + i.getOrigin().getX() * scale,
						y + i.getOrigin().getY() * scale, scale));
	}

	/**
	 * Adds more layers to this LayeredImage.
	 * 
	 * @param num
	 *            The number of layers to add.
	 */
	public void addLayers(int num) {
		numLayers += num;

		for (int i = 0; i < num; i++) {
			layers.add(null);
		}
	}

	/**
	 * Gets the Image on a given layer. If the layer does not already exist,
	 * then the layer will be created.
	 * 
	 * @param z
	 *            The index of the layer to get. 0 is the bottom layer.
	 * @return
	 */
	public PositionedImage getLayer(int z) {
		if (z >= 0 && z < numLayers) {
			PositionedImage img = layers.get(z);
			if (img == null) {
				try {
					img = new PositionedImage(Point.ZERO,
							new Image(width, height));
					layers.add(z, img);
				} catch (SlickException se) {
					System.err.println("Unable to create new layer on image.");
					if (GlobalOptions.debug()) {
						se.printStackTrace();
					}
				}
			}
			return img;
		} else {
			return null;
		}
	}

	/**
	 * Sets this layer to show the given PositionedImage.
	 * 
	 * @param layer
	 * @param img
	 */
	public void setLayer(int layer, PositionedImage img) {
		if (layer >= 0 && layer < numLayers) {
			if (GlobalOptions.debug()
					&& (img.getWidth() > width || img.getHeight() > height)) {
				System.err.println("Incorrectly sized image " + img.getWidth()
						+ "x" + img.getHeight() + " added to layer of size "
						+ width + "x" + height);
			}
			layers.add(layer, img);
		} else if (GlobalOptions.debug()) {
			System.err.println(
					"Attempting to add an image to an invalid layer " + layer);
		}
	}

	public void setLayer(int layer, Image img) {
		setLayer(layer, new PositionedImage(Point.ZERO, img));
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
		Image img = getLayer(layer).getImage();
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
	 * Sets the layer to show the text. The text will be centered. The layer
	 * specified will be completely cleared before the text is added.
	 * 
	 * @param layer
	 *            The number of the layer to fill. 0 is the background.
	 * @param text
	 *            The color with which to fill the layer.
	 */
	public void setTextCentered(int layer, String text) {
		Image img = getLayer(layer).getImage();
		if (img != null) {
			try {
				Graphics g = img.getGraphics();
				float w = g.getFont().getWidth(text);
				float h = g.getFont().getHeight(text);
				
				float width = img.getWidth();
				float height = img.getHeight();

				float x = (width - w) / 2;
				float y = (height - h) / 2;

				setText(layer, text, new Point(x, y));
			} catch (SlickException se) {
				System.err.println("Error while adding text to image layer.");
				if (GlobalOptions.debug()) {
					se.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sets the layer to show the text. The text will be aligned with its
	 * top-left at the point topLeft. The layer specified will be completely
	 * cleared before the text is added.
	 * 
	 * @param layer
	 *            The number of the layer to fill. 0 is the background.
	 * @param text
	 *            The color with which to fill the layer.
	 * @param topLeft
	 *            The point at which the topLeft of the text will be.
	 */
	public void setText(int layer, String text, Point topLeft) {
		Image img = getLayer(layer).getImage();
		if (img != null) {
			try {
				Graphics g = img.getGraphics();
				g.clear();

				g.setColor(Color.black);
				g.drawString(text, topLeft.getX(), topLeft.getY());
				g.flush();
			} catch (SlickException se) {
				System.err.println("Error while adding text to image layer.");
				if (GlobalOptions.debug()) {
					se.printStackTrace();
				}
			}
		}
	}
}
