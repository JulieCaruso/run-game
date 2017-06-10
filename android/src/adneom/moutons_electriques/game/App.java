package adneom.moutons_electriques.game;

import android.app.Application;

import adneom.moutons_electriques.game.util.Cache;

public class App extends Application {

    public static final String URL = "https://vast-crag-92889.herokuapp.com/api";
    public static final String KEY_USERS = "users";
    public static final String EXTRA_USER = "user";

    private static App sInstance;
    private Cache mCache;

    public static App getInstance() {
        return sInstance;
    }

    public static void setInstance(App instance) {
        sInstance = instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);

        mCache = new Cache();
    }

    public static Cache getCache() {
        return sInstance.mCache;
    }
}
