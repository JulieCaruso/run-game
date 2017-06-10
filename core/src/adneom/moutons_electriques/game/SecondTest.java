package adneom.moutons_electriques.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Test Actions
 */

public class SecondTest extends ApplicationAdapter{

    private Stage stage;
    //position:
    private float arrivaleX = 0;
    private float arrivaleY = 0;
    //batch:
    private SpriteBatch batch;
    //image
    private Texture runner;
    //indicates if runner jumped
    private boolean isJumped = false;


    //called first time
    @Override
    public void create() {
        super.create();
        stage = new Stage();
        batch = new SpriteBatch();
        runner = new Texture(Gdx.files.internal("runner.gif"));

        //event
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                arrivaleX = 0;
                isJumped = !isJumped;
                arrivaleY = (isJumped) ? 550f : 0f;
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });

    }

    //called each time there is an event by example
    @Override
    public void render() {
        super.render();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        batch.begin();
        batch.draw(runner,arrivaleX,arrivaleY);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
