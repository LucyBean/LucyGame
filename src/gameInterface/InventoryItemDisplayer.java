package gameInterface;

import org.newdawn.slick.Image;

import helpers.Point;
import images.LayeredImage;
import images.SpriteBuilder;
import player.InventoryItem;

public class InventoryItemDisplayer extends IEListItem {

	public InventoryItemDisplayer() {
		super(SpriteBuilder.makeInventoryDisplaySprite());
	}
	
	public void display(InventoryItem item, int quantity) {
		Point vTextAlign = new Point(0,8);
		Image img = item.getImage();
		String name = item.getName();
		
		LayeredImage limg = getSprite().getImage();
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
	}
	
	public void displayBlank() {
		LayeredImage limg = getSprite().getImage();
		// Clear layers 1 to 4
		for (int i = 1; i <= 4; i++) {
			limg.clear(i);
		}
	}

	@Override
	public float getHeight() {
		return getSprite().getBoundingRectangle().getHeight();
	}

	@Override
	public void onClick(int button) {
		// TODO Auto-generated method stub
		
	}
}
