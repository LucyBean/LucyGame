package worlds;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.Actor;
import objects.Lockable;
import objects.Locker;
import objects.ObjectLayerSet;
import objects.WorldObject;
import options.GlobalOptions;

/**
 * The Map is responsible for holding all the objects in a World.
 * 
 * @author Lucy
 *
 */
public class WorldMap {
	private Collection<Actor> actors;
	private Set<Actor> activeActors;
	private Collection<WorldObject> solids;
	private Set<WorldObject> activeSolids;
	private Collection<WorldObject> interactables;
	private ObjectLayerSet<WorldObject> layers;
	private MapPainter mapPainter;
	private World world;
	private Map<Integer, Set<Lockable>> lockablesByID;
	private Map<Integer, Locker> lockers;

	public WorldMap(World world) {
		layers = new ObjectLayerSet<>();
		actors = new HashSet<>();
		activeActors = new HashSet<>();
		solids = new HashSet<>();
		activeSolids = new HashSet<>();
		interactables = new HashSet<>();
		mapPainter = new MapPainter(this);
		lockablesByID = new HashMap<>();
		lockers = new HashMap<>();
		this.world = world;
	}

	public MapPainter getPainter() {
		return mapPainter;
	}

	public void addObject(WorldObject go) {
		WorldLayer layer = go.getLayer();
		layers.add(go, layer.ordinal());

		// Adds the object to any extra lists.
		if (go instanceof Actor) {
			actors.add((Actor) go);
		}
		if (go.isSolid()) {
			solids.add(go);
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
			if (GlobalOptions.debug() && old != null) {
				System.err.println("Warning! Locks with duplicate lockIDs.");
			}

		}

		go.setWorld(world);
	}

	public void removeObject(WorldObject go) {
		layers.remove(go);

		if (go instanceof Actor) {
			actors.remove(go);
		}

		if (go.isSolid()) {
			solids.remove(go);
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
			lockers.remove(lockID);
		}

		removeFromActiveSets(go);
	}

	public Collection<WorldObject> getActiveSolids() {
		// Currently returns all solid objects in the world.
		// Modify to keep track of on screen objects.
		return activeSolids;
	}

	public Collection<WorldObject> getAllInteractables() {
		return interactables;
	}

	public void removeFromActiveSets(WorldObject go) {
		if (go instanceof Actor) {
			activeActors.remove((Actor) go);
		}
		if (go.isSolid()) {
			activeSolids.remove(go);
		}
	}

	public void addToActiveSets(WorldObject go) {
		if (go instanceof Actor) {
			activeActors.add((Actor) go);
		}
		if (go.isSolid()) {
			activeSolids.add(go);
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

}
