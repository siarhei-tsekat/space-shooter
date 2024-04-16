package com.me.spaceshooter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship {

    public PlayerShip(float movementSpeed, int shield,
                      float width, float height,
                      float xCentre, float yCentre,
                      float laserWidth, float laserHeight,
                      float laserMovementSpeed, float timeBetweenShots,
                      TextureRegion shipTextureRegion,
                      TextureRegion shieldTextureRegion,
                      TextureRegion laserTextureRegion) {
        super(movementSpeed, shield, width, height, xCentre, yCentre, laserWidth, laserHeight, laserMovementSpeed, timeBetweenShots, shipTextureRegion, shieldTextureRegion, laserTextureRegion);
    }

    @Override
    public Laser[] fireLasers() {

        Laser[] lasers = new Laser[2];
        lasers[0] = new Laser(xPosition + width * 0.07f, yPosition + height * 0.45f, laserWidth, laserHeight, laserMovementSpeed, laserTextureRegion);
        lasers[1] = new Laser(xPosition + width * 0.93f, yPosition + height * 0.45f, laserWidth, laserHeight, laserMovementSpeed, laserTextureRegion);

        timeSinceLastShot = 0;

        return lasers;
    }
}
