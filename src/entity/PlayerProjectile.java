package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public class PlayerProjectile extends Projectile{

    public static int projectileCount = 0;

    public PlayerProjectile(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        loadSprites("sprites/player_projectile.png");
        setHitBox(new Rectangle(worldPosition.getX() + 15, worldPosition.getY() + 30, 16 * gW.TILE_SCALE, 6 * gW.TILE_SCALE));
        setScreenPosition(gW.util.worldPosToScreenPos(worldPosition));
        projectileCount++;
    }

    @Override
    public void process(){
        getWorldPosition().setX(getWorldPosition().getX() + getVelocity().getX());
        getHitBox().x += getVelocity().getX();
        setScreenPosition(getgW().util.worldPosToScreenPos(getWorldPosition()));
        checkCollisionWithEntity();
        if (!getgW().util.isRectOnScreenPartial(getHitBox())){
            getgW().entitiesToDelete.add(this);
            projectileCount--;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(sprites[getFacing()][0], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
        getgW().util.drawDebugRect(g2d, getHitBox());
    }

    public void checkCollisionWithEntity(){
        for (Enemy e : getgW().enemies){
            if (getHitBox().intersects(e.getHitBox())){
                if (!e.isDead() && e.isActive()){
                    e.onHit(getWorldPosition());
                    getgW().entitiesToDelete.add(this);
                    projectileCount--;
                }
            }
        }

    }

    void loadSprites(String spritePath){
        sprites = getgW().util.loadGraphic2D(spritePath, 24);
    }

}
