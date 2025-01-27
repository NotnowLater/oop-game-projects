package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DolphinEnemy extends Enemy{
    BufferedImage[][] sprites;

    public int spriteWalkFrame = 0;
    public int spriteWalkId = 0;
    public int lastSpriteWalkId = 0;

    public Vector2 notActiveWorldPos;
    public Vector2 notActiveHitBoxWorldPos;

    public boolean inAttackRange;

    public boolean movingLeft;

    public boolean shooting;
    public int shootFrame;
    public final int MAX_SHOOT_FRAME = 30;

    public final int MAX_INVINCIBILITY_FRAME = 16;
    public int invincibilityFrame = 0;
    public boolean invincible = false;
    public int hp = 3;

    public DolphinEnemy(GameWindow gW, Vector2 worldPos, String spritePath){
        super(gW, worldPos);
        loadSprites(spritePath);
        notActiveWorldPos = new Vector2(worldPos);
        screenPosition = gW.util.worldPosToScreenPos(worldPos);
        hitBox = new Rectangle(worldPos.x + 27, worldPos.y + 3,32 * gW.TILE_SCALE - 15 * gW.TILE_SCALE, 32 * gW.TILE_SCALE - 2 * gW.TILE_SCALE );
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
            if (!inAttackRange){
                shooting = false;
                if (!gW.tileManager.isTileBlocking(hitBox.x - 2, hitBox.y + hitBox.height + collisionCheckTileOffset) && movingLeft){
                    movingLeft = false;
                } else if (!movingLeft && !gW.tileManager.isTileBlocking(hitBox.x + hitBox.width + 2, hitBox.y + hitBox.height + collisionCheckTileOffset)){
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
                    velocity.y = 10;
                }

            } else {
                // InPlayerRange
                velocity.x = 0;
                if (gW.player.worldPosition.x > worldPosition.x){
                    facing = 1;
                    movingLeft = false;
                } else {
                    facing = 0;
                    movingLeft = true;
                }
                if (shootFrame >= MAX_SHOOT_FRAME && !shooting){
                    shootFrame = 0;
                    shooting = true;
                } else {
                    shootFrame++;
                }

            }
            invincibilityCheck();
            applyVelocity();
            animateSprite();
            screenPosition = gW.util.worldPosToScreenPos(worldPosition);
            checkCollisionWithPlayer();
        } else {
            // respawn
            invincible = false;
            invincibilityCheck();
            worldPosition = new Vector2(notActiveWorldPos);
            hitBox.x = notActiveHitBoxWorldPos.x;
            hitBox.y = notActiveHitBoxWorldPos.y;
            velocity = new Vector2(0, 0);
            screenPosition = gW.util.worldPosToScreenPos(worldPosition);
            hp = 3;
        }
    }

    public void animateSprite(){
        if (!inAttackRange) {
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
        } else {
            if (!shooting){
                spriteWalkId = 0;
            } else {
                if (spriteWalkFrame >= 16){
                    spriteWalkFrame = 0;
                    spriteWalkId++;
                    if (spriteWalkId == 3){
                        shootPlayer();
                    } else if (spriteWalkId == 5){
                        shooting = false;
                        shootFrame = 0;
                        spriteWalkId = 0;
                    }
                } else {
                    spriteWalkFrame++;
                }
            }
        }
    }

    public void shootPlayer(){
        Vector2 spawnPoint;
        if (facing == 0){
            spawnPoint = new Vector2(worldPosition.x - 24, worldPosition.y + 22);
        } else {
            spawnPoint = new Vector2(worldPosition.x + 24, worldPosition.y + 22);
        }
        Projectile p = new DolphinProjectile(gW, new Vector2(spawnPoint));
        p.facing = facing;
        p.velocity.x = 16;
        if (facing == 0){
            p.velocity.x *= -1;
        }
        gW.projectiles.add(p);
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
                if (!shooting){
                    g2d.drawImage(sprites[facing][spriteWalkId], screenPosition.x, screenPosition.y, 32 * gW.TILE_SCALE, 32 * gW.TILE_SCALE, null);
                } else {
                    g2d.drawImage(sprites[facing + 2][spriteWalkId], screenPosition.x, screenPosition.y, 32 * gW.TILE_SCALE, 32 * gW.TILE_SCALE, null);
                }
            }
            gW.util.drawDebugRect(g2d, hitBox);
        }
    }

    public void loadSprites(String spritePath){
        sprites = gW.util.loadGraphic2D(spritePath, 32);
    }
}
