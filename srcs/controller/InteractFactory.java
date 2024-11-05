package controller;

import model.*;

public class InteractFactory {
	public static Interactable create(Storable s) {
		if (s instanceof Key) return new KeyControl((Key)s);
		if (s instanceof Map) return new MapControl((Map)s);
		if (s instanceof Exit) return new ExitControl((Exit)s);
		return null;
	}
}
