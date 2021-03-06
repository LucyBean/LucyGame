package worlds;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import helpers.Rectangle;
import objects.ObjectLayerSet;
import objects.world.Actor;
import objects.world.Lockable;
import objects.world.Locker;
import objects.world.WorldObject;
import objects.world.characters.Inventory;
import objects.world.characters.NPC;
import objects.world.characters.Player;
import options.GlobalOptions;

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
		if (objects != null) {
			objects.stream().forEach(wo -> addObject(wo));
		} else if (GlobalOptions.debug()) {
			System.err.println("Attempting to add a null collection of objects.");
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void addObject(WorldObject go) {
		if (go != null) {

			// If it is a Player, register their inventory with the inventory
			// displayer
			if (go instanceof Player) {
				Inventory i = ((Player) go).getInventory();
				world.setInventoryToDisplay(i);
				world.getCamera().setTarget(go);
			}
			
			WorldLayer layer = go.getLayer();
			layers.add(go, layer.ordinal());

			// Adds the object to any extra lists.
			if (go instanceof Actor) {
				actors.add((Actor) go);
			}
			if (go.getCollider().isPresent()) {
				objectsWithColliders.add(go);
			}
			if (go.getInteractBox().isPresent()) {
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

	public void addToActiveSets(WorldObject go) {
		if (go instanceof Actor) {
			activeActors.add((Actor) go);
		}
		if (go.getCollider().isPresent()) {
			objectsWithColliders.add(go);
		}
	}

	public void removeObject(WorldObject go) {
		layers.remove(go);

		if (go instanceof Actor) {
			actors.remove(go);
		}

		if (go.getCollider().isPresent()) {
			objectsWithColliders.remove(go);
		}
		if (go.getInteractBox().isPresent()) {
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

	public void removeFromActiveSets(WorldObject go) {
		if (go instanceof Actor) {
			activeActors.remove((Actor) go);
		}
		if (go.getCollider().isPresent()) {
			objectsWithColliders.remove(go);
		}
	}

	public Collection<WorldObject> getObjects() {
		return layers.getAll();
	}

	public Stream<WorldObject> getActiveSolids() {
		// Currently returns all solid objects in the world.
		// TODO: Modify to keep track of on screen objects.
		return objectsWithColliders.stream().filter(wo -> wo.getCollider().get().isSolid());
	}

	public Collection<WorldObject> getAllInteractables() {
		return interactables;
	}

	public Stream<Lockable> getLockablesByID(int lockID) {
		lockablesByID.putIfAbsent(lockID, new HashSet<Lockable>());
		return lockablesByID.get(lockID).stream();
	}

	public NPC getNPC(int npcID) {
		return npcsByID.get(npcID);
	}

	public <T extends WorldObject> Stream<T> getAllObjectsOfType(Class<T> t) {
		return getObjects().stream().filter(a -> a.isEnabled()).filter(a -> t.isInstance(a)).map(a -> t.cast(a));
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
	public <T extends WorldObject> Stream<T> getOverlappingObjectsOfType(Rectangle rectWorld, Class<T> t) {
		Stream<T> ts = getAllObjectsOfType(t);
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
	public Stream<WorldObject> getOverlappingSolids(Rectangle rectWorld) {
		Stream<WorldObject> solids = getActiveSolids();
		return findOverlappingObjects(rectWorld, solids, WorldObject.class);
	}

	private <T extends WorldObject> Stream<T> findOverlappingObjects(Rectangle rect, Stream<T> candidates, Class<T> t) {
		return candidates.filter(go -> go.getCollider().isPresent()).filter(go -> {
			Rectangle rectRel = go.getCollider().get().getRectangle();
			Rectangle rectWorld = go.getCoOrdTranslator().objectToWorldCoOrds(rectRel);
			return rectWorld.overlaps(rect);
		});
	}

	public void render() {
		layers.render();
	}

	public void update(GameContainer gc, int delta) {
		layers.update(gc, delta);
	}

	/**
	 * Finds the object at the point (given in window co-ordinates)
	 * 
	 * @param clickPoint
	 *            The point of the mouse click in window co-ordinates.
	 * @return
	 */
	public Optional<WorldObject> findObjectScreen(Point clickPoint) {
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
}
