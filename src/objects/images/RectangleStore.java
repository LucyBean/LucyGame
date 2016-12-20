package objects.images;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Color;

class RectangleImageStore {
	private Map<Integer, Map<Integer, LucyImage>> store;
	private Color color;

	public RectangleImageStore(Color c) {
		store = new HashMap<>();
		color = c;
	}

	public LucyImage get(int width, int height) {
		// look up by width first
		store.putIfAbsent(width, new HashMap<>());
		Map<Integer, LucyImage> m = store.get(width);

		// look up by height
		LucyImage r = m.get(height);

		if (r != null) {
			// Return existing
			return r;
		} else {
			// Make a new one
			Color fill = color;
			Color border = fill.darker(0.5f);
			border.a = 220;
			LucyImage s = ImageBuilder.makeRectangle(width, height, fill,
					border);
			m.put(height, s);
			return s;
		}
	}
}
