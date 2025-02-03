package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public class WhaleBossProjectile extends Projectile{

    public WhaleBossProjectile(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        loadSprites("sprites/boss_projectile.png");
        setHitBox(new Rectangle(worldPosition.getX() + 21, worldPosition.getY() + 24, 24 * gW.TILE_SCALE - 15 * gW.TILE_SCALE, 24 * gW.TILE_SCALE - 16 * gW.TILE_SCALE));
        setScreenPosition(gW.util.worldPosToScreenPos(worldPosition));
    }

    @Override
    public void process(){
        getWorldPosition().setX(getWorldPosition().getX() + getVelocity().getX());
        getHitBox().x += getVelocity().getX();
        setScreenPosition(getgW().util.worldPosToScreenPos(getWorldPosition()));
        checkCollisionWithEntity();
        if (!getgW().util.isRectOnScreenPartial(getHitBox())){
            getgW().entitiesToDelete.add(this);
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(sprites[getFacing()][0], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
        getgW().util.drawDebugRect(g2d, getHitBox());
    }

    public void checkCollisionWithEntity() {
        if (getHitBox().intersects(getgW().player.getHitBox())){
            if (!getgW().player.isInvincible()){
                getgW().player.onHit(getWorldPosition());
                getgW().entitiesToDelete.add(this);
            }
        }
    }

    void loadSprites(String spritePath){
        sprites = getgW().util.loadGraphic2D(spritePath, 24);
    }

}
