package com.me.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemyShip extends Ship {

    public EnemyShip(float movementSpeed, int shield,
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
        lasers[0] = new Laser(xPosition + width * 0.18f, yPosition - height, laserWidth, laserHeight, laserMovementSpeed, laserTextureRegion);
        lasers[1] = new Laser(xPosition + width * 0.82f, yPosition - height, laserWidth, laserHeight, laserMovementSpeed, laserTextureRegion);

        timeSinceLastShot = 0;

        return lasers;
    }

    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, xPosition, yPosition, width, height);
        if (shield > 0) {
            batch.draw(shieldTextureRegion, xPosition, yPosition - height * 0.2f, width, height);
        }
    }
}