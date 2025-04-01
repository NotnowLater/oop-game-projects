package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public class WaterBossProjectile extends Projectile{

    public static int spawnCount;

    private boolean goingUp = false;

    public WaterBossProjectile(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        loadSprites("sprites/boss_projectile.png");
        setHitBox((new Rectangle(worldPosition.getX() + 27, worldPosition.getY() + 30, 13 * getgW().TILE_SCALE, 12 * getgW().TILE_SCALE)));
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPosition));
        spawnCount++;
    }

    @Override
    public void process() {
//            Vector2 playerPos = getgW().getPlayer().getWorldPosition();
//            double deltaX = playerPos.getX() - getWorldPosition().getX();
//            double deltaY = playerPos.getY() - getWorldPosition().getY();
//            double angle = Math.atan2(deltaY, deltaX);
//            double speed = 3;
//            getVelocity().setX((int) (speed * Math.cos(angle)));
//            getVelocity().setY((int) (speed * Math.sin(angle)));
        if (goingUp){
            if (getVelocity().getY() > -10){
                getVelocity().setY(getVelocity().getY() - 1);
            } else if (getVelocity().getY() < -6) {
                goingUp = false;
            }
        } else {
            if (getVelocity().getY() < 10){
                getVelocity().setY(getVelocity().getY() + 1);
            } else if (getVelocity().getY() > 6) {
                goingUp = true;
            }
        }
        getWorldPosition().setX(getWorldPosition().getX() + getVelocity().getX());
        getWorldPosition().setY(getWorldPosition().getY() + getVelocity().getY());
        getHitBox().x += getVelocity().getX();
        getHitBox().y += getVelocity().getY();

        setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
        checkCollisionWithEntity();

        if (!getgW().getUtil().isRectOnScreenPartial(getHitBox())){
            getgW().getEntitiesToDelete().add(this);
            spawnCount--;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(sprites[0][0], getScreenPosition().getX(), getScreenPosition().getY(), 32 * getgW().TILE_SCALE, 32 * getgW().TILE_SCALE, null);
        getgW().getUtil().drawDebugRect(g2d, getHitBox());
    }

    public void checkCollisionWithEntity() {
        if (getHitBox().intersects(getgW().getPlayer().getHitBox())){
            if (!getgW().getPlayer().isInvincible()){
                getgW().getPlayer().onHit(getWorldPosition());
                getgW().getEntitiesToDelete().add(this);
                spawnCount--;
            }
        }
    }

    void loadSprites(String spritePath){
        sprites = getgW().getUtil().loadGraphic2D(spritePath, 32);
    }
}