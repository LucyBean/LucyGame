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

	public AttackBox(Point topLeft, float width, float height) {
		this(new Rectangle(topLeft, width, height));
	}

	@Override
	public LayeredImage getImage() {
		if (image == null) {
			image = ImageBuilder.makeAttackBoxImage(getRectangle());
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
