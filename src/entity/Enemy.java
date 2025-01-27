package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public abstract class Enemy extends Entity {

    public int collisionCheckTileOffset = 4;

    public Rectangle hitBox;
    public Vector2 screenPosition;
    public int facing = 0;

    public Vector2 velocity = new Vector2(0, 0);

    public boolean isActive;
    public boolean isDead;

    public Enemy(GameWindow gW, Vector2 worldPos){
        super(gW, worldPos);
    }

    public void checkCollisionWithPlayer(){
        if (hitBox.intersects(gW.player.hitBox)){
            gW.player.onHit(worldPosition);
        }
    }

    public void animateSprite(){

    }

    public void applyVelocity(){
        // apply x
        if (!gW.tileManager.checkRectNotIntersectAnyTile(hitBox.x + velocity.x, hitBox.y, hitBox.width, hitBox.height)) {
            int tileSnapPosX = 0;
            if (velocity.x > 0) {
                tileSnapPosX = ((hitBox.x + hitBox.width + velocity.x + 2) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE;
                velocity.x = ((hitBox.x + hitBox.width + 1) - tileSnapPosX) * -1;
            } else {
                tileSnapPosX = (((hitBox.x + velocity.x - 2) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE) + gW.RENDER_TILE_SIZE;
                velocity.x = ((hitBox.x - 1) - tileSnapPosX) * -1;
//                velocity.x = 0;
            }
        }
        // apply y
        if (!gW.tileManager.checkRectNotIntersectAnyTile(hitBox.x, hitBox.y + velocity.y, hitBox.width, hitBox.height)) {
            int tileSnapPosY = 0;
//            System.out.println(tileSnapPosY);
            if (velocity.y > 0) {
                tileSnapPosY = ((hitBox.y + hitBox.height + velocity.y + 4) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE;
                velocity.y = ((hitBox.y + hitBox.height + 1) - tileSnapPosY) * -1;
            } else {
                tileSnapPosY = (((hitBox.y + velocity.y - 4) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE) + gW.RENDER_TILE_SIZE;
                velocity.y = Math.abs(((hitBox.y - 1) - tileSnapPosY));
//                velocity.y = 0;
            }
        }
        worldPosition.x += velocity.x;
        worldPosition.y += velocity.y;
        hitBox.x += velocity.x;
        hitBox.y += velocity.y;
    }

    boolean isOnGround(){
        return (gW.tileManager.isTileBlocking(hitBox.x, hitBox.y + hitBox.height + collisionCheckTileOffset) ||
                gW.tileManager.isTileBlocking(hitBox.x + hitBox.width, hitBox.y + hitBox.height + collisionCheckTileOffset)) &&
                (((hitBox.y + hitBox.height + velocity.y + 1) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE) - (hitBox.y + hitBox.height) < 2;
    }


    abstract void loadSprites(String spritePath);
}
