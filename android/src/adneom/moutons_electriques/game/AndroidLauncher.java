package adneom.moutons_electriques.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * This classe is used to launch the Game
 */
public class AndroidLauncher extends AndroidApplication {


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new RunGame(), config);
		//initialize(new SecondTest(),config);
	}
}
