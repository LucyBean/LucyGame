package gameInterface;

import options.Option;

public class OptionButton extends MenuButton {
	Option option;
	public OptionButton(Option option) {
		super(option.toString());
		this.option = option;
	}
	
	@Override
	public void onClick(int button) {
		option.setToNextValue();
		setText(option.toString());
	}
}
