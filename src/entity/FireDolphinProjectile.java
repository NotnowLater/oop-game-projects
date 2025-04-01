package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public class FireDolphinProjectile extends Projectile{

    public FireDolphinProjectile(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        loadSprites("sprites/enemy8_projectile.png");
        setHitBox(new Rectangle(worldPosition.getX() + 18, worldPosition.getY() + 12, 24 * gW.TILE_SCALE - 12 * gW.TILE_SCALE, 24 * gW.TILE_SCALE - 5 * gW.TILE_SCALE));
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPosition));
    }

    @Override
    public void process(){
        getWorldPosition().setX(getWorldPosition().getX() + getVelocity().getX());
        getHitBox().x += getVelocity().getX();
        setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
        checkCollisionWithEntity();
        if (!getgW().getUtil().isRectOnScreenPartial(getHitBox())){
            getgW().getEntitiesToDelete().add(this);
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(sprites[getFacing()][0], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
        getgW().getUtil().drawDebugRect(g2d, getHitBox());
    }

    public void checkCollisionWithEntity() {
        if (getHitBox().intersects(getgW().getPlayer().getHitBox())){
            if (!getgW().getPlayer().isInvincible()){
                getgW().getPlayer().onHit(new Vector2((int)getHitBox().getCenterX(), (int)getHitBox().getCenterY()));
                getgW().getEntitiesToDelete().add(this);
            }
        }
    }

    void loadSprites(String spritePath){
        sprites = getgW().getUtil().loadGraphic2D(spritePath, 24);
    }
}
