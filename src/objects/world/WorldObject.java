package objects.world;

import java.util.Collection;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.GameObject;
import objects.attachments.ActorSticker;
import objects.attachments.Attachment;
import objects.attachments.AttachmentSet;
import objects.attachments.AttackBox;
import objects.attachments.Collider;
import objects.attachments.InteractBox;
import objects.attachments.Sensor;
import objects.images.Sprite;
import options.GlobalOptions;
import worlds.World;
import worlds.WorldLayer;

public abstract class WorldObject extends GameObject {
	//
	// Positioning
	// Position will be the object's position in the world. This should be set
	// to
	// the top-left point of the collider (for solids) or image (for
	// non-solids).
	//
	private ItemType itemType;
	private Collider collider;
	private InteractBox interactBox;
	private WorldLayer layer;
	private AttachmentSet attachments;

	/**
	 * Creates a new GameObject. Origin points for sprite, collider, and
	 * interact box should be set in object co-ords (i.e. relative to the
	 * GameObject's origin point).
	 * 
	 * @param origin
	 *            Top-left point of object in world co-ordinates. Should be set
	 *            to top-left of collider for solids or top-left of sprite for
	 *            non-solids.
	 * @param sprite
	 *            The image drawn in the world to represent the object.
	 * @param collider
	 *            Rectangle used for collision checking.
	 * @param interactBox
	 *            Rectangle used for interacting with the object.
	 */
	public WorldObject(Point origin, WorldLayer layer, ItemType itemType,
			Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, sprite);
		this.layer = layer;
		this.itemType = itemType;
		attachments = new AttachmentSet();
		attach(collider);
		attach(interactBox);

		reset();
	}

	public WorldObject(Point origin, WorldLayer layer, ItemType itemType,
			Sprite sprite) {
		this(origin, layer, itemType, sprite, null, null);
	}

	public WorldObject(Point origin, WorldLayer layer, ItemType itemType) {
		this(origin, layer, itemType, itemType.getSprite(), null, null);
	}

	protected void setColliderFromSprite() {
		if (getSprite() != null) {
			attach(new Collider(getSprite().getRectangle()));
		}
	}

	protected void setInteractBoxFromSprite() {
		if (getSprite() != null) {
			attach(new InteractBox(getSprite().getRectangle()));
		}
	}

	protected void attach(Attachment a) {
		if (a != null) {
			a.setObject(this);
			attachments.add(a);

			if (a instanceof Sprite) {
				setSprite((Sprite) a);
			} else if (a instanceof Collider) {
				setCollider((Collider) a);
			} else if (a instanceof InteractBox) {
				setInteractBox((InteractBox) a);
			} else if (a instanceof AttackBox) {
				((AttackBox) a).resetTargets();
			}
		}
	}

	protected void detach(Attachment a) {
		if (a != null) {
			a.setObject(null);

			if (a instanceof Collider) {
				setCollider(null);
			} else if (a instanceof InteractBox) {
				setInteractBox(null);
			} else {
				attachments.remove(a);
			}
		}
	}

	private void setCollider(Collider c) {
		if (collider != null) {
			attachments.remove(collider);
		}
		collider = c;
		if (collider != null) {
			attachments.add(collider);
		}
	}

	private void setInteractBox(InteractBox ib) {
		if (interactBox != null) {
			attachments.remove(interactBox);
		}
		interactBox = ib;
		if (interactBox != null) {
			attachments.add(interactBox);
		}
	}

	/**
	 * Resets an object to its initial state.
	 */
	public final void reset() {
		resetState();
	}

	protected abstract void resetState();

	// TODO
	// Getters
	//
	public boolean hasCollider() {
		return getCollider() != null;
	}

	public boolean isInteractable() {
		return getInteractBox() != null;
	}

	public Collider getCollider() {
		return collider;
	}

	public InteractBox getInteractBox() {
		return interactBox;
	}

	public WorldLayer getLayer() {
		return layer;
	}

	public ItemType getType() {
		return itemType;
	}

	protected Collection<ActorSticker> getActorStickers() {
		return attachments.getByType(ActorSticker.class);
	}

	/**
	 * Enables the object, causing it to to be rendered. Enabled Actors will
	 * receive updates.
	 */
	public void enable() {
		super.enable();
		if (getWorld() != null) {
			getWorld().addToActiveSets(this);
		}
		resetState();
	}

	/**
	 * Disables the object, preventing it from being rendered. Disabled Actors
	 * will not receive updates.
	 */
	public void disable() {
		super.disable();
		getWorld().removeFromActiveLists(this);
	}

	// TODO
	// Interaction and reactions
	//
	public void interactedBy(Actor a) {
		
	}

	/**
	 * This is called on an object when a WorldObject starts overlapping it.
	 * Override this to react to "on entry" events.
	 * 
	 * @param a
	 *            The WorldObject that has started overlapping this.
	 */
	public void overlapStart(WorldObject a) {
	}

	/**
	 * This is called on an object when the a WorldObject stops overlapping it.
	 * Override this to react to "on exit" events.
	 * 
	 * @param a
	 *            The WorldObject that has stopped overlapping this.
	 */
	public void overlapEnd(WorldObject a) {
	}

	//
	// Render
	//
	@Override
	public void draw() {
		// Draw the sprite
		super.draw();

		// Draws collider and interact boxes
		if (getCollider() != null) {
			if (GlobalOptions.drawAllColliders()
					|| GlobalOptions.drawInvisObjColliders()
							&& (getSprite() == null || !isVisible())) {
				getCollider().draw();
			}
		}
		if (getInteractBox() != null && GlobalOptions.drawInteractBoxes()) {
			getInteractBox().draw();
		}
		if (GlobalOptions.drawAttachments()) {
			Collection<Sensor> sensors = attachments.getByType(Sensor.class);
			sensors.stream().forEach(s -> s.draw());

			Collection<AttackBox> activeAttacks = attachments.getByType(
					AttackBox.class);
			activeAttacks.stream().forEach(s -> s.draw());

			Collection<ActorSticker> actorStickers = attachments.getByType(
					ActorSticker.class);
			actorStickers.stream().forEach(s -> s.draw());
		}
	}

	@Override
	public String toString() {
		if (isEnabled()) {
			return getClass().getSimpleName() + " at " + getPosition();
		} else {
			return getClass().getSimpleName();
		}
	}

	public int getKeyID() {
		return 0;
	}

	public int getLockID() {
		return 0;
	}

	public int getNPCID() {
		return 0;
	}

	@Override
	public void setWorld(World w) {
		super.setWorld(w);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		super.update(gc, delta);
		attachments.getByType(AttackBox.class).stream().forEach(
				a -> a.checkAttack());
	}
}
