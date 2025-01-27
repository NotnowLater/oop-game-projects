package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ExplosionEnemy extends Enemy{

    public BufferedImage[] sprites;

    public int currentFrame = 0;
    public int animFrame = 0;
    public final int MAX_ANIM_FRAME = 2;

    public ExplosionEnemy(GameWindow gW, Vector2 worldPos, String spritePath){
        super(gW, worldPos);
        loadSprites(spritePath);
        screenPosition = gW.util.worldPosToScreenPos(worldPos);
//        hitBox = new Rectangle(worldPos.x + 12, worldPos.y + 18,24 * gW.TILE_SCALE - 8 * gW.TILE_SCALE, 24 * gW.TILE_SCALE - 7 * gW.TILE_SCALE );
        isActive = false;
        isDead = false;
    }

    @Override
    public void process(){
        screenPosition = gW.util.worldPosToScreenPos(worldPosition);
        if (animFrame >= MAX_ANIM_FRAME){
            currentFrame++;
            animFrame = 0;
            if (currentFrame >= sprites.length){
                gW.entitiesToDelete.add(this);
            }
        } else {
            animFrame ++;
        }
    }

    @Override
    public void render(Graphics2D g2d){
        g2d.drawImage(sprites[currentFrame], screenPosition.x, screenPosition.y, 24 * gW.TILE_SCALE, 24 * gW.TILE_SCALE, null);
    }

    @Override
    public void loadSprites(String spritePath){
        sprites = gW.util.loadGraphic1D(spritePath, 24);
    }
}
