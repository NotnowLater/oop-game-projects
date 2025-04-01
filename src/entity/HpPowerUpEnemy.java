package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HpPowerUpEnemy extends Enemy {
    private static BufferedImage sprite;

    private boolean used = false;

    public HpPowerUpEnemy(GameWindow gW, Vector2 worldPos, String spritePath){
        super(gW, worldPos);
        loadSprites(spritePath);
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPos));
        setHitBox(new Rectangle(worldPos.getX() + 18, worldPos.getY() + 24,11 * gW.TILE_SCALE, 11 * gW.TILE_SCALE));
    }

    @Override
    public void process() {
        if (!isUsed()){
            setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
            if (getHitBox().intersects(getgW().getPlayer().getHitBox()) && getgW().getPlayer().getHp() < getgW().getPlayer().getMAX_HP()){
                getgW().getPlayer().applyDirectDamage(-2);
//            getgW().getEntitiesToDelete().add(this);
                setUsed(true);
            }
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!isUsed()){
            g2d.drawImage(sprite, getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            getgW().getUtil().drawDebugRect(g2d, getHitBox());
        }
    }

    public void loadSprites(String spritePath){
        if (sprite == null){
            sprite = getgW().getUtil().loadGraphic(spritePath);
        }
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
