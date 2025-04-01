package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EnergyPowerUpEnemy extends Enemy {

    private static BufferedImage normalSprite;
    private static BufferedImage waterSprite;
    private static BufferedImage fireSprite;
    private static BufferedImage grassSprite;

    private boolean used = false;

    public EnergyPowerUpEnemy(GameWindow gW, Vector2 worldPos, String spritePath){
        super(gW, worldPos);
        loadSprites(spritePath);
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPos));
        setHitBox(new Rectangle(worldPos.getX() + 18, worldPos.getY() + 24,11 * gW.TILE_SCALE, 11 * gW.TILE_SCALE));
    }

    @Override
    public void process() {
        if (!isUsed()) {
            setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
            if (getHitBox().intersects(getgW().getPlayer().getHitBox()) &&
                    ((getgW().getPlayer().getHp() < getgW().getPlayer().getMAX_HP() && getgW().getPlayer().getCurrentElement() == 0) ||
                            (getgW().getPlayer().getWaterElementEnergy() < 24 && getgW().getPlayer().getCurrentElement() == 1)
                            || (getgW().getPlayer().getFireElementEnergy() < 24 && getgW().getPlayer().getCurrentElement() == 2)
                            || (getgW().getPlayer().getGrassElementEnergy() < 24 && getgW().getPlayer().getCurrentElement() == 3))) {

                if (getgW().getPlayer().getCurrentElement() == 0) {
                    getgW().getPlayer().applyDirectDamage(-1);
                } else if (getgW().getPlayer().getCurrentElement() == 1) {
                    getgW().getPlayer().addElementEnergy(1, 8);
                } else if (getgW().getPlayer().getCurrentElement() == 2) {
                    getgW().getPlayer().addElementEnergy(2, 8);
                } else if (getgW().getPlayer().getCurrentElement() == 3) {
                    getgW().getPlayer().addElementEnergy(3, 8);
                }
//              getgW().getEntitiesToDelete().add(this);
                setUsed(true);
            }
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!isUsed()){
            if (getgW().getPlayer().getCurrentElement() == 0){
                g2d.drawImage(normalSprite, getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            } else if (getgW().getPlayer().getCurrentElement() == 1){
                g2d.drawImage(waterSprite, getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            } else if (getgW().getPlayer().getCurrentElement() == 2){
                g2d.drawImage(fireSprite, getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            } else if (getgW().getPlayer().getCurrentElement() == 3){
                g2d.drawImage(grassSprite, getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            }
            getgW().getUtil().drawDebugRect(g2d, getHitBox());
        }
    }

    public void loadSprites(String spritePath){
        if (normalSprite == null){
            normalSprite = getgW().getUtil().loadGraphic("sprites/power_up2.png");
            waterSprite = getgW().getUtil().loadGraphic("sprites/power_up3.png");
            fireSprite = getgW().getUtil().loadGraphic("sprites/power_up4.png");
            grassSprite = getgW().getUtil().loadGraphic("sprites/power_up5.png");
        }
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
