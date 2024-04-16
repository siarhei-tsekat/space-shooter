package com.me.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Ship {

    float movementSpeed; // world units per second
    int shield;

    float xPosition; // lower-left corner
    float yPosition;
    float width;
    float height;

    TextureRegion shipTextureRegion;
    TextureRegion shieldTextureRegion;
    TextureRegion laserTextureRegion;

    float laserWidth;
    float laserHeight;
    float laserMovementSpeed;
    float timeBetweenShots;
    float timeSinceLastShot = 0;

    public Ship(float movementSpeed, int shield,
                float width, float height,
                float xCentre, float yCentre,
                float laserWidth, float laserHeight,
                float laserMovementSpeed, float timeBetweenShots,
                TextureRegion shipTextureRegion,
                TextureRegion shieldTextureRegion,
                TextureRegion laserTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.xPosition = xCentre - width / 2;
        this.yPosition = yCentre - height / 2;
        this.width = width;
        this.height = height;
        this.shipTextureRegion = shipTextureRegion;
        this.shieldTextureRegion = shieldTextureRegion;
        this.laserTextureRegion = laserTextureRegion;
        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
    }

    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canFireLaser() {
        return timeSinceLastShot - timeBetweenShots >= 0;
    }

    public abstract Laser[] fireLasers();

    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, xPosition, yPosition, width, height);
        if (shield > 0) {
            batch.draw(shieldTextureRegion, xPosition, yPosition, width, height);
        }
    }
}
