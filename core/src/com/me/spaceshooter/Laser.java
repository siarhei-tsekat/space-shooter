package com.me.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser {

    Rectangle boundingBox;
    float movementSpeed; // world units per second

    TextureRegion textureRegion;

    public Laser(float xPosition, float yPosition, float width, float height, float movementSpeed, TextureRegion textureRegion) {
        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
        this.boundingBox = new Rectangle(xPosition - width / 2, yPosition, width, height);
    }

    public void draw(Batch batch) {
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
