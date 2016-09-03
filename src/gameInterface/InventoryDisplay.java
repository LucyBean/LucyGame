package gameInterface;

import java.util.Iterator;
import java.util.TreeMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import helpers.Point;
import images.LayeredImage;
import images.Sprite;
import images.SpriteBuilder;
import player.Inventory;
import player.InventoryItem;

public class InventoryDisplay extends IEList {
	TreeMap<InventoryItem, Integer> items;

	InventoryItem minItem;
	InventoryItem maxItem;

	public InventoryDisplay(Point firstPoint, Inventory i) {
		super(firstPoint, 4, (() -> SpriteBuilder.makeInventoryDisplaySprite()),
				(s -> s.getImage().fillLayer(0, new Color(120, 120, 180))));
		this.items = i.getItems();
		updateSprites();
	}

	public InventoryDisplay(Point firstPoint) {
		super(firstPoint, 4, (() -> SpriteBuilder.makeInventoryDisplaySprite()),
				(s -> s.getImage().fillLayer(0, new Color(120, 120, 180))));
	}

	public void setInventory(Inventory i) {
		this.items = i.getItems();
		updateSprites();
	}

	@Override
	public void elementClicked(int elementIndex) {
		moveUp();
	}

	@Override
	public void getElementSprite(int elementIndex, Sprite s) {
		if (items != null && elementIndex >= 0 && elementIndex < items.size()) {
			// There is information to display
			// Navigate to correct element
			Iterator<InventoryItem> iie = items.navigableKeySet().iterator();
			for (int i = 0; i < elementIndex; i++) {
				iie.next();
			}

			InventoryItem ii = iie.next();
			LayeredImage limg = s.getImage();
			// set the sprite according to the contents of ii
			Image img = ii.getImage();
			String name = ii.getName();
			Point vTextAlign = new Point(0, 8);
			int quantity = items.get(ii);

			// Set deselected background
			limg.fillLayer(0, new Color(200, 200, 200));

			// Set image on layer 1
			limg.setLayer(1, img);

			// Set name on layer 2
			limg.setText(2, name, vTextAlign);

			// Set quantity on layers 3 and 4
			if (quantity >= 10) {
				int digitTens = quantity / 10;
				limg.setText(3, "" + digitTens, vTextAlign);
			} else {
				limg.clear(3);
			}
			int digitOnes = quantity % 10;
			limg.setText(4, "" + digitOnes, vTextAlign);
		} else {
			// No information to display
			LayeredImage limg = s.getImage();

			// Set deselected background
			limg.fillLayer(0, new Color(200, 200, 200));

			// Remove icon
			limg.setLayer(1, (Image) null);

			// Clear name and quantity
			for (int i = 2; i <= 4; i++) {
				limg.clear(i);
			}
		}
	}

	@Override
	public int getNumElements() {
		if (items == null) {
			return 0;
		} else {
			return items.size();
		}
	}
}
