package objects.images;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.newdawn.slick.SlickException;

import helpers.Point;
import helpers.Rectangle;
import io.ErrorLogger;

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
	private boolean mirrored = false;

	public LayeredImage(int width, int height, int numLayers) {
		layers = new LinkedList<PositionedImage>();
		addLayers(numLayers);
		this.width = width;
		this.height = height;
	}

	public LayeredImage(LucyImage img) {
		layers = new LinkedList<PositionedImage>();
		layers.add(new PositionedImage(Point.ZERO, img));
		this.numLayers = 1;
		this.width = img.getWidth();
		this.height = img.getHeight();
	}

	public LayeredImage(List<PositionedImage> imgs) {
		layers = new LinkedList<PositionedImage>(imgs);
		numLayers = imgs.size();
		Optional<Float> w = imgs.stream().map(i -> i.getWidth()).reduce(
				Float::max);
		Optional<Float> h = imgs.stream().map(i -> i.getHeight()).reduce(
				Float::max);

		if (w.isPresent() && h.isPresent()) {
			this.width = (int) ((float) w.get());
			this.height = (int) ((float) h.get());
		} else {
			ErrorLogger.log("Attempted to create a new LayeredImage "
					+ "from an empty list of images.", 2);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle getRectangle() {
		return new Rectangle(Point.ZERO, width, height);
	}

	public int getTopLayerNumber() {
		return numLayers - 1;
	}

	public void draw(float x, float y, float scale) {
		layers.stream().filter(i -> i != null && i.getImage() != null).forEach(
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
							new StaticImage(width, height));
					layers.add(z, img);
				} catch (SlickException se) {
					ErrorLogger.log(se, "Unable to create new layer on image.",
							4);
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
			if (layers.get(layer) != null) {
				layers.remove(layer);
			}
			layers.add(layer, img);
		} else {
			ErrorLogger.log(
					"Attempting to add an image to an invalid layer " + layer,
					1);
		}
	}

	/**
	 * Sets the origin position for the image to be drawn to a layer.
	 * 
	 * @param layer
	 *            The layer for which the origin position will be set.
	 * @param p
	 *            The new origin position for the layer.
	 */
	public void setLayerPosition(int layer, Point p) {
		if (layer >= 0 && layer < numLayers) {
			PositionedImage pi = layers.get(layer);
			if (pi != null) {
				pi.setPosition(p);
			} else {
				layers.add(layer, new PositionedImage(p, null));
			}
		} else {
			ErrorLogger.log(
					"Attempting to add an image to an invalid layer " + layer,
					1);
		}
	}

	/**
	 * Sets this layer's image. Will be drawn at the origin position for that
	 * layer.
	 * 
	 * @param layer
	 * @param img
	 */
	public void setLayer(int layer, LucyImage img) {
		PositionedImage pimg = layers.get(layer);
		Point point;
		if (pimg != null) {
			point = pimg.getOrigin();
		} else {
			point = Point.ZERO;
		}
		setLayer(layer, new PositionedImage(point, img));
	}

	/**
	 * Replaces this layer with an image containing the uiColour. Will fill the
	 * entire LayeredImage if this layer does not already exist, otherwise
	 * replaces the image on the specified layer with a block of colour of the
	 * same size.
	 * 
	 * @param layer
	 *            The number of the layer to fill. 0 is the background.
	 * @param c
	 *            The color with which to fill the layer.
	 */
	public void fillLayer(int layer, int uiColour) {
		LucyImage img = getLayer(layer).getImage();
		int width;
		int height;
		Point origin;
		if (img != null) {
			origin = getLayer(layer).getOrigin();
			width = img.getWidth();
			height = img.getHeight();
		} else {
			origin = Point.ZERO;
			width = getWidth();
			height = getHeight();
		}
		setLayer(layer, new PositionedImage(origin,
				ImageBuilder.getColouredRectangle(width, height, uiColour)));
	}

	/**
	 * Clears the layer, setting the image to be transparent if it is a static
	 * image layer.
	 * 
	 * @param layer
	 */
	public void clear(int layer) {
		LucyImage img = getLayer(layer).getImage();
		if (img instanceof TextImage) {
			// Set to blank text if TextImage to keep its
			// hAlign and vAlign properties
			TextImage timg = (TextImage) img;
			timg.setText("");
		} else {
			getLayer(layer).setImage(null);
		}
	}

	/**
	 * Clears all layers, setting all images to be transparent.
	 */
	public void clearAll() {
		for (int i = 0; i < numLayers; i++) {
			clear(i);
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
	 *            The text to draw.
	 * @param origin
	 *            The point at which the text will be drawn.
	 * @param hAlign
	 *            The horizontal alignment for the layer relative to the origin.
	 *            0 = left, 1 = center, 2 = right
	 * @param vAlign
	 *            The vertical alignment for the layer relative to the origin. 0
	 *            = top, 1 = middle, 2 = bottom
	 */
	public void setText(int layer, String text, Point origin, int hAlign,
			int vAlign) {
		TextImage timg = new TextImage(text);
		float w = timg.getWidth();
		float h = timg.getHeight();

		float x = origin.getX() - (w * hAlign) / 2;
		float y = origin.getY() - (h * vAlign) / 2;

		setLayer(layer, new PositionedImage(new Point(x, y), timg));
	}

	/**
	 * Sets the layer to show the text. The text will be centered. The layer
	 * specified will be completely cleared before the text is added.
	 * 
	 * @param layer
	 *            The number of the layer to fill. 0 is the background.
	 * @param text
	 *            The text to show
	 */
	public void setTextCentered(int layer, String text) {
		TextImage timg = new TextImage(text);
		float x = (getWidth() - timg.getWidth()) / 2;
		float y = (getHeight() - timg.getHeight()) / 2;
		setLayer(layer, new PositionedImage(new Point(x, y), timg));
	}

	/**
	 * Sets the layer to show the text. The text will be aligned with its
	 * top-left at the point topLeft. The layer specified will be completely
	 * cleared before the text is added. This will only work if the layer is a
	 * static image layer.
	 * 
	 * @param layer
	 *            The number of the layer to fill. 0 is the background.
	 * @param text
	 *            The text to draw.
	 * @param topLeft
	 *            The point at which the topLeft of the text will be.
	 */
	public void setText(int layer, String text, Point topLeft) {
		TextImage timg = new TextImage(text);
		setLayer(layer, new PositionedImage(topLeft, timg));
	}

	/**
	 * Sets this layer to display text. Text will be positioned at the origin
	 * for the given layer.
	 * 
	 * @param layer
	 * @param text
	 */
	public void setText(int layer, String text) {
		LucyImage img = getLayer(layer).getImage();
		if (img instanceof TextImage) {
			TextImage timg = (TextImage) img;
			timg.setText(text);
		} else {
			setLayer(layer, new TextImage(text));
		}
	}

	/**
	 * Must be called to update animated sprites;
	 * 
	 * @param delta
	 */
	public void update(int delta) {
		layers.stream().filter(i -> i != null).map(i -> i.getImage()).filter(
				i -> i != null && i instanceof AnimatedImage).map(
						i -> (AnimatedImage) i).forEach(i -> i.update(delta));
	}

	/**
	 * Mirrors the image horizontally.
	 * 
	 * @param mirrored
	 */
	public void setMirrored(boolean mirrored) {
		if (this.mirrored != mirrored) {
			layers.stream().filter(i -> i != null).map(
					i -> i.getImage()).filter(i -> i != null).forEach(
							i -> i.setMirrored(mirrored));
			this.mirrored = mirrored;
		}
	}

	@Override
	public String toString() {
		List<PositionedImage> images = layers.subList(0, numLayers);
		return images.toString();
	}

	public void resetAnimations() {
		layers.stream().filter(i -> i != null).map(i -> i.getImage()).filter(
				i -> i != null && i instanceof AnimatedImage).map(
						i -> (AnimatedImage) i).forEach(
								i -> i.resetAnimation());
	}

	public void setLayers(LayeredImage limg) {
		numLayers = limg.numLayers;
		layers = limg.layers;
	}

	/**
	 * Sets the alpha of all layers. Note that layers later added to this
	 * LayeredImage will not inherit this alpha value.
	 * 
	 * @param alpha
	 */
	public void setAlpha(float alpha) {
		for (PositionedImage pimg : layers) {
			pimg.setAlpha(alpha);
		}
	}
}
