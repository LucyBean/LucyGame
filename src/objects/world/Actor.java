package objects.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.newdawn.slick.GameContainer;

import helpers.Dir;
import helpers.Point;
import helpers.Rectangle;
import objects.attachments.Collider;
import objects.attachments.InteractBox;
import objects.attachments.Sensor;
import objects.images.Sprite;
import objects.images.StatedSprite;
import quests.EventInfo;
import quests.EventType;
import worlds.World;
import worlds.WorldLayer;

public abstract class Actor extends WorldObject {
	private Collection<WorldObject> activeInteractables;
	private Dir facing;
	private final static float GRAVITY = 0.00005f;
	private final static float TERMINAL_FALL_VELOCITY = 0.5f;
	private final static float TERMINAL_WALL_SLIDE_VELOCITY = 0.005f;
	private final static float FEET_COLLISION_THRESHOLD = 0.01f;
	private float vSpeed;
	private float jumpHSpeed;
	private float velocityExp = 0.0f;
	private float moveSpeed = 0.01f;
	private float walkSpeed = 0.5f;
	private float crouchSpeed = 0.7f;
	private float pushSpeed = 0.4f;
	private float defaultJumpStrength = 0.013f;
	private float nextJumpStrength = defaultJumpStrength;
	private boolean gravityEnabled = true;
	private boolean pushable = false;
	private ActorState lastState;
	private ActorState state;
	private Sensor floorAheadSensor;
	private Sensor floorSensor;
	private Sensor ceilingSensor;
	private Sensor wallAheadSensorTop;
	private Sensor wallAheadSensorBtm;
	private boolean canMidAirJump = true;
	private boolean jumpNextFrame = false;
	private boolean canJumpSustain = false;
	private boolean jumpSustainThisFrame = false;
	private Point positionDelta;
	private Collider standingCollider;
	private Collider crouchingCollider;
	private boolean interactNextFrame;
	private Actor pushTarget;
	private Collection<WorldObject> solidsToIgnoreThisFrame;

