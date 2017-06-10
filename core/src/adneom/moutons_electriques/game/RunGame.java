package adneom.moutons_electriques.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import sun.rmi.runtime.Log;

public class RunGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture texture;
    private Texture runner;

    private Sprite sprite;

    private float sourceX = 0;

    private OrthographicCamera camera;
    private ParallaxBackground background;
    private TextureRegion textureRegion;


    @Override
    public void create() {
        /*float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();*/
        batch = new SpriteBatch();

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

        //camera = new OrthographicCamera(30, 30 * (h / w));
        //camera.update();
    }

    @Override
    public void render() {
        //camera.update();

        sourceX = 1;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        //batch.draw(sprite, sourceX, 0, 0, 0);
        //sprite.setPosition(sourceX, 0);

        //sprite.draw(batch);
        background.render(sourceX);
        batch.draw(runner, 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
        runner.dispose();
    }


}
