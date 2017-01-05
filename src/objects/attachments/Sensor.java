package objects.attachments;

import java.util.Iterator;
import java.util.stream.Stream;

import helpers.Point;
import helpers.Rectangle;
import objects.images.ImageBuilder;
import objects.images.LayeredImage;
import objects.world.Actor;
import objects.world.WorldObject;

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
		Stream<WorldObject> solids = getObject().getWorld().getMap().getOverlappingSolids(
				rect);
		Iterator<WorldObject> is = solids.iterator();
		return solids == null || is.hasNext();
	}

	public <T extends WorldObject> boolean isOverlapping(Class<T> t) {
		Rectangle rect = getObject().getCoOrdTranslator().objectToWorldCoOrds(
				getRectangle());
		Stream<T> ts = getObject().getWorld().getMap().getOverlappingObjectsOfType(
				rect, t);
		return ts == null || ts.count() != 0;
	}

}
