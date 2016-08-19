package gameInterface;

import images.ImageBuilder;

public class MenuButton extends IEListItem {
	Menu menu;
	
	public MenuButton(String text) {
		super(ImageBuilder.getMenuButtonBackground());
		setTextCentered(text);
	}
	
	public void setMenu(Menu m) {
		menu = m;
	}
	
	public Menu getMenu() {
		return menu;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 40;
	}

	@Override
	public void onClick(int button) {
		// TODO Auto-generated method stub
		
	}
}
