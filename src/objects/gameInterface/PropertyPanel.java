package objects.gameInterface;

import java.util.ArrayList;
import java.util.List;

import helpers.Point;
import helpers.Rectangle;
import objects.images.PropertyPanelSprite;
import objects.images.SingleSprite;
import objects.images.Sprite;
import worlds.PainterProperty;

public class PropertyPanel extends IEList {
	List<PainterProperty> properties;

	public PropertyPanel(Point firstPoint) {
		super(firstPoint, 1, 4, 4);
		setBackground(5);

		properties = new ArrayList<PainterProperty>();
		properties.add(PainterProperty.getLockID());
		properties.add(PainterProperty.getNpcID());
		
		PainterProperty.setPanel(this);

		updateSprites();
	}

	@Override
	protected void getElementSprite(int elementIndex, Sprite s) {
		PropertyPanelSprite pps = (PropertyPanelSprite) s;
		if (properties != null && elementIndex >= 0
				&& elementIndex < properties.size()) {
			PainterProperty p = properties.get(elementIndex);
			pps.setName(p.getName());
			pps.setQuantity(p.getValue());
		} else {
			pps.clear();
		}
	}

	@Override
	protected int getNumElements() {
		return properties.size();
	}

	@Override
	public void buttonClicked(int buttonIndex, Point clickPoint) {
		assert validButtonIndex(buttonIndex);
		int minItemDisplayed = getMinItemDisplayed();
		int elementIndex = buttonIndex + minItemDisplayed;
		if (elementExists(elementIndex) && clickPoint != null) {
			PainterProperty p = properties.get(elementIndex);
			Sprite s = getButton(buttonIndex).get().getSprite().get();
			PropertyPanelSprite pps = (PropertyPanelSprite) s;
			Rectangle plus = pps.getPlusBound();
			Rectangle minus = pps.getMinusBound();

			if (plus.contains(clickPoint)) {
				p.increment();
			} else if (minus.contains(clickPoint)) {
				p.decrement();
			}
		}
	}

	@Override
	protected void elementClicked(int elementNumber) {

	}

	@Override
	protected SingleSprite makeNewSprite() {
		return new PropertyPanelSprite();
	}

}
