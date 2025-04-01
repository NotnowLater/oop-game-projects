package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerProjectile extends Projectile{

    public static int projectileCount = 0;
    private int projectileElement;
    private boolean hittedSomthing = false;

    private static BufferedImage normalSprites[][];
    private static BufferedImage waterSprites[][];
    private static BufferedImage fireSprites[][];
    private static BufferedImage grassSprites[][];

    public PlayerProjectile(GameWindow gW, Vector2 worldPosition, int projectileElement){
        super(gW, worldPosition);
        loadSprites();
        setProjectileElement(projectileElement);
        if (projectileElement == 1){
            setHitBox(new Rectangle(worldPosition.getX() + 27, worldPosition.getY() + 33, 13 * gW.TILE_SCALE, 11 * gW.TILE_SCALE));
        } else {
            setHitBox(new Rectangle(worldPosition.getX() + 21, worldPosition.getY() + 24, 8 * gW.TILE_SCALE, 8 * gW.TILE_SCALE));
        }
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPosition));
        projectileCount++;
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
            projectileCount--;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        if (getProjectileElement() == 0){
            g2d.drawImage(normalSprites[getFacing()][0], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
        } else if (getProjectileElement() == 1){
            g2d.drawImage(waterSprites[getFacing()][0], getScreenPosition().getX(), getScreenPosition().getY(), 32 * getgW().TILE_SCALE, 32 * getgW().TILE_SCALE, null);
        } else if (getProjectileElement() == 2){
            g2d.drawImage(fireSprites[getFacing()][0], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
        } else if (getProjectileElement() == 3){
            g2d.drawImage(grassSprites[getFacing()][0], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
        }

        getgW().getUtil().drawDebugRect(g2d, getHitBox());
    }

    public void checkCollisionWithEntity(){
        for (Enemy e : getgW().getEnemies()){
            if (getHitBox().intersects(e.getHitBox())){
                if (!e.isDead() && e.isActive() && !e.isInvincible() && !hittedSomthing){
                    e.onHit(getWorldPosition(), getProjectileElement());
                    getgW().getEntitiesToDelete().add(this);
                    hittedSomthing = true;
                    projectileCount--;
                }
            }
        }

    }

    void loadSprites(){
        if (normalSprites == null){
            normalSprites = getgW().getUtil().loadGraphic2D("sprites/player_projectile0.png", 24);
            waterSprites = getgW().getUtil().loadGraphic2D("sprites/player_projectile1.png", 32);
            fireSprites = getgW().getUtil().loadGraphic2D("sprites/player_projectile2.png", 24);
            grassSprites = getgW().getUtil().loadGraphic2D("sprites/player_projectile3.png", 24);
        }

    }

    public int getProjectileElement() {
        return projectileElement;
    }

    public void setProjectileElement(int projectileElement) {
        this.projectileElement = projectileElement;
    }
}
