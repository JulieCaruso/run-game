package adneom.game;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


/**
 * This classe is used to start the game
 */
public class RunGame extends ApplicationAdapter implements InputProcessor {
    private SpriteBatch batch;
    private Texture texture;
    private Texture runner;
    private Texture obstacle;
    private int obstacleX = -1000;
    private int obstacleY;

    private Sprite sprite;

    private float sourceX = 0.1f;

    private ParallaxBackground background;
    private TextureRegion textureRegion;
    private long timestamp;
    private boolean gameStart;
    float w;
    float h;

    private float runnerX = 0f;
    private float runnerY = 0f;
    private float logoX = 350f;
    private float logoY = 750f;
    private Stage stage;
    //indicates if runner jumped
    private boolean isJumped = false;
    private Texture adneom;

    Random rand = new Random();

    int score = 0;

    private Activity activity;
    private Sound dropSound;

    public RunGame(Activity activity){
        this.activity = activity;
    }

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
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

        adneom = getImage();

        textureRegion = new TextureRegion(texture, 0, 0, w, h);
        background = new ParallaxBackground(new ParallaxLayer[]{
                new ParallaxLayer(textureRegion, new Vector2(1, 1), new Vector2(0, 0)),}, w, h, new Vector2(50, 0));


        /*dropSound = Gdx.audio.newSound(Gdx.files.internal("ricochet_com__die_glisser_effet_2.wav"));

        dropSound.setLooping(2,true);
        dropSound.play();*/
    }

    public Texture getImage() {
        Pixmap pixmap = null;
        try {
            URL url = new URL("https://www.barefoot-studio.be/img/JeremyJacquet.png");
            byte[] buff = new byte[1024];
            int bytesRead;
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            InputStream in = url.openConnection().getInputStream();
            while ((bytesRead = in.read(buff)) != -1) {
                bao.write(buff, 0, bytesRead);
            }
            pixmap = new Pixmap(bao.toByteArray(), 0, bao.size());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("E", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("E", e.getMessage());
        }
        return new Texture(pixmap);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());

        long noTouch = System.currentTimeMillis() / 100;
        long diff = noTouch - timestamp;
        if (sourceX > 0.05 && diff > 10 && sourceX - 0.01f > 0) {
            sourceX = sourceX - 0.05f;
        } /*else if(sourceX <= 0.5f ){
            sourceX = 0;
        }*/

        background.render(sourceX);
        batch.begin();
        /*if(sourceX == 0){
        }*/
        drawObstacle(batch);
        batch.draw(runner, runnerX, runnerY);
        batch.draw(adneom, logoX, logoY);
        batch.end();
    }

    /*private void updateUser() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", user.getId());
        jsonObject.addProperty("score", user.getScore());
        Ion.with(this)
                .load("PUT", App.URL + "/users/" + user.getId())
                .setHeader("Content-Type", "application/json")
                .setLogging("ION_LOGGING", Log.VERBOSE)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.i("Adneom", "error " + (e == null));
                        }
                    }
                });
    }*/


    public void drawObstacle(Batch batch) {
        if (obstacleX <= -500) {
            score += 10;
            obstacleX = (int) w + 100;
            obstacleY = (rand.nextInt(50 - 20) + 20);
            int n = rand.nextInt(4);
            switch (n) {
                case 0:
                    obstacle = new Texture("desk.png");
                    break;
                case 1:
                    obstacle = new Texture("laptop.png");
                    break;
                case 2:
                    obstacle = new Texture("adneom_logo.png");
                    break;
                case 3:
                    obstacle = new Texture("plant.png");
                    break;
                default:
                    break;
            }
        } else {
            obstacleX -= (int) (sourceX * 20);
            checkCollision();
        }

        batch.draw(obstacle, obstacleX, obstacleY);
    }

    private void checkCollision () {
        if (obstacle != null) {
            int py = (int)runnerY;
            int oy1 = obstacleY;
            int oy2 = obstacleY + obstacle.getHeight();

            int px = (int)runnerX;
            int ox1 = obstacleX;
            int ox2 = obstacleX + obstacle.getWidth();

            if (py > oy1 && py < oy2 && px > ox1 && px < ox2) {
                sourceX = 0;
            }
        }
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
        runnerX = 0f;
        logoX = 350f;
        isJumped = !isJumped;
        runnerY = (isJumped) ? 550f : 0f;
        logoY = (isJumped) ? 1320f : 750f;
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
        if (diff < 200 && sourceX < 2) {
            sourceX = sourceX + 0.05f;
            timestamp = newTouch;
        }
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