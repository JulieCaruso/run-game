package adneom.moutons_electriques.game.util;

import java.util.HashMap;

import adneom.moutons_electriques.game.App;

public class Cache {

    private final HashMap<String, Object> mData = new HashMap<>();

    @SuppressWarnings("unchecked")
    private <T> T getValue(String key) {
        return (T) mData.get(key);
    }

    private void setValue(String key, Object value) {
        mData.put(key, value);
    }

    public static <T> T get(String key) {
        return App.getCache().getValue(key);
    }

    public static void set(String key, Object value) {
        App.getCache().setValue(key, value);
    }
}
