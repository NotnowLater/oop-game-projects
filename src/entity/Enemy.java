package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public abstract class Enemy extends Entity {

    private int collisionCheckTileOffset = 4;

    private Rectangle hitBox;
    private Vector2 screenPosition;
    private int facing = 0;

    private Vector2 velocity = new Vector2(0, 0);

    private boolean isActive;
    private boolean isDead;

    public Enemy(GameWindow gW, Vector2 worldPos){
        super(gW, worldPos);
    }

    public void checkCollisionWithPlayer(){
        if (getHitBox().intersects(getgW().player.getHitBox())){
            getgW().player.onHit(getWorldPosition());
        }
    }

    public void animateSprite(){

    }

    public void applyVelocity(){
        // apply x
        if (!getgW().tileManager.checkRectNotIntersectAnyTile(getHitBox().x + getVelocity().getX(), getHitBox().y, getHitBox().width, getHitBox().height)) {
            int tileSnapPosX = 0;
            if (getVelocity().getX() > 0) {
                tileSnapPosX = ((getHitBox().x + getHitBox().width + getVelocity().getX() + 2) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE;
                getVelocity().setX(((getHitBox().x + getHitBox().width + 1) - tileSnapPosX) * -1);
            } else {
                tileSnapPosX = (((getHitBox().x + getVelocity().getX() - 2) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE) + getgW().RENDER_TILE_SIZE;
                getVelocity().setX(((getHitBox().x - 1) - tileSnapPosX) * -1);
//                velocity.x = 0;
            }
        }
        // apply y
        if (!getgW().tileManager.checkRectNotIntersectAnyTile(getHitBox().x, getHitBox().y + getVelocity().getY(), getHitBox().width, getHitBox().height)) {
            int tileSnapPosY = 0;
//            System.out.println(tileSnapPosY);
            if (getVelocity().getY() > 0) {
                tileSnapPosY = ((getHitBox().y + getHitBox().height + getVelocity().getY() + 4) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE;
                getVelocity().setY(((getHitBox().y + getHitBox().height + 1) - tileSnapPosY) * -1);
            } else {
                tileSnapPosY = (((getHitBox().y + getVelocity().getY() - 4) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE) + getgW().RENDER_TILE_SIZE;
                getVelocity().setY(Math.abs(((getHitBox().y - 1) - tileSnapPosY)));
//                velocity.y = 0;
            }
        }
        getWorldPosition().setX(getWorldPosition().getX() + getVelocity().getX());
        getWorldPosition().setY(getWorldPosition().getY() + getVelocity().getY());
        getHitBox().x += getVelocity().getX();
        getHitBox().y += getVelocity().getY();
    }

    boolean isOnGround(){
        return (getgW().tileManager.isTileBlocking(getHitBox().x, getHitBox().y + getHitBox().height + getCollisionCheckTileOffset()) ||
                getgW().tileManager.isTileBlocking(getHitBox().x + getHitBox().width, getHitBox().y + getHitBox().height + getCollisionCheckTileOffset())) &&
                (((getHitBox().y + getHitBox().height + getVelocity().getY() + 1) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE) - (getHitBox().y + getHitBox().height) < 2;
    }


    abstract void loadSprites(String spritePath);

    public int getCollisionCheckTileOffset() {
        return collisionCheckTileOffset;
    }

    public void setCollisionCheckTileOffset(int collisionCheckTileOffset) {
        this.collisionCheckTileOffset = collisionCheckTileOffset;
    }

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

    @Override
    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
