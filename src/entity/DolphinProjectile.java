package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public class DolphinProjectile extends Projectile{

    public DolphinProjectile(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        loadSprites("sprites/enemy2_projectile.png");
        hitBox = new Rectangle(worldPosition.x + 18, worldPosition.y + 12, 24 * gW.TILE_SCALE - 12 * gW.TILE_SCALE, 24 * gW.TILE_SCALE - 5 * gW.TILE_SCALE);
        screenPosition = gW.util.worldPosToScreenPos(worldPosition);
    }

    @Override
    public void process(){
        worldPosition.x += velocity.x;
        hitBox.x += velocity.x;
        screenPosition = gW.util.worldPosToScreenPos(worldPosition);
        checkCollisionWithEntity();
        if (!gW.util.isRectOnScreenPartial(hitBox)){
            gW.entitiesToDelete.add(this);
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(sprites[facing][0], screenPosition.x, screenPosition.y, 24 * gW.TILE_SCALE, 24 * gW.TILE_SCALE, null);
        gW.util.drawDebugRect(g2d, hitBox);
    }

    public void checkCollisionWithEntity() {
        if (hitBox.intersects(gW.player.hitBox)){
            if (!gW.player.invincible){
                gW.player.onHit(worldPosition);
                gW.entitiesToDelete.add(this);
            }
        }
    }

    void loadSprites(String spritePath){
        sprites = gW.util.loadGraphic2D(spritePath, 24);
    }
}
