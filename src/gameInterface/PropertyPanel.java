package gameInterface;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import helpers.Point;
import helpers.Rectangle;
import images.PropertyPanelSprite;
import images.Sprite;

public class PropertyPanel extends IEList {
	List<Property> properties;

	public PropertyPanel(Point firstPoint) {
		super(firstPoint, 1, 4, 4);
		setBackground(Color.cyan);

		properties = new ArrayList<Property>();

		properties.add(new Property("Lock ID", 1) {
			@Override
			protected boolean isValid(int value) {
				return (value > 0 && value < 256);
			}
		});

		properties.add(new Property("NPC ID", 1) {
			@Override
			protected boolean isValid(int value) {
				return (value > 0 && value < 256);
			}
		});

		updateSprites();
	}

	@Override
	protected void getElementSprite(int elementIndex, Sprite s) {
		PropertyPanelSprite pps = (PropertyPanelSprite) s;
		if (properties != null && elementIndex >= 0
				&& elementIndex < properties.size()) {
			Property p = properties.get(elementIndex);
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
		int minItemDisplayed = getMinItemDisplayed();
		int elementIndex = buttonIndex + minItemDisplayed;
		if (elementExists(elementIndex) && clickPoint != null) {
			Property p = properties.get(elementIndex);
			Sprite s = getButton(buttonIndex).getSprite();
			PropertyPanelSprite pps = (PropertyPanelSprite) s;
			Rectangle plus = pps.getPlusBound();
			Rectangle minus = pps.getMinusBound();

			if (plus.contains(clickPoint)) {
				p.increment();
				updateSprites();
			} else if (minus.contains(clickPoint)) {
				p.decrement();
				updateSprites();
			}
		}
	}

	@Override
	protected void elementClicked(int elementNumber) {

	}

	@Override
	protected Sprite makeNewSprite() {
		return new PropertyPanelSprite();
	}

}

abstract class Property {
	String name;
	int value;

	public Property(String name, int value) {
		this.name = name;
		setValue(value);
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	private void setValue(int newValue) {
		if (isValid(newValue)) {
			value = newValue;
		}
	}

	public void increment() {
		int newValue = value + 1;
		setValue(newValue);
	}

	public void decrement() {
		int newValue = value - 1;
		setValue(newValue);
	}

	protected abstract boolean isValid(int value);
}
