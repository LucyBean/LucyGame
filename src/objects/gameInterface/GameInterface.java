package objects.gameInterface;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.ObjectLayer;
import objects.ObjectLayerSet;
import objects.world.WorldObject;
import objects.world.characters.Conversation;
import objects.world.characters.Inventory;
import worlds.World;
import worlds.WorldState;

/**
 * Class for building GameInterfaces.
 * 
 * @author Lucy
 *
 */
public class GameInterface {
	private ObjectLayerSet<InterfaceElement> interfaces;
	private ObjectLayer<InterfaceElement> allStateInterface;
	private StatusWindow statusWindow;
	private InventoryDisplay inventoryDisplay;
	private ConversationDisplay conversationDisplay;
	private World world;
	private List<MenuSet> menus;
	private TextPrompt textPrompt;

	public GameInterface() {
		interfaces = new ObjectLayerSet<InterfaceElement>();
		allStateInterface = new ObjectLayer<InterfaceElement>();
		menus = new ArrayList<MenuSet>();
	}

	/**
	 * Sets the World of the GameInterface and all contained InterfaceElements.
	 * 
	 * @param w
	 */
	public void setWorld(World w) {
		world = w;
		interfaces.applyToAllObjects(a -> a.setWorld(w));
		allStateInterface.applyToAll(a -> a.setWorld(w));
	}

	/**
	 * Adds an InterfaceElement to the interface for a specified WorldState.
	 * 
	 * @param ie
	 *            The InterfaceElement to be added.
	 * @param state
	 *            The WorldState for which the InterfaceElement should appear.
	 */
	public void add(InterfaceElement ie, WorldState state) {
		if (state != null) {
			interfaces.add(ie, state.ordinal());
			ie.setWorld(world);
		}
		if (ie instanceof MenuSet) {
			menus.add((MenuSet) ie);
		}
		if (ie instanceof TextPrompt) {
			textPrompt = (TextPrompt) ie;
		}
	}

	/**
	 * Adds the InterfaceElement to all interface states.
	 * 
	 * @param ie
	 */
	public void addToAll(InterfaceElement ie) {
		allStateInterface.add(ie);
		if (ie instanceof MenuSet) {
			menus.add((MenuSet) ie);
		}
		if (ie instanceof TextPrompt) {
			textPrompt = (TextPrompt) ie;
		}
		ie.setWorld(world);
	}

	/**
	 * Removes an InterfaceElement from all interfaces in which it appears.
	 * 
	 * @param ie
	 *            The InterfaceElement to remove.
	 */
	public void remove(InterfaceElement ie) {
		allStateInterface.remove(ie);
		interfaces.applyToAllLayers(ol -> ol.remove(ie));
	}

	/**
	 * Sets the watch target for the GameInterface's StatusWindow. This will
	 * also enable the StatusWindow.
	 * 
	 * @param a
	 */
	public void setWatchTarget(WorldObject a) {
		enableStatusWindow();
		statusWindow.setWatching(a);
	}

	public void enableStatusWindow() {
		if (statusWindow == null) {
			statusWindow = new StatusWindow(new Point(0, 0));
			addToAll(statusWindow);
		}
	}
	
	public void focusTextPrompt() {
		if (textPrompt != null) {
			textPrompt.focus();
		}
	}

	public void setInventoryToDisplay(Inventory i) {
		if (inventoryDisplay == null) {
			inventoryDisplay = new InventoryDisplay(new Point(400, 40), i);
			add(inventoryDisplay, WorldState.INVENTORY);
		} else {
			inventoryDisplay.setInventory(i);
		}
	}

	public void refreshInventoryDisplay() {
		if (inventoryDisplay != null) {
			inventoryDisplay.updateSprites();
		}
	}

	public void showConversation(Conversation c) {
		if (conversationDisplay == null) {
			conversationDisplay = new ConversationDisplay();
			add(conversationDisplay, WorldState.CONVERSATION);
		}
		conversationDisplay.setConversation(c);
	}

	/**
	 * Propagates the mousePressed update to all InterfaceElements within the
	 * currently active interface.
	 * 
	 * @param button
	 * @param clickPoint
	 * @param state
	 */
	public void mousePressed(int button, Point clickPoint, WorldState state) {
		interfaces.applyToLayerObjects(a -> a.mousePressed(button, clickPoint),
				state.ordinal());
		allStateInterface.applyToAll(a -> a.mousePressed(button, clickPoint));
	}

	/**
	 * Propagates the keyPressed update to all InterfaceElements within the
	 * currently active interface.
	 * 
	 * @param keycode
	 */
	public void keyPressed(int keycode, WorldState state) {
		interfaces.applyToLayerObjects(a -> a.keyPressed(keycode),
				state.ordinal());
		allStateInterface.applyToAll(a -> a.keyPressed(keycode));
	}

	/**
	 * Checks whether the mouse is currently over an element in this interface.
	 * 
	 * @param mousePoint
	 *            The position of the mouse.
	 * @return
	 */
	public boolean mouseOver(Point mousePoint, WorldState state) {
		InterfaceElement t = interfaces.findClickedObject(mousePoint, state.ordinal());
		if (t != null) {
			return true;
		}
		
		t = allStateInterface.findClickedObject(mousePoint);
		if (t !=null) {
			return true;
		}
		
		return false;
	}

	/**
	 * Renders the required interface for the current WorldState.
	 * 
	 * @param state
	 *            The WorldState for which the interface should be rendered.
	 */
	public void render(WorldState state) {
		interfaces.render(state.ordinal());
		allStateInterface.render();
	}

	/**
	 * Propagates updates to all objects on this layer.
	 * 
	 * @param gc
	 *            GameContainer object to use.
	 * @param delta
	 *            Time difference.
	 */
	public void update(final GameContainer gc, final int delta,
			WorldState state) {
		interfaces.update(gc, delta, state.ordinal());
		allStateInterface.update(gc, delta);
	}

	/**
	 * Resets the menus to their original state (i.e. closes all sub-menus)
	 */
	public void resetMenus() {
		menus.stream().forEach(m -> m.reset());
	}
}
