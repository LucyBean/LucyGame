package gameInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import helpers.Pair;
import helpers.Point;
import images.ImageBuilder;
import images.Sprite;
import images.SpriteBuilder;
import objectLibrary.Gem;
import objectLibrary.Key;
import objects.WorldObject;

public class Palette extends IEList {
	List<Pair<Image, Function<Point, WorldObject>>> elements;

	public Palette(Point firstPoint) {
		super(firstPoint, 16, 8, 4, () -> SpriteBuilder.makePaletteBlock());
		setBackground(Color.white);
		elements = new ArrayList<>();

		elements.add(new Pair<>(ImageBuilder.getGemImg(), p -> new Gem(p)));
		elements.add(new Pair<>(ImageBuilder.getKeyImg(1), p -> new Key(p, 1)));

		updateSprites();
	}

	@Override
	public void getElementSprite(int elementIndex, Sprite s) {
		if (elements != null && elementIndex >= 0
				&& elementIndex < elements.size()) {
			Image img = elements.get(elementIndex).getFirst();
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
			Function<Point, WorldObject> brush = elements.get(
					elementNumber).getSecond();
			getWorld().getMap().getPainter().setBrush(brush);
		}
	}

}
