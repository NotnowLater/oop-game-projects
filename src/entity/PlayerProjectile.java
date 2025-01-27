package entity;

import main.GameWindow;
import main.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerProjectile extends Projectile{

    public static int projectileCount = 0;

    public PlayerProjectile(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        loadSprites("sprites/player_projectile.png");
        hitBox = new Rectangle(worldPosition.x + 15, worldPosition.y + 30, 16 * gW.TILE_SCALE, 6 * gW.TILE_SCALE);
        screenPosition = gW.util.worldPosToScreenPos(worldPosition);
        projectileCount++;
    }

    @Override
    public void process(){
        worldPosition.x += velocity.x;
        hitBox.x += velocity.x;
        screenPosition = gW.util.worldPosToScreenPos(worldPosition);
        checkCollisionWithEntity();
        if (!gW.util.isRectOnScreenPartial(hitBox)){
            gW.entitiesToDelete.add(this);
            projectileCount--;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(sprites[facing][0], screenPosition.x, screenPosition.y, 24 * gW.TILE_SCALE, 24 * gW.TILE_SCALE, null);
        gW.util.drawDebugRect(g2d, hitBox);
    }

    public void checkCollisionWithEntity(){
        for (Enemy e : gW.enemies){
            if (hitBox.intersects(e.hitBox)){
                if (!e.isDead && e.isActive){
                    e.onHit(worldPosition);
                    gW.entitiesToDelete.add(this);
                    projectileCount--;
                }
            }
        }

    }

    void loadSprites(String spritePath){
        sprites = gW.util.loadGraphic2D(spritePath, 24);
    }

}
