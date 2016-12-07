package objects;

import java.util.Collection;

import helpers.Point;
import helpers.Rectangle;
import images.ImageBuilder;
import images.LayeredImage;

public class Sensor extends Attachment {
	private LayeredImage image;

	public Sensor(Rectangle rect, Actor myActor) {
		super(rect, myActor);
	}

	public Sensor(Point topLeft, float width, float height, Actor myActor) {
		this(new Rectangle(topLeft, width, height), myActor);
	}

	@Override
	public LayeredImage getImage() {
		if (image == null) {
			image = ImageBuilder.makeSensorImage(getRectangle());
		}

		return image;
	}

	public boolean isOverlappingSolid() {
		Rectangle rect = getObject().getCoOrdTranslator().objectToWorldCoOrds(
				getRectangle());
		Collection<WorldObject> solids = getObject().getWorld().getOverlappingSolids(
				rect);
		return solids == null || !solids.isEmpty();
	}

}
