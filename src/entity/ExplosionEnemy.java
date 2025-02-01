package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ExplosionEnemy extends Enemy{

     BufferedImage[] sprites;

     private int currentFrame = 0;
     private int animFrame = 0;
     final int MAX_ANIM_FRAME = 2;

    public ExplosionEnemy(GameWindow gW, Vector2 worldPos, String spritePath){
        super(gW, worldPos);
        loadSprites(spritePath);
        setScreenPosition(gW.util.worldPosToScreenPos(worldPos));
//        hitBox = new Rectangle(worldPos.x + 12, worldPos.y + 18,24 * gW.TILE_SCALE - 8 * gW.TILE_SCALE, 24 * gW.TILE_SCALE - 7 * gW.TILE_SCALE );
        setActive(false);
        setDead(false);
    }

    @Override
    public void process(){
        setScreenPosition(getgW().util.worldPosToScreenPos(getWorldPosition()));
        if (animFrame >= MAX_ANIM_FRAME){
            currentFrame++;
            animFrame = 0;
            if (currentFrame >= sprites.length){
                getgW().entitiesToDelete.add(this);
            }
        } else {
            animFrame ++;
        }
    }

    @Override
    public void render(Graphics2D g2d){
        g2d.drawImage(sprites[currentFrame], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
    }

    @Override
    public void loadSprites(String spritePath){
        sprites = getgW().util.loadGraphic1D(spritePath, 24);
    }
}
