package objects;

import java.util.Collection;

import characters.Enemy;
import helpers.Point;
import helpers.Rectangle;
import images.ImageBuilder;
import images.LayeredImage;

public class AttackBox extends Attachment {
	private LayeredImage image;
	
	public AttackBox(Rectangle rect, Actor myActor) {
		super(rect, myActor);
	}

	public AttackBox(Point topLeft, float width, float height, Actor myActor) {
		this(new Rectangle(topLeft, width, height), myActor);
	}

	@Override
	public LayeredImage getImage() {
		if (image == null) {
			image = ImageBuilder.makeSensorImage(getRectangle());
		}

		return image;
	}

	public Collection<Enemy> getOverlappingEnemies() {
		Rectangle rect = getCoOrdTranslator().objectToWorldCoOrds(
				getRectangle());
		Class<Enemy> ec = Enemy.class;
		return getWorld().getOverlapping(rect, ec);
	}

}
