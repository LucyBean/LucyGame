package worlds;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.Actor;
import objects.ObjectLayerSet;
import objects.WorldObject;

/**
 * The Map is responsible for holding all the objects in a World.
 * 
 * @author Lucy
 *
 */
public class Map {
	private Collection<Actor> actors;
	private Set<Actor> activeActors;
	private Collection<WorldObject> solids;
	private Set<WorldObject> activeSolids;
	private Collection<WorldObject> interactables;
	private ObjectLayerSet<WorldObject> layers;

	public Map() {
		layers = new ObjectLayerSet<>();
		actors = new HashSet<>();
		activeActors = new HashSet<>();
		solids = new HashSet<>();
		activeSolids = new HashSet<>();
		interactables = new HashSet<>();
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
