package objects;

import java.util.Collection;
import java.util.HashSet;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import images.Sprite;
import images.SpriteBuilder;
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
	private Collection<Sensor> sensors;
	private Collection<AttackBox> activeAttacks;
	private WorldLayer layer;
	private Collection<Attachment> attachments;

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
		sensors = new HashSet<>();
		attachments = new HashSet<>();
		activeAttacks = new HashSet<>();
		attach(collider);
		attach(interactBox);

		reset();
	}

	public WorldObject(Point origin, WorldLayer layer, ItemType itemType,
			Sprite sprite) {
		this(origin, layer, itemType, sprite, null, null);
	}

	public WorldObject(Point origin, WorldLayer layer, ItemType itemType) {
		this(origin, layer, itemType, SpriteBuilder.getWorldItem(itemType),
				null, null);
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
			attachments.add(a);

			if (a instanceof Collider) {
				setCollider((Collider) a);
			}
			if (a instanceof InteractBox) {
				setInteractBox((InteractBox) a);
			}
			if (a instanceof Sensor) {
				addSensor((Sensor) a);
			}
			if (a instanceof AttackBox) {
				addAttackBox((AttackBox) a);
			}
		}
	}

	protected void detach(Attachment a) {
		if (a != null) {
			attachments.remove(a);
			if (a instanceof Collider) {
				setCollider(null);
			}
			if (a instanceof InteractBox) {
				setInteractBox(null);
			}
			if (a instanceof Sensor) {
				removeSensor((Sensor) a);
			}
			if (a instanceof AttackBox) {
				removeAttackBox((AttackBox) a);
			}
		}
	}

	private void setCollider(Collider c) {
		if (collider != null) {
			attachments.remove(collider);
		}
		collider = c;
		if (collider != null) {
			collider.setObject(this);
			attachments.add(collider);
		}
	}

	private void setInteractBox(InteractBox ib) {
		if (interactBox != null) {
			attachments.remove(interactBox);
		}
		interactBox = ib;
		if (interactBox != null) {
			interactBox.setObject(this);
			attachments.add(interactBox);
		}
	}

	private void addSensor(Sensor s) {
		if (s != null) {
			sensors.add(s);
			s.setObject(this);
			attachments.add(s);
		}
	}

	private void removeSensor(Sensor s) {
		if (s != null) {
			sensors.remove(s);
			attachments.remove(s);
		}
	}

	private void addAttackBox(AttackBox a) {
		if (a != null) {
			activeAttacks.add(a);
			a.setObject(this);
			attachments.add(a);
		}
	}

	private void removeAttackBox(AttackBox a) {
		if (a != null) {
			activeAttacks.remove(a);
			attachments.remove(a);
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
		if (GlobalOptions.drawSensors()) {
			sensors.stream().forEach(s -> s.draw());
		}
		if (GlobalOptions.drawAttackBoxes()) {
			activeAttacks.stream().forEach(s -> s.draw());
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
		activeAttacks.stream().forEach(a -> a.checkAttack());
	}
}
