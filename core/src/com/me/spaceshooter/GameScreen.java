package com.me.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {

    // screen
    private Camera camera;
    private Viewport viewport;

    // graphics
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private TextureRegion[] backgrounds;

    private TextureRegion playerShipTextureRegion;
    private TextureRegion playerShieldTextureRegion;
    private TextureRegion enemyShipTextureRegion;
    private TextureRegion enemyShieldTextureRegion;
    private TextureRegion playerLaserTextureRegion;
    private TextureRegion enemyLaserTextureRegion;

    // timing
    private float backgroundOffset[] = {0, 0, 0, 0};
    private float backgroundMaxScrollingSpeed;
    private float backgroundHeight;

    // world
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;

    private final float TOUCH_MOVEMENT_THRESHOLD = 0.5f;

    // game objects
    private PlayerShip playerShip;
    private EnemyShip enemyShip;
    private LinkedList<Laser> playerLaserList = new LinkedList<>();
    private LinkedList<Laser> enemyLaserList = new LinkedList<>();

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        atlas = new TextureAtlas("images.atlas");

        backgrounds = new TextureRegion[4];

        backgrounds[0] = atlas.findRegion("Starscape00");
        backgrounds[1] = atlas.findRegion("Starscape01");
        backgrounds[2] = atlas.findRegion("Starscape02");
        backgrounds[3] = atlas.findRegion("Starscape03");

        backgroundMaxScrollingSpeed = WORLD_HEIGHT / 4f;
        backgroundHeight = WORLD_HEIGHT * 2;

        batch = new SpriteBatch();

        playerShipTextureRegion = atlas.findRegion("playerShip2_blue");
        playerShieldTextureRegion = atlas.findRegion("shield2");
        enemyShipTextureRegion = atlas.findRegion("enemyRed3");
        enemyShieldTextureRegion = atlas.findRegion("shield1");
        enemyShieldTextureRegion.flip(false, true);
        playerLaserTextureRegion = atlas.findRegion("laserBlue03");
        enemyLaserTextureRegion = atlas.findRegion("laserRed02");

        playerShip = new PlayerShip(
                48, 3,
                10, 10,
                WORLD_WIDTH / 2, WORLD_HEIGHT / 4,
                0.4f, 4, 45, 0.5f,
                playerShipTextureRegion, playerShieldTextureRegion, playerLaserTextureRegion);

        enemyShip = new EnemyShip(
                48, 1,
                10, 10,
                SpaceShooterGame.random.nextFloat() * (WORLD_WIDTH - 10) + 5, WORLD_HEIGHT - 5,
                0.3f, 5, 50, 0.8f,
                enemyShipTextureRegion, enemyShieldTextureRegion, enemyLaserTextureRegion);

    }

    @Override
    public void render(float delta) {

        batch.begin();

        detectInput(delta);

        moveEnemies(delta);

        playerShip.update(delta);
        enemyShip.update(delta);

        // scrolling background
        renderBackground(delta);

        enemyShip.draw(batch);

        playerShip.draw(batch);

        renderLasers(delta);

        detectCollisions();

        batch.end();
    }

    private void moveEnemies(float delta) {
        float leftLimit = -enemyShip.boundingBox.x;
        float rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        float upLimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;
        float downLimit = WORLD_HEIGHT / 2f - enemyShip.boundingBox.y;


        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * delta;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * delta;

        if (xMove > 0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);

        if (yMove > 0) yMove = Math.min(yMove, upLimit);
        else yMove = Math.max(yMove, downLimit);

        enemyShip.translate(xMove, yMove);

    }

    private void detectInput(float delta) {
        // keyboard

        float leftLimit = -playerShip.boundingBox.x;
        float rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        float upLimit = WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height;
        float downLimit = -playerShip.boundingBox.y;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            float xChange = playerShip.movementSpeed * delta;
            xChange = Math.min(xChange, rightLimit);
            playerShip.translate(xChange, 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            float yChange = playerShip.movementSpeed * delta;
            yChange = Math.min(yChange, upLimit);
            playerShip.translate(0f, yChange);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {

            float xChange = -playerShip.movementSpeed * delta;
            xChange = Math.max(xChange, leftLimit);
            playerShip.translate(xChange, 0f);

        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {

            float yChange = -playerShip.movementSpeed * delta;
            yChange = Math.max(yChange, downLimit);
            playerShip.translate(0f, yChange);
        }

        // touch
        if (Gdx.input.isTouched()) {
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            Vector2 touchPoint = new Vector2(xTouchPixels, yTouchPixels);
            touchPoint = viewport.unproject(touchPoint);

            Vector2 playerShipCentre = new Vector2(playerShip.boundingBox.x + playerShip.boundingBox.width / 2, playerShip.boundingBox.y + playerShip.boundingBox.height / 2);

            float touchDistance = touchPoint.dst(playerShipCentre);

            if (touchDistance > TOUCH_MOVEMENT_THRESHOLD) {

                float xTouchDifference = touchPoint.x - playerShipCentre.x;
                float yTouchDifference = touchPoint.y - playerShipCentre.y;

                float xMove = xTouchDifference / touchDistance * playerShip.movementSpeed * delta;
                float yMove = yTouchDifference / touchDistance * playerShip.movementSpeed * delta;

                if (xMove > 0) xMove = Math.min(xMove, rightLimit);
                else xMove = Math.max(xMove, leftLimit);

                if (yMove > 0) yMove = Math.min(yMove, upLimit);
                else yMove = Math.max(yMove, downLimit);

                playerShip.translate(xMove, yMove);
            }

        }
    }

    private void detectCollisions() {
        ListIterator<Laser> iterator = playerLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            if (enemyShip.intersects(laser.getBoundingBox())) {
                enemyShip.hit(laser);
                iterator.remove();
            }
        }

        iterator = enemyLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            if (playerShip.intersects(laser.getBoundingBox())) {
                playerShip.hit(laser);
                iterator.remove();
            }
        }
    }

    private void renderLasers(float delta) {
        if (playerShip.canFireLaser()) {
            Laser[] lasers = playerShip.fireLasers();
            for (Laser laser : lasers) {
                playerLaserList.add(laser);
            }
        }

        if (enemyShip.canFireLaser()) {
            Laser[] lasers = enemyShip.fireLasers();
            for (Laser laser : lasers) {
                enemyLaserList.add(laser);
            }
        }

        ListIterator<Laser> iterator = playerLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed * delta;

            if (laser.boundingBox.y > WORLD_HEIGHT) {
                iterator.remove();
            }
        }

        ListIterator<Laser> enemyIterator = enemyLaserList.listIterator();
        while (enemyIterator.hasNext()) {
            Laser laser = enemyIterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed * delta;

            if (laser.boundingBox.y + laser.boundingBox.height < 0) {
                enemyIterator.remove();
            }
        }
    }

    private void renderBackground(float delta) {

        backgroundOffset[0] += delta * backgroundMaxScrollingSpeed / 8;
        backgroundOffset[1] += delta * backgroundMaxScrollingSpeed / 4;
        backgroundOffset[2] += delta * backgroundMaxScrollingSpeed / 2;
        backgroundOffset[3] += delta * backgroundMaxScrollingSpeed;

        for (int i = 0; i < backgroundOffset.length; i++) {
            if (backgroundOffset[i] > WORLD_HEIGHT) {
                backgroundOffset[i] = 0;
            }

            batch.draw(backgrounds[i], 0, -backgroundOffset[i], WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(backgrounds[i], 0, -backgroundOffset[i] + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    // the resize method is for when you change the size of the window but it also runs at very beginning of the application
    // it starts up with this resize method
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
