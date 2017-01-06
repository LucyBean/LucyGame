package objects.gameInterface;

import java.util.ArrayList;
import java.util.List;

import helpers.Point;
import helpers.Rectangle;
import objects.images.LayeredImage;
import objects.images.SingleSprite;
import objects.images.Sprite;
import objects.world.ItemType;
import options.GlobalOptions;

public class Palette extends IEList {
	List<ItemType> elements;
	private static final int blockSize = GlobalOptions.GRID_SIZE;

	public Palette(Point firstPoint) {
		super(firstPoint, 2, 8, 4);
		setBackground(2);
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
		SingleSprite ss = (SingleSprite) s;
		
		if (elements != null && elementIndex >= 0
				&& elementIndex < elements.size()) {
			ItemType it = elements.get(elementIndex);
			Sprite sit = it.getSprite();
			LayeredImage limg = sit.getImage();
			
			float wratio = ((float) blockSize) / limg.getWidth();
			float hratio = ((float) blockSize) / limg.getHeight();
			float drawScale = Math.min(wratio, hratio);
			
			ss.setDrawScale(drawScale);
			ss.setImage(limg, Point.ZERO);
		} else {
			ss.setImage(null, Point.ZERO);
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
		return new SingleSprite(new Rectangle(Point.ZERO, blockSize, blockSize), 1);
	}

}
