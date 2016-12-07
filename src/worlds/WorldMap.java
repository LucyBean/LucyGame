package worlds;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.GameContainer;

import characters.NPC;
import helpers.Point;
import helpers.Rectangle;
import objects.Actor;
import objects.Lockable;
import objects.Locker;
import objects.ObjectLayerSet;
import objects.WorldObject;
import player.Player;

/**
 * The Map is responsible for holding all the objects in a World.
 * 
 * @author Lucy
 *
 */
public class WorldMap {
	private Collection<Actor> actors;
	private Collection<Actor> activeActors;
	private Collection<WorldObject> objectsWithColliders;
	private Collection<WorldObject> interactables;
	private ObjectLayerSet<WorldObject> layers;
	private MapPainter mapPainter;
	private World world;
	private Map<Integer, Set<Lockable>> lockablesByID;
	private Map<Integer, Locker> lockers;
	private Map<Integer, NPC> npcsByID;
	private Player player;

	public WorldMap(World world) {
		reset();
		this.world = world;
	}

	public void reset() {
		layers = new ObjectLayerSet<>();
		actors = new HashSet<>();
		activeActors = new HashSet<>();
		objectsWithColliders = new HashSet<>();
		interactables = new HashSet<>();
		mapPainter = new MapPainter(this);
		lockablesByID = new HashMap<>();
		lockers = new HashMap<>();
		npcsByID = new HashMap<>();
	}

	public MapPainter getPainter() {
		return mapPainter;
	}

	public void addObjects(Collection<WorldObject> objects) {
		objects.stream().forEach(wo -> addObject(wo));
	}

	public Collection<WorldObject> getObjects() {
		Collection<WorldObject> objects = new HashSet<>();

		layers.values().stream().forEach(layer -> objects.addAll(layer));

		return objects;
	}

	public Player getPlayer() {
		return player;
	}

	public void addObject(WorldObject go) {
		if (go != null) {
			WorldLayer layer = go.getLayer();
			layers.add(go, layer.ordinal());

			// Adds the object to any extra lists.
			if (go instanceof Actor) {
				actors.add((Actor) go);
			}
			if (go.hasCollider()) {
				objectsWithColliders.add(go);
			}
			if (go.isInteractable()) {
				interactables.add(go);
			}
			if (go.isEnabled()) {
				addToActiveSets(go);
			}
			if (go instanceof Lockable) {
				Lockable lgo = (Lockable) go;
				int lockID = lgo.getLockID();
				lockablesByID.putIfAbsent(lockID, new HashSet<Lockable>());
				lockablesByID.get(lockID).add(lgo);
				Locker locker = lockers.get(lockID);
				if (locker != null) {
					if (locker.isLocked()) {
						lgo.lock();
					} else {
						lgo.unlock();
					}
				}
			}
			if (go instanceof Locker) {
				Locker lgo = (Locker) go;
				int lockID = lgo.getLockID();
				Locker old = lockers.put(lockID, lgo);
				if (old != null) {
					removeObject(old);
				}
			}
			if (go instanceof Player) {
				if (player != null) {
					removeObject(player);
				}
				player = (Player) go;
			}
			if (go instanceof NPC) {
				NPC npc = (NPC) go;
				NPC n = npcsByID.put(npc.getNPCID(), npc);
				if (n != null) {
					removeObject(n);
				}
			}

			go.setWorld(world);
		}
	}

	public void removeObject(WorldObject go) {
		layers.remove(go);

		if (go instanceof Actor) {
			actors.remove(go);
		}

		if (go.hasCollider()) {
			objectsWithColliders.remove(go);
		}
		if (go.isInteractable()) {
			interactables.remove(go);
		}
		if (go instanceof Lockable) {
			Lockable lgo = (Lockable) go;
			int lockID = lgo.getLockID();
			lockablesByID.get(lockID).remove(lgo);
		}
		if (go instanceof Locker) {
			Locker lgo = (Locker) go;
			int lockID = lgo.getLockID();
			Locker lock = lockers.get(lockID);
			if (lgo == lock) {
				lockers.remove(lockID);
			}
		}

		removeFromActiveSets(go);
	}

