package objects;

import helpers.Point;
import helpers.Rectangle;
import images.ImageBuilder;
import images.LayeredImage;

public class Sensor extends Attachment {
	private LayeredImage image;

	public Sensor(Rectangle rect) {
		super(rect);
	}

	public Sensor(Point topLeft, float width, float height) {
		this(new Rectangle(topLeft, width, height));
	}

	@Override
	public LayeredImage getImage() {
		if (image == null) {
			image = ImageBuilder.makeSensorImage(getRectangle());
		}

		return image;
	}

}
