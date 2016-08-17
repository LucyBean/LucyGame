package objectLibrary;

import org.newdawn.slick.Color;

import gameInterface.InterfaceElement;
import helpers.Rectangle;

public class TextBox extends InterfaceElement {

	public TextBox(Rectangle rect) {
		super(rect);
		setBackground(Color.white);
		setTextCentered("Hello, world!");
	}

	@Override
	public void onClick(int button) {
		
	}

}
