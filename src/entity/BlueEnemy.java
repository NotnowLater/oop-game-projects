package entity;

import main.GameWindow;
import main.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BlueEnemy extends Enemy{
    public BufferedImage[][] sprites;

    public int spriteWalkFrame = 0;
    public int spriteWalkId = 0;
    public int lastSpriteWalkId = 0;

    public Vector2 notActiveWorldPos;
    public Vector2 notActiveHitBoxWorldPos;

    public BlueEnemy(GameWindow gW, Vector2 worldPos, String spritePath){
        super(gW, worldPos);
        loadSprites(spritePath);
        notActiveWorldPos = new Vector2(worldPos);
        screenPosition = gW.util.worldPosToScreenPos(worldPos);
        hitBox = new Rectangle(worldPos.x + 12, worldPos.y + 18,24 * gW.TILE_SCALE - 8 * gW.TILE_SCALE, 24 * gW.TILE_SCALE - 7 * gW.TILE_SCALE );
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
            if (!gW.tileManager.isTileBlocking(hitBox.x - 2, hitBox.y + hitBox.height + collisionCheckTileOffset) && facing == 0){
                velocity.x = 4;
                facing = 1;
            } else if (facing == 1 && !gW.tileManager.isTileBlocking(hitBox.x + hitBox.width + 2, hitBox.y + hitBox.height + collisionCheckTileOffset)){
                velocity.x = -4;
                facing = 0;
            }
            if (!isOnGround()){
                velocity.y = 10;
            }
            applyVelocity();
            animateSprite();
            screenPosition = gW.util.worldPosToScreenPos(worldPosition);
            checkCollisionWithPlayer();
        } else {
            worldPosition = new Vector2(notActiveWorldPos);
            hitBox.x = notActiveHitBoxWorldPos.x;
            hitBox.y = notActiveHitBoxWorldPos.y;
            velocity = new Vector2(0, 0);
            screenPosition = gW.util.worldPosToScreenPos(worldPosition);
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!isDead && isActive){
            g2d.drawImage(sprites[facing][spriteWalkId], screenPosition.x, screenPosition.y, 24 * gW.TILE_SCALE, 24 * gW.TILE_SCALE, null);
            gW.util.drawDebugRect(g2d, hitBox);
        }
    }

    public void animateSprite(){
        if (spriteWalkFrame >= 8){
            spriteWalkFrame = 0;
            if (spriteWalkId == 0){
                if (lastSpriteWalkId == 2){
                    spriteWalkId = 1;
                    lastSpriteWalkId = spriteWalkId;
                } else {
                    spriteWalkId = 2;
                    lastSpriteWalkId = spriteWalkId;
                }
            } else {
                spriteWalkId =0;
            }
        } else {
            spriteWalkFrame ++;
        }
    }

    @Override
    public void onHit(Vector2 hitPos){
        isDead = true;
        gW.effects.add(gW.enemyFactory.getEnemy(-1, new Vector2(worldPosition)));
    }

    @Override
    void loadSprites(String spritePath){
        try{
            BufferedImage full = ImageIO.read(getClass().getClassLoader().getResourceAsStream(spritePath));
            sprites = new BufferedImage[full.getHeight() / 24][full.getWidth() / 24];
            for (int i = 0; i < full.getHeight() / 24; i++) {
                for (int j = 0; j < full.getWidth() / 24; j++) {
                    sprites[i][j] = full.getSubimage(j * 24, i * 24, 24, 24);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
