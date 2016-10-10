package characters;

import images.ImageBuilder;
import images.LucyImage;

public enum ConversationCharacter {
	LUCY(ImageBuilder.getConversationCharacterImg(0),"Lucy"),
	MATT (ImageBuilder.getConversationCharacterImg(1), "Matt");
	
	LucyImage img;
	String name;
	
	ConversationCharacter(LucyImage img, String name) {
		this.img = img;
		this.name= name;
	}
	
	public LucyImage getImage() {
		return img;
	}
	
	public String getName() {
		return name;
	}
}
