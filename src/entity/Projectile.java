package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Projectile extends Entity{

    BufferedImage[][] sprites;
    private Rectangle hitBox;
    private Vector2 screenPosition;
    private int facing = 0;

    public Projectile(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
    }

    public abstract void checkCollisionWithEntity();


    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public Vector2 getScreenPosition() {
        return screenPosition;
    }

    public void setScreenPosition(Vector2 screenPosition) {
        this.screenPosition = screenPosition;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }
}
