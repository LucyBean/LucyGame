package gameInterface;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import helpers.Point;
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
	protected void elementClicked(int elementNumber) {
		// Need to figure out if the up/down button was clicked??`
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
