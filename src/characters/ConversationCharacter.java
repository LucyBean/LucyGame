package characters;

import org.newdawn.slick.Image;

import images.ImageBuilder;

public enum ConversationCharacter {
	LUCY(ImageBuilder.getConversationCharacterImg(0),"Lucy"),
	MATT (ImageBuilder.getConversationCharacterImg(1), "Matt");
	
	Image img;
	String name;
	
	ConversationCharacter(Image img, String name) {
		this.img = img;
		this.name= name;
	}
	
	public Image getImage() {
		return img;
	}
	
	public String getName() {
		return name;
	}
}
