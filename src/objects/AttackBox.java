package objects;

import java.util.Collection;

import characters.Enemy;
import helpers.Point;
import helpers.Rectangle;
import images.ImageBuilder;
import images.LayeredImage;

public class AttackBox extends Attachment {
	private LayeredImage image;

	public AttackBox(Rectangle rect) {
		super(rect, null);
	}

	public AttackBox(Rectangle rect, GameObject myObject) {
		super(rect, myObject);
	}

	public AttackBox(Point topLeft, float width, float height) {
		this(new Rectangle(topLeft, width, height));
	}

	public AttackBox(Point topLeft, float width, float height,
			GameObject myObject) {
		this(new Rectangle(topLeft, width, height), myObject);
	}

	@Override
	public LayeredImage getImage() {
		if (image == null) {
			image = ImageBuilder.makeAttackBoxImage(getRectangle());
		}

		return image;
	}

	public Collection<Enemy> getOverlappingEnemies() {
		Rectangle rect = getObject().getCoOrdTranslator().objectToWorldCoOrds(
				getRectangle());
		return getObject().getWorld().getMap().getOverlappingObjectsOfType(rect,
				Enemy.class);
	}

}
