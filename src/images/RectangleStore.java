package images;

import java.util.HashMap;
import java.util.Map;

class RectangleImageStore {
	private Map<Integer, Map<Integer, LucyImage>> store;

	public RectangleImageStore() {
		store = new HashMap<>();
	}

	public LucyImage get(int width, int height) {
		// look up by width first
		Map<Integer, LucyImage> m = store.get(width);

		if (m != null) {
			// look up by height
			LucyImage r = m.get(height);

			if (r != null) {
				return r;
			}
		}
		
		return null;
	}
	
	public void store(LucyImage s) {
		int width = s.getWidth();
		int height = s.getHeight();
		
		store.putIfAbsent(width, new HashMap<>());
		Map<Integer, LucyImage> m = store.get(width);
		m.put(height, s);
	}
}
