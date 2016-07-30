package objectLibrary;

import org.newdawn.slick.Color;

import helpers.Rectangle;
import objects.InterfaceElement;

public class TextBox extends InterfaceElement {

	public TextBox(Rectangle rect) {
		super(rect);
		setBackground(Color.white);
		setText("Hello, world!");
	}

	@Override
	public void onClick(int button) {
		
	}

}