	public Collection<WorldObject> getActiveSolids() {
		// Currently returns all solid objects in the world.
		// Modify to keep track of on screen objects.
		Collection<WorldObject> solids = new HashSet<>();
		objectsWithColliders.stream().filter(
				wo -> wo.getCollider().isSolid()).forEach(wo -> solids.add(wo));
		return solids;
	}

	public Collection<WorldObject> getAllInteractables() {
		return interactables;
	}

	public void removeFromActiveSets(WorldObject go) {
		if (go instanceof Actor) {
			activeActors.remove((Actor) go);
		}
		if (go.hasCollider()) {
			objectsWithColliders.remove(go);
		}
	}

	public void addToActiveSets(WorldObject go) {
		if (go instanceof Actor) {
			activeActors.add((Actor) go);
		}
		if (go.hasCollider()) {
			objectsWithColliders.add(go);
		}
	}

	public Collection<Lockable> getLockablesByID(int lockID) {
		return lockablesByID.get(lockID);
	}

	public void render() {
		layers.render();
	}

	public void update(GameContainer gc, int delta) {
		layers.update(gc, delta);
	}

	public WorldObject findClickedObject(Point clickPoint) {
		return layers.findClickedObject(clickPoint);
	}

	/**
	 * Propagates keyPressed signal to all objects on the map
	 * 
	 * @param keycode
	 */
	public void keyPressed(int keycode) {
		layers.applyToAllObjects(c -> c.keyPressed(keycode));
	}

	public <T extends WorldObject> Collection<T> getAllObjectsOfType(
			Class<T> t) {
		Collection<WorldObject> objects = layers.getAll();
		Collection<T> ts = new HashSet<T>();
		objects.stream().filter(a -> t.isInstance(a)).map(
				a -> t.cast(a)).forEach(a -> ts.add(a));
		return ts;
	}

	public Collection<WorldObject> getAllObjects() {
		Collection<WorldObject> objects = layers.getAll();
		return objects;
	}

	/**
	 * Gets all WorldObjects of the type T whose collider's overlap with the
	 * given rectangle.
	 * 
	 * @param rectWorld
	 *            The rectangle to check in world co-ords
	 * @param t
	 *            The type of objects that should be returned
	 * @return
	 */
	public <T extends WorldObject> Collection<T> getOverlappingObjectsOfType(
			Rectangle rectWorld, Class<T> t) {
		Collection<T> ts = getAllObjectsOfType(t);
		return findOverlappingObjects(rectWorld, ts, t);
	}

	/**
	 * Gets all WorldObjects with solid Colliders that overlap with the given
	 * rectangle.
	 * 
	 * @param rectWorld
	 *            The rectangle to check in world co-ords
	 * @return All solid Colliders that overlap.
	 */
	public Collection<WorldObject> getOverlappingSolids(Rectangle rectWorld) {
		Collection<WorldObject> solids = getActiveSolids();
		return findOverlappingObjects(rectWorld, solids, WorldObject.class);
	}

	private <T extends WorldObject> Collection<T> findOverlappingObjects(
			Rectangle rect, Collection<T> candidates, Class<T> t) {
		Collection<T> overlapping = new HashSet<T>();
		Iterator<T> si = candidates.iterator();
		while (si.hasNext()) {
			T go = si.next();
			if (go.hasCollider()) {
				Rectangle rectRel = go.getCollider().getRectangle();
				Rectangle rectWorld = go.getCoOrdTranslator().objectToWorldCoOrds(
						rectRel);
				if (rectWorld.overlaps(rect)) {
					overlapping.add(go);
				}
			}
		}
		return overlapping;
	}
}
