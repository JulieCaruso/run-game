package adneom.moutons_electriques.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class RunGame extends ApplicationAdapter implements InputProcessor {
    private SpriteBatch batch;
    private Texture texture;
    private Texture runner;

    private Sprite sprite;

    private float sourceX = 0.1f;

    private ParallaxBackground background;
    private TextureRegion textureRegion;
    private long timestamp;
    private boolean gameStart;
    float w ;
    float h;

    private int arrivaleX = 0;
    private int arrivaleY = 0;
    int w2;
    int h2;
    private Stage stage;


    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
         w = Gdx.graphics.getWidth();
         h = Gdx.graphics.getHeight();
        w2 = (int)w;
        h2 = (int)h;
        batch = new SpriteBatch();
        gameStart = false;

        stage = new Stage();
        runner = new Texture("runner.gif");

        Pixmap pixmap = new Pixmap(Gdx.files.internal("bg.png"));
        texture = new Texture(pixmap);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        sprite = new Sprite(texture);
        sprite.setPosition(0, 0);
        sprite.setSize(w, h);

        textureRegion = new TextureRegion(texture, 0, 0, w, h);
        background = new ParallaxBackground(new ParallaxLayer[]{
                new ParallaxLayer(textureRegion, new Vector2(1, 1), new Vector2(0, 0)), }, w, h, new Vector2(50, 0));

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        arrivaleX = Math.abs(arrivaleX);
        arrivaleY = Math.abs(arrivaleY);

        long noTouch = System.currentTimeMillis() / 100;
        long diff = noTouch - timestamp;
        if (sourceX > 0 && diff > 10 && sourceX - 0.01f > 0) {
            sourceX = sourceX - 0.01f;
        }

        background.render(sourceX);
        batch.begin();
        batch.draw(runner,arrivaleX,arrivaleY);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
        runner.dispose();
    }


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
        arrivaleX= screenX;
        arrivaleY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (gameStart == false) {
            gameStart = true;
            timestamp = System.currentTimeMillis() / 100;
        }
        long newTouch = System.currentTimeMillis() / 100;
        long diff = newTouch - timestamp;
        System.out.println("Diff" + diff);
        if (diff < 200 && sourceX < 2) {
            sourceX = sourceX + 0.1f;
            timestamp = newTouch;
        }
        System.out.println("sub" + diff);
        return true;
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
}