	public Actor(Point origin, WorldLayer layer, ItemType itemType,
			Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, layer, itemType, sprite, collider, interactBox);
		autoAlignSprite();
		standingCollider = collider;
		if (collider != null) {
			Point ccOrigin = collider.getTopLeft().move(Dir.SOUTH,
					collider.getHeight() / 2);
			float ccWidth = collider.getWidth();
			float ccHeight = collider.getHeight() / 2;
			crouchingCollider = new Collider(ccOrigin, ccWidth, ccHeight);
		}
	}

	public Actor(Point origin, WorldLayer layer, ItemType itemType,
			Sprite sprite) {
		this(origin, layer, itemType, sprite, null, null);
	}

	public Actor(Point origin, WorldLayer layer, ItemType itemType) {
		this(origin, layer, itemType, itemType.getSprite(), null, null);
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
			Point co = getCollider().getTopLeft();
			float newX = (getCollider().getWidth()
					- getSprite().getRectangle().getWidth()) / 2 + co.getX();
			float newY = (getCollider().getHeight()
					- getSprite().getRectangle().getHeight()) + co.getY();
			getSprite().setOrigin(new Point(newX, newY));
		}
	}

	private void addSensors() {
		if (getCollider() != null) {
			float sensorSize = 0.05f;
			float wallSensorHeight = 0.2f;

			floorSensor = new Sensor(getCollider().getBottomLeft(),
					getCollider().getWidth(), sensorSize, this);
			ceilingSensor = new Sensor(
					getCollider().getTopLeft().move(Dir.NORTH, sensorSize),
					getCollider().getWidth(), sensorSize, this);
			floorAheadSensor = new Sensor(getCollider().getBottomRight(),
					getCollider().getWidth(), sensorSize, this);
			wallAheadSensorTop = new Sensor(getCollider().getTopRight(),
					sensorSize, wallSensorHeight, this);
			wallAheadSensorBtm = new Sensor(
					getCollider().getBottomRight().move(Dir.NORTH,
							wallSensorHeight),
					sensorSize, wallSensorHeight, this);

			attach(floorSensor);
			attach(ceilingSensor);
			attach(floorAheadSensor);
			attach(wallAheadSensorTop);
			attach(wallAheadSensorBtm);
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
		jumpHSpeed = 0.0f;
		jumpNextFrame = false;
		positionDelta = Point.ZERO;
		lastState = ActorState.IDLE;
		setState(ActorState.IDLE);
		resetActorState();
	}

	/**
	 * This should be used to set any initial state. This is called when the
	 * Actor is constructed and reset.
	 */
	protected abstract void resetActorState();

	public boolean gravityEnabled() {
		return gravityEnabled;
	}

	public void setPushable(boolean pushable) {
		this.pushable = pushable;
	}

	/**
	 * Returns the state of the Actor at the end of the previous frame.
	 * 
	 * @return
	 */
	public ActorState getState() {
		return lastState;
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

	private void setAheadSensorLocation(Dir d) {
		if (floorAheadSensor != null) {
			if (d == Dir.EAST) {
				floorAheadSensor.setOrigin(getCollider().getBottomRight());
				wallAheadSensorTop.setOrigin(getCollider().getTopRight());
				wallAheadSensorBtm.setOrigin(
						getCollider().getBottomRight().move(Dir.NORTH,
								wallAheadSensorBtm.getHeight()));
			} else if (d == Dir.WEST) {
				floorAheadSensor.setOrigin(getCollider().getBottomLeft().move(
						Dir.WEST, floorAheadSensor.getWidth()));
				wallAheadSensorTop.setOrigin(getCollider().getTopLeft().move(
						Dir.WEST, wallAheadSensorTop.getWidth()));
				wallAheadSensorBtm.setOrigin(getCollider().getTopLeft().move(
						Dir.WEST, wallAheadSensorBtm.getWidth()).move(Dir.NORTH,
								wallAheadSensorBtm.getHeight()));
			}
		}
	}

	public Sensor getFloorSensor() {
		return floorAheadSensor;
	}

	/**
	 * @return the facing
	 */
	private Dir getFacing() {
		return facing;
	}

	/**
	 * @param facing
	 *            the facing to set
	 */
	private void setFacing(Dir facing) {
		this.facing = facing;
		setAheadSensorLocation(facing);
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

	public Actor getPushTarget() {
		return pushTarget;
	}

	public float getVSpeed() {
		return vSpeed;
	}

	/**
	 * Causes this solid to be ignored by the Actor's collision checking until
	 * the end of the frame.
	 * 
	 * @param wo
	 */
	private void ignoreSolid(WorldObject wo) {
		solidsToIgnoreThisFrame.add(wo);
	}
	
	/**
	 * Gets solids that should be ignored when collision checking
	 * @return
	 */
	private Collection<WorldObject> getIgnoredSolids() {
		Collection<WorldObject> solids = new HashSet<>();
		solids.addAll(solidsToIgnoreThisFrame);
		getOverlappingSolids(getCollider().getRectangle()).forEach(
				s -> solids.add(s));
		return solids;
	}

	/**
	 * Moves a direction, preventing this Actor's Collider from overlapping with
	 * any other Colliders. Does not set ActorState.
	 * 
	 * @param d
	 * @param amount
	 * @return The difference in position
	 */
	public Point move(Dir d, float amount) {
		if (d == null) {
			return Point.ZERO;
		}

		if (Math.abs(amount) > 0 && d == Dir.EAST || d == Dir.WEST) {
			if (amount >= 0) {
				setFacing(d);
			} else {
				setFacing(d.neg());
			}
			setAheadSensorLocation(getFacing());
		}

		if (getFacing() == Dir.EAST) {
			getSprite().setMirrored(false);
		} else if (getFacing() == Dir.WEST) {
			getSprite().setMirrored(true);
		}

		Point delta = Point.ZERO;

		if (getCollider() == null) {
			// This object has no collider so moves immediately without
			// collision checking
			setPosition(getPosition().move(d, amount));
			getActorStickers().stream().forEach(
					a -> a.moveStuckActors(d, amount));
			delta = Point.ZERO.move(d, amount);
			positionDelta = positionDelta.move(d, amount);
		} else {
			// This object moves with collision checking
			Point newPos = findNewPosition(d, amount);
			Point oldPos = getPosition();
			delta = newPos.move(oldPos.neg());
			positionDelta = positionDelta.move(delta);
			setPosition(newPos);

			if (delta.equals(Point.ZERO)) {
				// If delta is small/zero, set it to zero Point
				delta = Point.ZERO;
			} else {
				// Move all stuck actors by the same amount
				float moveAmount = delta.getDir(d);
				getActorStickers().stream().forEach(
						a -> a.moveStuckActors(d, moveAmount));

				if (d == Dir.EAST || d == Dir.WEST) {
					setFacing(d);
				}
			}
		}

		return delta;
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
	protected Stream<WorldObject> getOverlappingSolids(Rectangle rect) {
		rect = getCoOrdTranslator().objectToWorldCoOrds(rect);
		Stream<WorldObject> solids = getWorld().getMap().getOverlappingSolids(
				rect);
		solids = solids.filter(s -> s != this);
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
		Collection<WorldObject> solidsToCheck = getOverlappingSolids(
				moveArea).collect(Collectors.toSet());
		// Add the solids that are already overlapping to the ignore set
		Collection<WorldObject> solidsToIgnore = getIgnoredSolids();

		// If moving left/right allow the Actor to 'climb' onto solids which
		// have a
		// small "feet collision". The Actor will be nudged NORTH slightly in
		// order
		// to avoid colliding with these solids.
		if (d == Dir.EAST || d == Dir.WEST) {
			float northNudge = 0.0f;
			// feet collisions are one in which the vertically colliding
			// distance is small
			Iterator<WorldObject> osi = solidsToCheck.iterator();
			while (osi.hasNext()) {
				WorldObject solid = osi.next();
				float actorBtm = getCoOrdTranslator().objectToWorldCoOrds(
						getCollider().getBottomLeft()).getY();
				float solidTop = solid.getCoOrdTranslator().objectToWorldCoOrds(
						solid.getCollider().getTopLeft()).getY();
				float overlap = actorBtm - solidTop + 0.0001f;
				if (overlap > 0 && overlap < FEET_COLLISION_THRESHOLD) {
					northNudge = Math.max(overlap, northNudge);
				}
			}
			if (northNudge > 0) {
				northNudge += 0.0001f;
				move(Dir.NORTH, northNudge);
				moveArea = calculateMoveArea(d, amount);
				solidsToCheck = getOverlappingSolids(moveArea).collect(
						Collectors.toSet());
			}
		}

		// Remove all solids that are already overlapping
		solidsToCheck = solidsToCheck.stream().filter(
				s -> !solidsToIgnore.contains(s)).collect(Collectors.toSet());

		// If there are no active solids, move to that position immediately.
		if (solidsToCheck.isEmpty()) {
			return getPosition().move(d.asPoint().scale(amount));
		}
		// Else move to edge of the d.neg()-most point
		else {
			float toMove = 0.0f;
			Iterator<WorldObject> asi = solidsToCheck.iterator();
			WorldObject go;
			switch (d) {
				case NORTH: {
					go = asi.next();
					float maxSouth = go.getCollider().getBottomLeft().move(
							go.getPosition()).getY();
					while (asi.hasNext()) {
						go = asi.next();
						Point bl = go.getCollider().getBottomLeft().move(
								go.getPosition());
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
					float maxNorth = go.getCollider().getTopLeft().move(
							go.getPosition()).getY();
					while (asi.hasNext()) {
						go = asi.next();
						Point tl = go.getCollider().getTopLeft().move(
								go.getPosition());
						// If this is WEST-most object set as new origin
						if (tl.getY() < maxNorth) {
							maxNorth = tl.getY();
						}
					}
					toMove = maxNorth - getPosition().getY()
							- getCollider().getHeight();
					break;
				}

				case EAST: {
					go = asi.next();
					float maxWest = go.getCollider().getTopLeft().move(
							go.getPosition()).getX();
					while (asi.hasNext()) {
						go = asi.next();
						Point tl = go.getCollider().getTopLeft().move(
								go.getPosition());
						// If this is WEST-most object set as new origin
						if (tl.getX() < maxWest) {
							maxWest = tl.getX();
						}
					}
					toMove = maxWest - getPosition().getX()
							- getCollider().getWidth();
					break;
				}

				case WEST: {
					go = asi.next();
					float maxEast = go.getCollider().getTopRight().move(
							go.getPosition()).getX();
					while (asi.hasNext()) {
						go = asi.next();
						Point tr = go.getCollider().getTopRight().move(
								go.getPosition());
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
		// If this Actor is pushing something then make it push rather than walk
		if (wasPushing()) {
			push(d, delta);
			return;
		}

		if (isOnGround()) {
			setAheadSensorLocation(d);
			float moveAmount = moveSpeed * delta * walkSpeed;
			if (wasCrouching()) {
				moveAmount *= crouchSpeed;
			}
			if (floorAheadSensor.isOverlappingSolid()) {
				Point moved = move(d, moveAmount);
				if (moved != Point.ZERO && isOnGround()) {
					setState(ActorState.WALK);
				}
			}
		}
	}

	/**
	 * Moves in a direction at the run speed. Updates the Actor state.
	 * 
	 * @param d
	 * @param delta
	 */
	public void run(Dir d, int delta) {
		// If this Actor is pushing something then make it push rather than run
		if (wasPushing()) {
			push(d, delta);
			return;
		}

		float moveAmount = moveSpeed * delta;

		// Reduce the 'run speed' while crouching
		if (wasCrouching()) {
			moveAmount *= crouchSpeed;
		}

		// Reduce the 'run speed' while falling and jumping
		if (lastState == ActorState.FALL || lastState == ActorState.JUMP) {
			moveAmount *= 0.3f;
		}

		Point moved = move(d, moveAmount);
		if (isOnGround() && moved != Point.ZERO) {
			setState(ActorState.RUN);
			// If they're running 'off an edge' then make them fall through the
			// block by ignoring all collisions with WorldObjects overlapping
			// their feet
			if (!floorAheadSensor.isOverlappingSolid()) {
				Collection<WorldObject> groundBlocks = floorSensor.getOverlappingSolids().collect(
						Collectors.toSet());
				groundBlocks.forEach(wo -> ignoreSolid(wo));
				// Make them fall off the edge
				setState(ActorState.FALL);
			}
		}

		if (!isOnGround() && canWallSlide(d)) {
			// Start wall sliding if this Actor moves towards the wall
			setState(ActorState.WALL_SLIDE);
		}

		if (canClimb(d)) {
			setState(ActorState.CLIMB);
		}
	}

	public Point bePushed(Dir d, float amount) {
		// Objects cannot be pushed off edges
		setAheadSensorLocation(d);
		// if (floorAheadSensor.isOverlappingSolid()) {
		Point moved = move(d, amount);
		return moved;
		// }
		// return Point.ZERO;
	}

	/**
	 * Causes this Actor to push. This will move their pushTarget and themself
	 * in the given direction.
	 * 
	 * @param d
	 * @param delta
	 */
	private void push(Dir d, int delta) {
		setAheadSensorLocation(d);
		// Figure out whether to push or pull the pushTarget
		if (pushTarget != null && (d == Dir.EAST || d == Dir.WEST)) {
			// Determine whether pushing or pulling
			// deltaX > 0 means target is EAST of this Actor
			float deltaX = pushTarget.getPosition().getX()
					- getPosition().getX();
			boolean pushing = (d == Dir.EAST && deltaX > 0)
					|| (d == Dir.WEST && deltaX < 0);

			// Can only pull if there is no wall in the way
			if (pushing || (!wallAheadSensorTop.isOverlappingSolid()
					&& !wallAheadSensorBtm.isOverlappingSolid()
					&& floorAheadSensor.isOverlappingSolid())) {
				float moveAmount = moveSpeed * delta * pushSpeed;
				Point posDelta = Point.ZERO;
				if (pushing) {
					// If pushing move the push target first
					posDelta = pushTarget.bePushed(d, moveAmount);
					// Then move the player to it
					move(d, posDelta.getDir(d));
				} else {
					// If pulling, move the player than move the target
					// the same amount
					posDelta = move(d, moveAmount);
					pushTarget.bePushed(d, posDelta.getDir(d));
				}
				// Set state as necessary
				if (posDelta != Point.ZERO) {
					if (pushing) {
						setState(ActorState.PUSH);
					} else {
						setState(ActorState.PULL);
					}
				}
			}
		}
	}

	/**
	 * Moves the Actor up or down at the climbing speed. This does not check
	 * whether this Actor's state is CLIMB.
	 * 
	 * @param d
	 *            The direction to move (EAST and WEST have no effect)
	 * @param delta
	 */
	public void climb(Dir d, int delta) {
		if (d == Dir.NORTH || d == Dir.SOUTH) {
			float moveAmount = moveSpeed * delta * walkSpeed;
			Point moved = move(d, moveAmount);
			if (moved != Point.ZERO) {
				// TODO: Animate climbing sprite
			}
			if (!wallAheadSensorTop.isOverlapping(ClimbingWallMarker.class)) {
				// This Actor has now climbed to the top of the this
				// ClimbingWallMarker.
				// Set them to the top of the wall by setting the bottom left of
				// the
				// player's collider to the location of the wallAheadSensorTop's
				// bottom left co-ordinate
				Point btmLeft = getCoOrdTranslator().objectToWorldCoOrds(
						wallAheadSensorTop.getBottomLeft());
				Point topLeft = btmLeft.move(Dir.NORTH,
						getCollider().getHeight());
				setPosition(topLeft);

			}
		}
	}

	/**
	 * Sends a signal to the Actor to jump at the end of the next frame.
	 * 
	 * @param strength
	 */
	public void signalJump() {
		jumpNextFrame = true;
	}

	/**
	 * Sends a signal to the Actor to jump sustain this frame (if possible).
	 */
	public void signalJumpSustain() {
		jumpSustainThisFrame = true;
	}

	/**
	 * Sends a signal to the Actor to interact with all objects at the end of
	 * the next frame.
	 */
	public void signalInteract() {
		interactNextFrame = true;
	}

	/**
	 * Signals a jump that is of a relative strength to a standard jump.
	 * 
	 * @param nextJumpStrengthRelative
	 */
	public void signalJumpRelative(float nextJumpStrengthRelative) {
		this.nextJumpStrength = defaultJumpStrength * nextJumpStrengthRelative;
		signalJump();
	}

	public void signalJumpAbs(float nextJumpStrength) {
		this.nextJumpStrength = nextJumpStrength;
		signalJump();
	}

	/**
	 * @return Whether the Actor was crouching in the previous frame.
	 */
	private boolean wasCrouching() {
		ActorState as = getState();
		return as == ActorState.CROUCH || as == ActorState.CROUCH_WALK;
	}

	private boolean wasPushing() {
		ActorState as = getState();
		return as == ActorState.PUSH_PULL_IDLE || as == ActorState.PUSH
				|| as == ActorState.PULL;
	}

	/**
	 * Starts the Actor crouching
	 */
	protected void startCrouch() {
		setState(ActorState.CROUCH);
	}

	public void startPushing(Actor pb) {
		setState(ActorState.PUSH_PULL_IDLE);
		pushTarget = pb;
	}

	public void resetMidAirJump() {
		canMidAirJump = true;
	}

	private void jump(int delta) {
		if (gravityEnabled()) {
			if (isOnGround()) {
				// If on ground then single jump
				// in the direction of travel
				vSpeed = -nextJumpStrength;
				nextJumpStrength = defaultJumpStrength;
				jumpHSpeed = velocityExp * 0.8f;
				canJumpSustain = true;
			} else if (lastState == ActorState.WALL_SLIDE
					|| lastState == ActorState.CLIMB) {
				// If wall sliding or climbing then single jump
				// away from the wall
				vSpeed = -nextJumpStrength * 1.3f;
				nextJumpStrength = defaultJumpStrength;
				setFacing(getFacing().neg());
				jumpHSpeed = moveSpeed * 0.7f;
				if (getFacing() == Dir.WEST) {
					jumpHSpeed *= -1;
				}
			} else if (canMidAirJump && vSpeed > -0.1f) {
				// If falling then mid-air jump
				vSpeed = -nextJumpStrength * 0.8f;
				nextJumpStrength = defaultJumpStrength;
				canMidAirJump = false;
				canJumpSustain = true;
			}
		}
	}

	/**
	 * Calculates the new vertical speed of the object as if gravity were acting
	 * on it
	 * 
	 * @param delta
	 */
	private void calculateVSpeed(int delta) {
		if (isOnGround() && vSpeed >= 0) {
			// If player is on the ground and is falling, stop them.
			// Also reset mid-air jump ability
			vSpeed = 0.0f;
			resetMidAirJump();
		} else if (isOnCeiling() && vSpeed < 0) {
			// Stop jumping if touching the ceiling
			vSpeed = 0.0f;
		} else if (state == ActorState.WALL_SLIDE) {
			// Fall at wall slide speed
			vSpeed += GRAVITY * delta;
			vSpeed = Math.min(TERMINAL_WALL_SLIDE_VELOCITY, vSpeed);
		} else {
			// Fall
			vSpeed += GRAVITY * delta;

			// Cause jump sustain by reducing the effect of gravity
			if (vSpeed < 0 && canJumpSustain) {
				if (jumpSustainThisFrame) {
					vSpeed -= GRAVITY * delta * 0.5f;
				} else {
					canJumpSustain = false;
				}
			}
			vSpeed = Math.min(TERMINAL_FALL_VELOCITY, vSpeed);
			if (vSpeed > 0) {
				setState(ActorState.FALL);
			} else {
				setState(ActorState.JUMP);
			}
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
			// Find all solids that are not being ignored
			Collection<WorldObject> solidsToIgnore = getIgnoredSolids();
			Stream<WorldObject> floorSolids = floorSensor.getOverlappingSolids().filter(
					s -> !solidsToIgnore.contains(s));
			long count = floorSolids.count();
			return count != 0;
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

	private boolean canWallSlide(Dir d) {
		return getCollider() != null && vSpeed > 0
				&& (d == Dir.EAST || d == Dir.WEST)
				&& wallAheadSensorTop.isOverlappingSolid()
				&& wallAheadSensorBtm.isOverlappingSolid();
	}

	private boolean canClimb(Dir d) {
		return (getCollider() != null && d == Dir.EAST || d == Dir.WEST)
				&& wallAheadSensorTop.isOverlapping(ClimbingWallMarker.class)
				&& wallAheadSensorBtm.isOverlapping(ClimbingWallMarker.class);
	}

	/**
	 * Checks whether the Actor should be Wall Sliding
	 */
	private void checkWallSlide() {
		// Check for continuing a wall slide
		// This happens when the Actor was previously wall sliding
		// and has not since tried to move away from the wall
		Dir d = getFacing();
		if (lastState == ActorState.WALL_SLIDE && canWallSlide(d)) {
			setState(ActorState.WALL_SLIDE);
		}
	}

	/**
	 * Checks whether the Actor should be Climbing
	 */
	private void checkClimb() {
		// Check for continuing a climb
		// This happens when the Actor was previously climbing
		// and has not since tried to move away from the wall
		Dir d = getFacing();
		if (lastState == ActorState.CLIMB && canClimb(d)) {
			setState(ActorState.CLIMB);
		}
	}

	/**
	 * Checks whether the Actor should be Pushing
	 */
	private void checkPush() {
		// Check for continuing a push
		// This happens when the Actor was previous pushing and
		// their push target has not started falling.
		if (wasPushing() && pushTarget != null
				&& pushTarget.getState() != ActorState.FALL) {
			setState(ActorState.PUSH_PULL_IDLE);
		}

	}

	private Collection<WorldObject> findInteractablesHere() {
		// Check for any interactables that are at the Actor's current position
		Rectangle thisArea = getCollider().getRectangle().translate(
				getPosition());
		Stream<WorldObject> interactables = getWorld().getMap().getAllInteractables();
		Collection<WorldObject> nowActive = new ArrayList<WorldObject>();
		interactables.filter(go -> go != this && go.isEnabled()).filter(
				go -> go.getInteractBox().getRectangle().translate(
						go.getPosition()).overlaps(thisArea)).forEach(
								go -> nowActive.add(go));
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
			Collection<WorldObject> newlyActive = subtract(nowActive,
					prevActive);
			Collection<WorldObject> newlyInactive = subtract(prevActive,
					nowActive);

			// Call overlapStart and overlapEnd on these newly active/inactive
			// objects
			newlyActive.stream().forEach(go -> overlapStart(go));
			newlyActive.stream().filter(go -> !(go instanceof Actor)).forEach(
					go -> go.overlapStart(this));
			newlyInactive.stream().forEach(go -> overlapEnd(go));
			newlyInactive.stream().filter(go -> !(go instanceof Actor)).forEach(
					go -> go.overlapEnd(this));

			activeInteractables = nowActive;
		}
	}

	//
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
			solidsToIgnoreThisFrame = new HashSet<>();
			positionDelta = Point.ZERO;
			setState(ActorState.IDLE);
			checkPush();
			jumpSustainThisFrame = false;
			act(gc, delta);
			checkForInteractions();
			if (interactNextFrame) {
				interactWithAll();
				interactNextFrame = false;
			}
			if (jumpNextFrame) {
				jump(delta);
				jumpNextFrame = false;
			}
			// Use exponential averaging to determine the 'velocity' of the
			// actor
			// This will be used for jump movement
			float alpha = 0.7f;
			velocityExp = velocityExp * alpha
					+ positionDelta.getX() * (1 - alpha) / delta;

			jumpMovement(delta);
		}

		if (lastState != state) {
			stateChanged(lastState, state);
		}

		lastState = state;
	}

	private void jumpMovement(int delta) {
		checkClimb();
		if (gravityEnabled() && state != ActorState.CLIMB) {
			checkWallSlide();
			calculateVSpeed(delta);

			if (state == ActorState.JUMP || state == ActorState.FALL
					|| state == ActorState.WALL_SLIDE) {
				move(Dir.SOUTH, vSpeed * delta);
				// Move E/W according to jump direction
				float moveAmount = jumpHSpeed * delta;
				move(Dir.EAST, moveAmount);
				// If hit a wall then set to zero
				if (getCollider() != null
						&& wallAheadSensorTop.isOverlappingSolid()
						|| wallAheadSensorBtm.isOverlappingSolid()) {
					jumpHSpeed = 0;
				}
			}
		}
	}

	public abstract void act(GameContainer gc, int delta);

	private void setState(ActorState newState) {
		if (newState == ActorState.CROUCH && state == ActorState.CROUCH_WALK) {
			// Do not let CROUCH overwrite CROUCH_WALK
			return;
		}
		if ((newState == ActorState.WALK || newState == ActorState.RUN)
				&& wasCrouching()) {
			newState = ActorState.CROUCH_WALK;
		}
		if (newState == ActorState.IDLE) {
			if (standingCollider != null && crouchingCollider != null
					&& wasCrouching()) {
				// Actually set to crouching if IDLE is not possible
				long standingCollisions = getOverlappingSolids(
						standingCollider.getRectangle()).count();
				if (standingCollisions != 0) {
					long crouchingCollisions = getOverlappingSolids(
							crouchingCollider.getRectangle()).count();
					if (crouchingCollisions == 0) {
						newState = ActorState.CROUCH;
					}
				}
			}
		}
		state = newState;
	}

	public void stateChanged(ActorState from, ActorState to) {
		// Updated sprite according to Actor's state
		if (getSprite() instanceof StatedSprite) {
			((StatedSprite) getSprite()).setState(state.ordinal());
		}

		if (to == ActorState.CLIMB) {
			// Reset vSpeed if start climbing
			vSpeed = 0;
		} else if (to == ActorState.WALL_SLIDE) {
			canMidAirJump = false;
		} else if (!wasCrouching()
				&& (to == ActorState.CROUCH || to == ActorState.CROUCH_WALK)) {
			// transitioning to crouching
			attach(crouchingCollider);
		}

		if ((from == ActorState.CROUCH && to != ActorState.CROUCH_WALK)
				|| (from == ActorState.CROUCH_WALK
						&& to != ActorState.CROUCH)) {
			// transitioning to standing
			attach(standingCollider);
		}

		// Clear the pushTarget if transitioning out of a pushing state.
		boolean wasPushing = from == ActorState.PUSH_PULL_IDLE
				|| from == ActorState.PUSH || from == ActorState.PULL;
		boolean nextPushing = to == ActorState.PUSH_PULL_IDLE
				|| to == ActorState.PUSH || to == ActorState.PULL;
		if (wasPushing && !nextPushing) {
			pushTarget = null;
		}

		// Set fall hSpeed
		if (to == ActorState.FALL && from != ActorState.JUMP) {
			jumpHSpeed = velocityExp * 0.8f;
		}
	}

	@Override
	public void statedSpriteImageChange() {
		autoAlignSprite();
	}

	private void interactWithAll() {
		// Stop pushing/pulling
		if (wasPushing()) {
			setState(ActorState.IDLE);
		} else if (!activeInteractables.isEmpty()) {
			Iterator<WorldObject> aii = activeInteractables.iterator();
			while (aii.hasNext()) {
				WorldObject go = aii.next();
				go.interactedBy(this);
				getWorld().signalEvent(
						new EventInfo(EventType.INTERACT, this, go));
			}
		}
	}

	@Override
	public void interactedBy(Actor a) {
		if (pushable) {
			a.startPushing(this);
		}
	}
}
