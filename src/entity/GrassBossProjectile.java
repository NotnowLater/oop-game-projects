package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public class GrassBossProjectile extends Projectile{

    private int facing;

    public GrassBossProjectile(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        loadSprites("sprites/boss_grass_projectile.png");
        setHitBox((new Rectangle(worldPosition.getX() + 21, worldPosition.getY() + 21, 9 * getgW().TILE_SCALE, 10 * getgW().TILE_SCALE)));
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPosition));
    }

    @Override
    public void process(){
        getWorldPosition().setX(getWorldPosition().getX() + getVelocity().getX());
        getWorldPosition().setY(getWorldPosition().getY() + getVelocity().getY());
        getHitBox().x += getVelocity().getX();
        getHitBox().y += getVelocity().getY();
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
                getgW().getPlayer().onHit(getWorldPosition());
                getgW().getEntitiesToDelete().add(this);
            }
        }
    }

    void loadSprites(String spritePath){
        sprites = getgW().getUtil().loadGraphic2D(spritePath, 24);
    }

    @Override
    public int getFacing() {
        return facing;
    }

    @Override
    public void setFacing(int facing) {
        this.facing = facing;
    }
}