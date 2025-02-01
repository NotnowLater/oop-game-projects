package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public class DolphinProjectile extends Projectile{

    public DolphinProjectile(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        loadSprites("sprites/enemy2_projectile.png");
        setHitBox(new Rectangle(worldPosition.getX() + 18, worldPosition.getY() + 12, 24 * gW.TILE_SCALE - 12 * gW.TILE_SCALE, 24 * gW.TILE_SCALE - 5 * gW.TILE_SCALE));
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
