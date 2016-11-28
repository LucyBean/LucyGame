package objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import images.Sprite;
import images.SpriteBuilder;
import images.StatedSprite;
import quests.EventInfo;
import quests.EventType;
import worlds.World;
import worlds.WorldLayer;

public abstract class Actor extends WorldObject {
	private Collection<WorldObject> activeInteractables;
	private Dir lastDirectionMoved;
	private final static float GRAVITY = 0.00005f;
	private final static float TERMINAL_FALL_VELOCITY = 0.5f;
	private float vSpeed;
	private float moveSpeed = 0.01f;
	private float walkSpeed = 0.5f;
	private boolean gravityEnabled;
	private ActorState lastState;
	private ActorState state;
	private Sensor floorAheadSensor;
	private Sensor floorSensor;
	private Sensor ceilingSensor;
	private Sensor wallAheadSensor;

	public Actor(Point origin, WorldLayer layer, ItemType itemType, Sprite sprite, Collider collider,
			InteractBox interactBox) {
		super(origin, layer, itemType, sprite, collider, interactBox);
		autoAlignSprite();
	}

	public Actor(Point origin, WorldLayer layer, ItemType itemType, Sprite sprite) {
		this(origin, layer, itemType, sprite, null, null);
	}

	public Actor(Point origin, WorldLayer layer, ItemType itemType) {
		this(origin, layer, itemType, SpriteBuilder.getWorldItem(itemType), null, null);
	}
	
	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		addSensors();
	}

	/**
	 * Sets the bottom-middle point of this Actor's Sprite to coincide with the
	 * bottom-middle point of its collider.
	 */
	private void autoAlignSprite() {
		if (getSprite() != null && getCollider() != null) {
			// Set position of Player's sprite such that bottom-middle points of
			// sprite and collider coincide.
			float newX = (getCollider().getWidth() - getSprite().getRectangle().getWidth()) / 2;
			float newY = (getCollider().getHeight() - getSprite().getRectangle().getHeight());
			getSprite().setOrigin(new Point(newX, newY));
		}
	}

	private void addSensors() {
		if (getCollider() != null) {
			float aheadSensorSize = 0.5f;
			float sensorSize = 0.05f;

			floorSensor = new Sensor(getCollider().getBottomLeft(), getCollider().getWidth(), sensorSize, this);
			ceilingSensor = new Sensor(getCollider().getTopLeft().move(Dir.NORTH, sensorSize), getCollider().getWidth(),
					sensorSize, this);
			floorAheadSensor = new Sensor(getCollider().getBottomRight(), getCollider().getWidth(), aheadSensorSize, this);
			wallAheadSensor = new Sensor(getCollider().getTopRight(), aheadSensorSize, getCollider().getHeight(), this);

			addSensor(floorSensor);
			addSensor(ceilingSensor);
			addSensor(floorAheadSensor);
			addSensor(wallAheadSensor);
		}
	}

	@Override
	protected void setColliderFromSprite() {
		super.setColliderFromSprite();
		addSensors();
	}

	protected final void resetState() {
		activeInteractables = new ArrayList<WorldObject>();
		vSpeed = 0.0f;
		gravityEnabled = true;
		lastState = ActorState.IDLE;
		state = ActorState.IDLE;
		resetActorState();
	}

	/**
	 * This should be used to set any initial state. This is called when the
	 * Actor is constructed and reset.
	 */
	protected abstract void resetActorState();

	//
	// Getters
	//
	public Dir getLastDirectionMoved() {
		return lastDirectionMoved;
	}

	public boolean gravityEnabled() {
		return gravityEnabled;
	}

	public ActorState getState() {
		return state;
	}

	//
	// Setters
	//
	public void useGravity(boolean gravity) {
		gravityEnabled = gravity;
	}

	/**
	 * Sets the move speed of the actor. The default value is 0.01f.
	 * 
	 * @param moveSpeed
	 */
	protected void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	private void setFloorSensorLocation(Dir d) {
		if (floorAheadSensor != null) {
			if (d == Dir.EAST) {
				floorAheadSensor.setOrigin(getCollider().getBottomRight());
				wallAheadSensor.setOrigin(getCollider().getTopRight());
			} else if (d == Dir.WEST) {
				floorAheadSensor.setOrigin(getCollider().getBottomLeft().move(Dir.WEST, floorAheadSensor.getWidth()));
				wallAheadSensor.setOrigin(getCollider().getTopLeft().move(Dir.WEST, wallAheadSensor.getWidth()));
			}
		}
	}

	public Sensor getFloorSensor() {
		return floorAheadSensor;
	}

	//
	// Movement
	//
	/**
	 * Moves in a direction at the walk speed. Updates the Actor state.
	 * 
	 * @param d
	 * @param delta
	 */
	public void walk(Dir d, int delta) {
		float moveAmount = moveSpeed * delta * walkSpeed;
		setFloorSensorLocation(d);

		if (floorAheadSensor.isOverlappingSolid()) {
			boolean moved = move(d, moveAmount);
			if (moved) {
				state = ActorState.WALK;
			}
		}
	}

	/**
	 * Sets the walk speed of this actor. This is measured relative to the run
	 * speed (e.g. walkSpeed of 0.5f means the Actor will walk half as fast as
	 * they run)
	 * 
	 * @param walkSpeed
	 */
	protected void setWalkSpeed(float walkSpeed) {
		this.walkSpeed = walkSpeed;
	}

	/**
	 * Moves in a direction at the run speed. Updates the Actor state.
	 * 
	 * @param d
	 * @param delta
	 */
	public void run(Dir d, int delta) {
		float moveAmount = moveSpeed * delta;
		setFloorSensorLocation(d);
		boolean moved = move(d, moveAmount);
		if (moved) {
			state = ActorState.RUN;
		}
	}

	/**
	 * Moves a direction, preventing this Actor's Collider from overlapping with
	 * any other Colliders. Does not set ActorState.
	 * 
	 * @param d
	 * @param amount
	 * @return Whether or not the character actually moved.
	 */
	public boolean move(Dir d, float amount) {
		if (d == Dir.EAST) {
			getSprite().setMirrored(false);
		} else if (d == Dir.WEST) {
			getSprite().setMirrored(true);
		}

		if (getCollider() == null) {
			setPosition(getPosition().move(d, amount));
			lastDirectionMoved = d;
			return true;
		} else {
			Point newPos = findNewPosition(d, amount);
			Point oldPos = getPosition();
			setPosition(newPos);
			if (newPos.equals(oldPos)) {
				return false;
			} else {
				lastDirectionMoved = d;
				return true;
			}
		}
	}

	/**
	 * Calculates the rectangle through which the object will move, assuming
	 * there are no collisions.
	 * 
	 * @param d
	 *            Direction in which to move.
	 * @param amount
	 *            Amount by which to move.
	 * @return The area through which the Actor moves, assuming no collisions
	 */
	private Rectangle calculateMoveArea(Dir d, float amount) {
		// Reverse the direction if moving a negative amount
		if (amount < 0) {
			d = d.neg();
			amount = -amount;
		}

		// Calculate the whole area through which the Actor moves excluding its
		// original box
		Point origin;
		// move the origin for NORTH and WEST
		if (d == Dir.NORTH || d == Dir.WEST) {
			origin = getCollider().getTopLeft().move(d, amount);
		}
		// set origin to bottom left for SOUTH
		else if (d == Dir.SOUTH) {
			origin = getCollider().getBottomLeft();
		}
		// set origin to top right for EAST
		else {
			origin = getCollider().getTopRight();
		}

		float width;
		float height;
		if (d == Dir.NORTH || d == Dir.SOUTH) {
			width = getCollider().getWidth();
			height = amount;
		} else {
			width = amount;
			height = getCollider().getHeight();
		}

		return new Rectangle(origin, width, height);
	}

	/**
	 * Finds the Colliders that are solid that overlap with the given rectangle.
	 * 
	 * @param rect
	 *            The rectangle to check in the Actor's co-ordinates.
	 * @return
	 */
	protected Collection<WorldObject> getOverlappingSolids(Rectangle rect) {
		rect = getCoOrdTranslator().objectToWorldCoOrds(rect);
		Collection<WorldObject> solids = getWorld().getOverlappingSolids(rect);
		return solids;
	}

	/**
	 * Checks for collisions to determine the new position of the object.
	 * 
	 * @return The object's new position
	 */
	private Point findNewPosition(Dir d, float amount) {
		// flip direction and amount if amount is negative
		if (amount < 0) {
			d = d.neg();
			amount = -amount;
		}
		Rectangle moveArea = calculateMoveArea(d, amount);
		Collection<WorldObject> activeSolids = getOverlappingSolids(moveArea);

		// If there are no active solids, move to that position immediately.
		if (activeSolids.isEmpty()) {
			return getPosition().move(d.asPoint().scale(amount));
		}
		// Else move to edge of the d.neg()-most point
		else {
			float toMove = 0.0f;
			Iterator<WorldObject> asi = activeSolids.iterator();
			WorldObject go;
			switch (d) {
			case NORTH: {
				go = asi.next();
				float maxSouth = go.getCollider().getBottomLeft().move(go.getPosition()).getY();
				while (asi.hasNext()) {
					go = asi.next();
					Point bl = go.getCollider().getBottomLeft().move(go.getPosition());
					// If this is WEST-most object set as new origin
					if (bl.getY() > maxSouth) {
						maxSouth = bl.getY();
					}
				}
				toMove = getPosition().getY() - maxSouth;
				break;

			}

			case SOUTH: {
				go = asi.next();
				float maxNorth = go.getCollider().getTopLeft().move(go.getPosition()).getY();
				while (asi.hasNext()) {
					go = asi.next();
					Point tl = go.getCollider().getTopLeft().move(go.getPosition());
					// If this is WEST-most object set as new origin
					if (tl.getY() < maxNorth) {
						maxNorth = tl.getY();
					}
				}
				toMove = maxNorth - getPosition().getY() - getCollider().getHeight();
				break;
			}

			case EAST: {
				go = asi.next();
				float maxWest = go.getCollider().getTopLeft().move(go.getPosition()).getX();
				while (asi.hasNext()) {
					go = asi.next();
					Point tl = go.getCollider().getTopLeft().move(go.getPosition());
					// If this is WEST-most object set as new origin
					if (tl.getX() < maxWest) {
						maxWest = tl.getX();
					}
				}
				toMove = maxWest - getPosition().getX() - getCollider().getWidth();
				break;
			}

			case WEST: {
				go = asi.next();
				float maxEast = go.getCollider().getTopRight().move(go.getPosition()).getX();
				while (asi.hasNext()) {
					go = asi.next();
					Point tr = go.getCollider().getTopRight().move(go.getPosition());
					// If this is WEST-most object set as new origin
					if (tr.getX() > maxEast) {
						maxEast = tr.getX();
					}
				}
				toMove = getPosition().getX() - maxEast;
				break;
			}
			}

			return getPosition().move(d, toMove);
		}
	}

	private Collection<WorldObject> findInteractablesHere() {
		// Check for any interactables that are at the Actor's current position
		Rectangle thisArea = getCollider().getRectangle().translate(getPosition());
		Collection<WorldObject> interactables = getWorld().getAllInteractables();
		Collection<WorldObject> nowActive = new ArrayList<WorldObject>();
		interactables.stream().filter(go -> go != this && go.isEnabled())
				.filter(go -> go.getInteractBox().getRectangle().translate(go.getPosition()).overlaps(thisArea))
				.forEach(go -> nowActive.add(go));
		return nowActive;
	}

	/**
	 * Returns the list (a - b) (i.e. all elements in a that are not in b). This
	 * will not modify the original lists.
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	private <T> Collection<T> subtract(Collection<T> a, Collection<T> b) {
		Collection<T> toReturn = new HashSet<T>(a);
		toReturn.removeAll(b);
		return toReturn;
	}

	private void checkForInteractions() {
		if (getCollider() != null) {
			Collection<WorldObject> nowActive = findInteractablesHere();
			Collection<WorldObject> prevActive = activeInteractables;
			// Figure out the objects that are newly active/inactive
			// activeInteractables is the list of interactables active last
			// iteration
			// nowActive is the list of interactables active this iteration
			Collection<WorldObject> newlyActive = subtract(nowActive, prevActive);
			Collection<WorldObject> newlyInactive = subtract(prevActive, nowActive);

			// Call overlapStart and overlapEnd on these newly active/inactive
			// objects
			newlyActive.stream().forEach(go -> overlapStart(go));
			newlyActive.stream().filter(go -> !(go instanceof Actor)).forEach(go -> go.overlapStart(this));
			newlyInactive.stream().forEach(go -> overlapEnd(go));
			newlyInactive.stream().filter(go -> !(go instanceof Actor)).forEach(go -> go.overlapEnd(this));

			activeInteractables = nowActive;
		}
	}

	//
	// #JustGravityThings
	//
	/**
	 * Checks whether this Actor is on the floor.
	 * 
	 * @return
	 */
	protected boolean isOnGround() {
		if (getCollider() == null) {
			return false;
		} else {
			return floorSensor.isOverlappingSolid();
		}
	}

	/**
	 * Checks whether this Actor has hit the ceiling.
	 * 
	 * @return
	 */
	protected boolean isOnCeiling() {
		if (getCollider() == null) {
			return false;
		} else {
			return ceilingSensor.isOverlappingSolid();
		}
	}

	private void calculateVSpeed(int delta) {
		if (gravityEnabled()) {
			// If player is on the ground and is falling, stop them.
			if (isOnGround() && vSpeed >= 0.0f) {
				vSpeed = 0.0f;
			} else if (isOnCeiling() && vSpeed < 0.0f) {
				vSpeed = 0.0f;
			} else {
				vSpeed += GRAVITY * delta;
				vSpeed = Math.min(TERMINAL_FALL_VELOCITY, vSpeed);
				if (vSpeed > 0) {
					state = ActorState.FALL;
				} else {
					state = ActorState.JUMP;
				}
			}
		}
	}

	protected void jump(float strength) {
		if (gravityEnabled() && isOnGround()) {
			vSpeed = -strength;
		}
	}

	// TODO
	// Event reactions
	//
	/*
	 * added to world, on created, on collision, on interact
	 */
	public void onMouseDown() {

	}

	public void onClick() {

	}

	//
	// Update
	//
	final public void update(GameContainer gc, int delta) {
		super.update(gc, delta);
		if (isEnabled()) {
			state = ActorState.IDLE;
			act(gc, delta);
			checkForInteractions();

			if (gravityEnabled()) {
				calculateVSpeed(delta);
				move(Dir.SOUTH, vSpeed * delta);
			}
		}

		if (lastState != state) {
			stateChanged(lastState, state);
		}

		lastState = state;
	}

	public abstract void act(GameContainer gc, int delta);

	protected void setState(ActorState newState) {
		state = newState;
	}

	public void stateChanged(ActorState from, ActorState to) {
		// Updated sprite according to Actor's state
		if (getSprite() instanceof StatedSprite) {
			((StatedSprite) getSprite()).setState(state.ordinal());
		}
	}

	@Override
	public void statedSpriteImageChange() {
		autoAlignSprite();
	}

	public void interactWithAll() {
		if (activeInteractables.isEmpty()) {
		} else {
			Iterator<WorldObject> aii = activeInteractables.iterator();
			while (aii.hasNext()) {
				WorldObject go = aii.next();
				go.interactedBy(this);
				getWorld().signalEvent(new EventInfo(EventType.INTERACT, this, go));
			}
		}
	}
}
