package gameInterface;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import helpers.Point;
import images.ImageBuilder;
import images.Sprite;
import images.SpriteBuilder;
import objects.ItemType;

public class Palette extends IEList {
	List<ItemType> elements;

	public Palette(Point firstPoint) {
		super(firstPoint, 16, 8, 4, () -> SpriteBuilder.makePaletteBlock());
		setBackground(Color.white);
		elements = new ArrayList<>();
		
		for (ItemType it : ItemType.values()) {
			elements.add(it);
		}

		updateSprites();
	}

	@Override
	public void getElementSprite(int elementIndex, Sprite s) {
		if (elements != null && elementIndex >= 0
				&& elementIndex < elements.size()) {
			ItemType it = elements.get(elementIndex);
			Image img = ImageBuilder.getItemImage(it);
			s.getImage().setLayer(1, img);
		} else {
			s.getImage().clear(1);
		}
	}

	@Override
	public int getNumElements() {
		return elements.size();
	}

	@Override
	public void elementClicked(int elementNumber) {
		if (elements != null && elementNumber >= 0
				&& elementNumber < elements.size()) {
			ItemType it = elements.get(elementNumber);
			getWorld().getMap().getPainter().setItem(it);
		}
	}

}
