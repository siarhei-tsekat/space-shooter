package com.me.spaceshooter;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    // screen
    private Camera camera;
    private Viewport viewport;

    // graphics
    private SpriteBatch batch;
    private Texture[] backgrounds;

    // timing
    private float backgroundOffset[] = {0, 0, 0, 0};
    private float backgroundMaxScrollingSpeed;

    // world
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;


    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
//        background = new Texture("bg.png");
        backgrounds = new Texture[4];

        backgrounds[0] = new Texture("Starscape00.png");
        backgrounds[1] = new Texture("Starscape01.png");
        backgrounds[2] = new Texture("Starscape02.png");
        backgrounds[3] = new Texture("Starscape03.png");

        backgroundMaxScrollingSpeed = WORLD_HEIGHT / 4f;

        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {

        batch.begin();

        // scrolling background
        renderBackground(delta);

        batch.end();
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
