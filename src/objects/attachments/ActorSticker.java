package objects.attachments;

import java.util.Collection;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import objects.GameObject;
import objects.images.ImageBuilder;
import objects.images.LayeredImage;
import objects.world.Actor;

public class ActorSticker extends Attachment {
	private LayeredImage image;
	
	public ActorSticker(Point topLeft, float width, float height,
			GameObject myObject) {
		this(new Rectangle(topLeft, width, height), myObject);
	}
	
	public ActorSticker(Point topLeft, float width, float height) {
		this(topLeft, width, height, null);
	}

	public ActorSticker(Rectangle rect, GameObject myObject) {
		super(rect, myObject);
	}
	
	public ActorSticker(Rectangle rect) {
		this(rect, null);
	}

	@Override
	public LayeredImage getImage() {
		if (image == null) {
			image = ImageBuilder.makeSensorImage(getRectangle());
		}

		return image;
	}
	
	public void moveStuckActors(Dir d, float amount) {
		Collection<Actor> actors = getOverlappingObjectsOfType(Actor.class);
		actors.stream().filter(a -> a != getObject()).forEach(a -> a.move(d, amount));
	}

}
