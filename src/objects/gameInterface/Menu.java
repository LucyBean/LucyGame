package objects.gameInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import helpers.Pair;
import helpers.Point;
import objects.images.ImageBuilder;
import objects.images.LayeredImage;
import objects.images.LucyImage;
import objects.images.Sprite;

public abstract class Menu extends IEList {
	private List<Supplier<String>> labels = new ArrayList<>();
	private List<Consumer<Menu>> clickActions = new ArrayList<>();
	private List<Consumer<Pair<Menu, String>>> inputAccepts = new ArrayList<>();
	private Consumer<Pair<Menu, String>> receiveInput;
	private MenuSet menuSet;

	public Menu(Point firstPoint, int numDisplayElems, MenuSet menuSet, boolean useSelection) {
		super(firstPoint, numDisplayElems, 1, 4, (useSelection) ? (s -> s.getImage().setLayer(0,
				ImageBuilder.getColouredRectangle(240, 32, 1))) : null);
		this.menuSet = menuSet;
	}
	
	public Menu(Point firstPoint, int numDisplayElems, MenuSet menuSet) {
		this(firstPoint, numDisplayElems, menuSet, true);
	}

	public Menu(Point firstPoint, int numDisplayElems) {
		this(firstPoint, numDisplayElems, null);
	}

	public MenuSet getMenuSet() {
		return menuSet;
	}

	@Override
	public void onClick(int button, Point clickPoint) {

	}

	/**
	 * Adds a new entry to the bottom of this Menu.
	 * 
	 * @param text
	 *            A function that produces the text for the button.
	 * @param clickAction
	 *            The function that is run when the button is clicked.
	 */
	public void add(Supplier<String> text, Consumer<Menu> clickAction, Consumer<Pair<Menu, String>> receiveInput) {
		labels.add(text);
		clickActions.add(clickAction);
		inputAccepts.add(receiveInput);
		updateSprites();
	}

	@Override
	protected void elementClicked(int elementIndex) {
		if (clickActions != null && elementIndex >= 0
				&& elementIndex < labels.size()) {
			Consumer<Menu> ca = clickActions.get(elementIndex);
			if (ca != null) {
				ca.accept(this);
			}
			receiveInput = inputAccepts.get(elementIndex);
		}
	}

	@Override
	protected void getElementSprite(int elementIndex, Sprite s) {
		if (labels != null && elementIndex >= 0
				&& elementIndex < labels.size()) {
			// There is information to be displayed
			String label = labels.get(elementIndex).get();
			LayeredImage limg = s.getImage();
			limg.setLayer(0, ImageBuilder.getColouredRectangle(240, 32, 0));
			limg.setTextCentered(1, label);
		} else {
			LayeredImage limg = s.getImage();
			limg.setLayer(0, (LucyImage) null);
			limg.clear(1);
		}
	}

	@Override
	protected int getNumElements() {
		if (labels == null) {
			return 0;
		} else {
			return labels.size();
		}
	}
	
	@Override
	public void acceptInput(String s) {
		// TODO: Run accept input method from last clicked element
		if (receiveInput != null) {
			receiveInput.accept(new Pair<Menu, String>(this,s));
		}
		receiveInput = i -> {};
	}

}
