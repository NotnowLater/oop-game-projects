package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PenguinEnemy extends Enemy{
    BufferedImage[][] sprites;

    public int spriteWalkFrame = 0;
    public int spriteWalkId = 0;
    public int lastSpriteWalkId = 0;

    public Vector2 notActiveWorldPos;
    public Vector2 notActiveHitBoxWorldPos;

    public boolean inAttackRange;

    public boolean movingLeft;

    public boolean canJump;
    public boolean jumping;
    public int jumpToPlayerFacing = 0;
    public int jumpFrame;
    public final int MAX_SHOOT_FRAME = 30;

    public final int MAX_INVINCIBILITY_FRAME = 16;
    public int invincibilityFrame = 0;
    public boolean invincible = false;
    public int hp = 3;

    public int currentState = 0;

    public PenguinEnemy(GameWindow gW, Vector2 worldPos, String spritePath){
        super(gW, worldPos);
        loadSprites(spritePath);
        notActiveWorldPos = new Vector2(worldPos);
        screenPosition = gW.util.worldPosToScreenPos(worldPos);
        hitBox = new Rectangle(worldPos.x + 27, worldPos.y + 3,24 * gW.TILE_SCALE - 15 * gW.TILE_SCALE, 24 * gW.TILE_SCALE - 2 * gW.TILE_SCALE );
        notActiveHitBoxWorldPos = new Vector2(hitBox.x, hitBox.y);
        isActive = false;
        isDead = false;
    }

    @Override
    public void process(){
        if (gW.util.isRectOnScreen(hitBox) && !isActive && !isDead){
            isActive = true;
        } else if (!gW.util.isRectOnScreen(hitBox) && isDead){
            isDead = false;
            isActive = false;
        }
        if (!isDead && isActive){
            inAttackRange = worldPosition.distanceTo(gW.player.worldPosition) <= 456;
            // walking state
            if (currentState == 0){
                canJump = false;
                if (movingLeft && (!gW.tileManager.isTileBlocking(hitBox.x - 2, hitBox.y + hitBox.height + collisionCheckTileOffset) || gW.tileManager.isTileBlocking(hitBox.x - collisionCheckTileOffset, hitBox.y))){
                    movingLeft = false;
                } else if (!movingLeft && (!gW.tileManager.isTileBlocking(hitBox.x + hitBox.width + 2, hitBox.y + hitBox.height + collisionCheckTileOffset) || gW.tileManager.isTileBlocking(hitBox.x + hitBox.width + collisionCheckTileOffset, hitBox.y))){
                    movingLeft = true;
                }
                if (!movingLeft){
                    velocity.x = 4;
                    facing = 1;
                } else {
                    velocity.x = -4;
                    facing = 0;
                }
                if (!isOnGround()){
                    velocity.y += 2;
                    if (velocity.y > 10){
                        velocity.y = 10;
                    }
                }
                if (inAttackRange){
                    changeStateTo(1);
                    if (gW.player.worldPosition.x <= worldPosition.x){
                        facing = 0;
                        movingLeft = true;
                    } else {
                        facing = 1;
                        movingLeft = false;
                    }
                    spriteWalkId = 0;
                    spriteWalkFrame =0;
                    velocity.x = 0;
                }
            }
            // jump state
            else if(currentState == 2){
                if (velocity.y <= 10){
                    velocity.y += 1;
                    if (movingLeft){
                        velocity.x = -14;
                    } else {
                        velocity.x = 14;
                    }
                }
                if (velocity.y > 0){
                    if (movingLeft){
                        velocity.x = -8;
                    } else {
                        velocity.x = 8;
                    }
                    if (isOnGround()){
                        changeStateTo(0);
                        spriteWalkId = 0;
                        velocity.x = 0;
                    }
                }
            }
            invincibilityCheck();
            applyVelocity();
            animateSprite();
            screenPosition = gW.util.worldPosToScreenPos(worldPosition);
            checkCollisionWithPlayer();
        } else {
            // respawn
            changeStateTo(0);
            invincible = false;
            invincibilityCheck();
            movingLeft = gW.player.worldPosition.x <= worldPosition.x;
            worldPosition = new Vector2(notActiveWorldPos);
            hitBox.x = notActiveHitBoxWorldPos.x;
            hitBox.y = notActiveHitBoxWorldPos.y;
            velocity = new Vector2(0, 0);
            screenPosition = gW.util.worldPosToScreenPos(worldPosition);
            hp = 3;
        }
    }

    public void animateSprite(){
        if (currentState == 0) {
            if (spriteWalkFrame >= 8) {
                spriteWalkFrame = 0;
                if (spriteWalkId == 0) {
                    if (lastSpriteWalkId == 2) {
                        spriteWalkId = 1;
                        lastSpriteWalkId = spriteWalkId;
                    } else {
                        spriteWalkId = 2;
                        lastSpriteWalkId = spriteWalkId;
                    }
                } else {
                    spriteWalkId = 0;
                }
            } else {
                spriteWalkFrame++;
            }
        } else if (currentState == 1){
            if (spriteWalkFrame >= 8){
                spriteWalkFrame = 0;
                spriteWalkId++;
                if (spriteWalkId == 3){
                    velocity.y = -12;
                    spriteWalkId++;
                    changeStateTo(2);
                }
            } else {
                spriteWalkFrame++;
            }
        }
    }

    public void changeStateTo(int newState){
        if (newState != currentState){
            currentState = newState;
        }
    }

    public void setUpJumpToPlayer(boolean falling){
        jumping = !falling;
        velocity.x = 0;
        velocity.y = 0;
        if (!falling){
            if (gW.player.worldPosition.x <= worldPosition.x){
                jumpToPlayerFacing = -1;
                movingLeft = true;
            } else {
                jumpToPlayerFacing = 1;
                movingLeft = false;
            }
        }
    }

    @Override
    public void onHit(Vector2 hitPos){
        if (!invincible){
            hp --;
            invincible = true;
            if (hp <= 0){
                isDead = true;
                gW.effects.add(gW.enemyFactory.getEnemy(-1, new Vector2(worldPosition)));
            }
        }
    }

    public void invincibilityCheck(){
        if (invincible){
            if (invincibilityFrame >= MAX_INVINCIBILITY_FRAME){
                invincible = false;
                invincibilityFrame = 0;
            } else {
                invincibilityFrame++;
            }

        }
    }

    @Override
    public void render(Graphics2D g2d){
        if (!isDead && isActive){
            if (invincibilityFrame % 2 == 0) {
                if (currentState == 0){
                    g2d.drawImage(sprites[facing][spriteWalkId], screenPosition.x, screenPosition.y, 24 * gW.TILE_SCALE, 24 * gW.TILE_SCALE, null);
                } else {
                    g2d.drawImage(sprites[facing + 2][Math.clamp(spriteWalkId, 0, 4)], screenPosition.x, screenPosition.y, 24 * gW.TILE_SCALE, 24 * gW.TILE_SCALE, null);
                }
            }
            gW.util.drawDebugRect(g2d, hitBox);
        }
    }

    public void loadSprites(String spritePath){
        sprites = gW.util.loadGraphic2D(spritePath, 24);
    }
}
