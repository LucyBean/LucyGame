package objects;

import java.util.Collection;

import helpers.Point;
import helpers.Rectangle;
import images.ImageBuilder;
import images.LayeredImage;
import worlds.World;

public class Sensor extends Attachment {
	private LayeredImage image;
	private CoOrdTranslator cot;
	private World world;

	public Sensor(Rectangle rect, Actor myActor) {
		super(rect);
		this.cot = myActor.getCoOrdTranslator();
		this.world = myActor.getWorld();
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
		Rectangle rect = cot.objectToWorldCoOrds(getRectangle());
		Collection<WorldObject> solids = world.getOverlappingSolids(rect);
		return solids == null || !solids.isEmpty();
	}

}
