package objects.gameInterface;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import helpers.Point;
import objects.images.LayeredImage;
import objects.images.Sprite;
import objects.images.SpriteBuilder;
import objects.world.ItemType;

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
	protected void getElementSprite(int elementIndex, Sprite s) {
		if (elements != null && elementIndex >= 0
				&& elementIndex < elements.size()) {
			ItemType it = elements.get(elementIndex);
			Sprite sit = it.getSprite();
			LayeredImage limg = sit.getImage();
			
			float wratio = ((float) s.getImage().getWidth()) / limg.getWidth();
			float hratio = ((float) s.getImage().getHeight()) / limg.getHeight();
			float drawScale = Math.min(wratio, hratio);
			s.setDrawScale(drawScale);
			s.getImage().setLayers(limg);
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
	protected Sprite makeNewSprite() {
		return SpriteBuilder.makePaletteBlock();
	}

}
