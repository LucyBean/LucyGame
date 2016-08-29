package gameInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.newdawn.slick.Image;

import helpers.Point;
import images.ImageBuilder;
import images.LayeredImage;
import images.Sprite;

public class Menu extends IEList {
	private List<Supplier<String>> labels;
	private List<Consumer<Menu>> clickActions;
	private MenuSet menuSet;

	public Menu(Point firstPoint, int numDisplayElems,
			Supplier<Sprite> spriteMaker, MenuSet menuSet) {
		super(firstPoint, numDisplayElems, spriteMaker,
				(s -> s.getImage().setLayer(0,
						ImageBuilder.getMenuButtonSelectedBackground())));
		labels = new ArrayList<Supplier<String>>();
		clickActions = new ArrayList<Consumer<Menu>>();
		this.menuSet = menuSet;
	}

	public Menu(Point firstPoint, int numDisplayElems,
			Supplier<Sprite> spriteMaker) {
		this(firstPoint, numDisplayElems, spriteMaker, null);
	}

	public MenuSet getMenuSet() {
		return menuSet;
	}

	/**
	 * Adds a new entry to the bottom of this Menu.
	 * 
	 * @param text
	 *            A function that produces the text for the button.
	 * @param clickAction
	 *            The function that is run when the button is clicked.
	 */
	public void add(Supplier<String> text, Consumer<Menu> clickAction) {
		labels.add(text);
		clickActions.add(clickAction);
		updateSprites();
	}

	@Override
	public void elementClicked(int elementIndex) {
		if (clickActions != null && elementIndex >= 0
				&& elementIndex < labels.size()) {
			Consumer<Menu> ca = clickActions.get(elementIndex);
			ca.accept(this);
		}
	}

	@Override
	public void getElementSprite(int elementIndex, Sprite s) {
		if (labels != null && elementIndex >= 0
				&& elementIndex < labels.size()) {
			// There is information to be displayed
			String label = labels.get(elementIndex).get();
			LayeredImage limg = s.getImage();
			limg.setLayer(0, ImageBuilder.getMenuButtonBackground());
			limg.setTextCentered(1, label);
		} else {
			LayeredImage limg = s.getImage();
			limg.setLayer(0, (Image) null);
			limg.clear(1);
		}
	}

	@Override
	public int getNumElements() {
		return labels.size();
	}

}
