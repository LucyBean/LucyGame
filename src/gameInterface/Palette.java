package gameInterface;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import helpers.Point;
import images.ImageBuilder;
import images.LucyImage;
import images.SingleSprite;
import images.SpriteBuilder;
import objects.ItemType;

public class Palette extends IEList {
	List<ItemType> elements;

	public Palette(Point firstPoint) {
		super(firstPoint, 2, 8, 4);
		setBackground(Color.white);
		elements = new ArrayList<>();

		for (ItemType it : ItemType.values()) {
			if (it.isPaintable()) {
				elements.add(it);
			}
		}

		updateSprites();
	}

	@Override
	protected void getElementSprite(int elementIndex, SingleSprite s) {
		if (elements != null && elementIndex >= 0
				&& elementIndex < elements.size()) {
			ItemType it = elements.get(elementIndex);
			LucyImage img = ImageBuilder.getItemImage(it);
			s.getImage().setLayer(1, img);
		} else {
			s.getImage().clear(1);
		}
	}

	@Override
	protected int getNumElements() {
		return elements.size();
	}

	@Override
	protected void elementClicked(int elementNumber) {
		if (elements != null && elementNumber >= 0
				&& elementNumber < elements.size()) {
			ItemType it = elements.get(elementNumber);
			getWorld().getMap().getPainter().setItem(it);
		}
	}

	@Override
	protected SingleSprite makeNewSprite() {
		return SpriteBuilder.makePaletteBlock();
	}

}
