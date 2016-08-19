package images;

import java.util.HashMap;
import java.util.Map;

class RectangleSpriteStore {
	private Map<Integer, Map<Integer, Sprite>> store;

	public RectangleSpriteStore() {
		store = new HashMap<Integer, Map<Integer, Sprite>>();
	}

	public Sprite get(int width, int height) {
		// look up by width first
		Map<Integer, Sprite> m = store.get(width);

		if (m != null) {
			// look up by height
			Sprite r = m.get(height);

			if (r != null) {
				return r;
			}
		}
		
		return null;
	}
	
	public void store(Sprite s) {
		int width = s.getImage().getWidth();
		int height = s.getImage().getHeight();
		
		store.putIfAbsent(width, new HashMap<Integer, Sprite>());
		Map<Integer, Sprite> m = store.get(width);
		m.put(height, s);
	}
}
