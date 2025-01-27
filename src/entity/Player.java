package entity;

import main.GameWindow;
import main.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{


    BufferedImage[][] sprites;

    public int collisionCheckTileOffset = 4;

//    public Vector2 screenPosition;

    public Rectangle hitBox;

    public int facing = 1;  // 0 - left 1 - right
    public int spriteId = 0;
    public int spriteAnimateFrame = 0;
    public int moveSpriteAnimId = 0;
    public int lastMoveSpriteAnimId = 0;

//    public Vector2 velocity = new Vector2(0, 0);
    public boolean jumped = false;

    public final int MAX_JUMP_FRAME = 4;
    public int jumpFrame = 0;
    public int moveDelayFrame = 0;

    public Vector2 playerScreenPosition;

    public int shootTimer = 0;
    public int shootMaxTimer = 1;
    public int shootAnim = 0;
    public int shootAnimChangeBackFrame = 0;

    public final int MAX_INVINCIBILITY_FRAME = 36;
    public int knockBackDir = 0;
    public int invincibilityFrame = 0;
    public boolean invincible = false;
    public int hp = 7;

    public Player(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        velocity = new Vector2(0,0);
        // center the player on the screen
        playerScreenPosition = gW.util.worldPosToScreenPos(worldPosition);
        this.hitBox = new Rectangle(worldPosition.x + 12, worldPosition.y + 6, 24 * 3 - 7 * 3, 24 * 3 - 3 * 3);
        gW.viewportPosition = new Vector2((gW.SCREEN_WIDTH / 2) - worldPosition.x, (gW.SCREEN_HEIGHT / 2)  - worldPosition.y);
        loadSprites();
    }

    @Override
    public void process(){
//        System.out.println(worldPosition);
        // Jump
        if (gW.keyInputHandler.confirm && !(invincible && invincibilityFrame < 8)){
            if (!jumped){
                velocity.y = -8;
                jumped = true;
                jumpFrame = 0;
            } else if (jumpFrame <= MAX_JUMP_FRAME){
                velocity.y += -3;
                jumpFrame++;
            }
        }
        // Movements
        if (gW.keyInputHandler.rightPressed && !(invincible && invincibilityFrame < 8)){
            if (!jumped){
                if (moveDelayFrame == 0){
                    velocity.x = 1;
                    moveDelayFrame++;
                } else if(moveDelayFrame < 8){
                    moveDelayFrame++;
                    velocity.x = 0;
                    spriteId = 1;
                } else {
                    velocity.x = 6;
                    spriteId = 2;
                    spriteAnimateFrame++;
                }
            } else {
                velocity.x = 4;
            }
            facing = 1;
        }
        else if (gW.keyInputHandler.leftPressed && !(invincible && invincibilityFrame < 8)){
            if (!jumped){
                if (moveDelayFrame == 0){
                    velocity.x = -1;
                    moveDelayFrame++;
                } else if(moveDelayFrame < 8){
                    moveDelayFrame++;
                    velocity.x = 0;
                    spriteId = 1;
                } else {
                    velocity.x = -6;
                    spriteId = 2;
                    spriteAnimateFrame++;
                }
            } else {
                velocity.x = -4;
            }
            facing = 0;
        }
        // knockBack
        else if (invincible && invincibilityFrame < 8){
            if (knockBackDir == 0){
                velocity.x = 8;
            } else {
                velocity.x = -8;
            }
            moveDelayFrame = 0;
            if (!jumped)
                spriteId = 0;
            moveSpriteAnimId = 0;
        } else {
            velocity.x = 0;
            moveDelayFrame = 0;
            if (!jumped)
                spriteId = 0;
            moveSpriteAnimId = 0;
        }
        // Ground Check and apply gravity
        if (!jumped){
            if (!isOnGround()){
                jumped = true;
            }
        } else {
            if (isOnGround() && velocity.y > 0){
                jumped = false;
                velocity.y = 0;
            }
            else
                velocity.y += 1;
        }
        // this should be in animateSprite but the game won't work if this is in animateSprite
        if (jumped || !isOnGround()){
            spriteId = 5;
        }

//        if (gW.keyInputHandler.cancel){
//            shoot();
//        }

        if (shootTimer < shootMaxTimer){
            shootTimer++;
        }

        invincibilityCheck();
        applyVelocity();
        animateSprite();
        gW.viewportPosition = new Vector2((gW.SCREEN_WIDTH / 2) - worldPosition.x, (gW.SCREEN_HEIGHT / 2)  - worldPosition.y);
//        gW.screenPosition = new Vector2((gW.SCREEN_WIDTH / 2) - worldPosition.x, gW.screenPosition.y);
        playerScreenPosition = gW.util.worldPosToScreenPos(worldPosition);
//        System.out.println(moveSpriteAnimId);
//        System.out.println(velocity.x + " " + velocity.y);
//        System.out.println(((hitBox.y + hitBox.height + velocity.y + 2) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE - (hitBox.y + hitBox.height));
    }

    public void applyVelocity(){
        // apply x
        // check if the player will overlap the any-tile with the current velocity, modify the velocity so that the player won't
        // overlap and get stuck inside a tile
        if (!gW.tileManager.checkRectNotIntersectAnyTile(hitBox.x + velocity.x, hitBox.y, hitBox.width, hitBox.height)) {
            int tileSnapPosX = 0;
            if (velocity.x > 0) {
                tileSnapPosX = ((hitBox.x + hitBox.width + velocity.x + 2) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE;
                velocity.x = ((hitBox.x + hitBox.width + 1) - tileSnapPosX) * -1;
            } else {
                tileSnapPosX = (((hitBox.x + velocity.x - 2) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE) + gW.RENDER_TILE_SIZE;
                velocity.x = ((hitBox.x - 1) - tileSnapPosX) * -1;
//                velocity.x = 0;
            }
        }
        // apply y
        // check if the player will overlap the any-tile with the current velocity, modify the velocity so that the player won't
        // overlap and get stuck inside a tile
        if (!gW.tileManager.checkRectNotIntersectAnyTile(hitBox.x, hitBox.y + velocity.y, hitBox.width, hitBox.height)) {
            int tileSnapPosY = 0;
//            System.out.println(tileSnapPosY);
            if (velocity.y > 0) {
                tileSnapPosY = ((hitBox.y + hitBox.height + velocity.y + 4) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE;
                velocity.y = ((hitBox.y + hitBox.height + 1) - tileSnapPosY) * -1;
//                System.out.println("!");
            } else {
                tileSnapPosY = (((hitBox.y + velocity.y - 4) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE) + gW.RENDER_TILE_SIZE;
                velocity.y = Math.abs(((hitBox.y - 1) - tileSnapPosY));
//                System.out.println("*");
//                velocity.y = 0;
            }
        }
        worldPosition.x += velocity.x;
        worldPosition.y += velocity.y;
        makeHitBoxFollowWorldPos();
//        hitBox.x += velocity.x;
//        hitBox.y += velocity.y;

    }

    public void animateSprite(){

        if (shootAnimChangeBackFrame <= 0){
            shootAnim = 0;
        } else {
            shootAnimChangeBackFrame--;
        }
        if (!jumped){
            if (spriteAnimateFrame >= 8){
                spriteAnimateFrame = 0;
                if (moveSpriteAnimId == 0){
                    if (lastMoveSpriteAnimId == 2){
                        moveSpriteAnimId = 1;
                        lastMoveSpriteAnimId = moveSpriteAnimId;
                    } else {
                        moveSpriteAnimId = 2;
                        lastMoveSpriteAnimId = moveSpriteAnimId;
                    }
                } else {
                    moveSpriteAnimId =0;
                }
            }
        } else {
            spriteAnimateFrame = 0;
            moveSpriteAnimId = 0;
            lastMoveSpriteAnimId = moveSpriteAnimId;
        }
    }

    public void makeHitBoxFollowWorldPos(){
        hitBox.x = worldPosition.x + 12;
        hitBox.y = worldPosition.y + 6;
    }

    @Override
    public void render(Graphics2D g2d){
        if (invincibilityFrame % 2 == 0) {
            g2d.drawImage(sprites[facing + shootAnim][spriteId + moveSpriteAnimId], playerScreenPosition.x, playerScreenPosition.y, 24 * gW.TILE_SCALE, 24 * gW.TILE_SCALE, null);
        }
        Vector2 rPos = gW.util.worldPosToScreenPos(new Vector2(hitBox.x, hitBox.y));
        g2d.drawRect(rPos.x, rPos.y, hitBox.width, hitBox.height);
//        rPos = gW.util.worldPosToScreenPos(new Vector2(worldPosition.x / gW.RENDER_TILE_SIZE, worldPosition.y / gW.RENDER_TILE_SIZE));
//        g2d.drawRect(rPos.x, rPos.y, gW.RENDER_TILE_SIZE, gW.RENDER_TILE_SIZE);
//        g2d.drawRect(worldPosition.x, worldPosition.y, 24, 24);

    }

    @Override
    public void onHit(Vector2 hitPos){
        if (hitPos.x < worldPosition.x) {
            gW.player.knockBackDir = 0;
        } else {
            gW.player.knockBackDir = 1;
        }
        if (!invincible){
            hp --;
            invincible = true;
        }
    }

    public void invincibilityCheck(){
        if (invincible){
            if (invincibilityFrame >= MAX_INVINCIBILITY_FRAME){
                invincible = false;
                invincibilityFrame = 0;
            } else {
                invincibilityFrame++;
            }

        }
    }

    public void shoot(){
//        if (shootTimer >= shootMaxTimer){
//            shootTimer = 0;
//            shootAnim = 2;
//            shootAnimChangeBackFrame = 16;
//            Projectile p = new PlayerProjectile(gW, new Vector2(worldPosition.x + 10, worldPosition.y - 3));
//            p.facing = facing;
//            p.velocity.x = 16;
//            if (facing == 0){
//                p.velocity.x *= -1;
//            }
//            gW.projectiles.add(p);
//        }
        if (PlayerProjectile.projectileCount < 3 && shootTimer >= shootMaxTimer){
            shootTimer = 0;
            shootAnim = 2;
            shootAnimChangeBackFrame = 16;
            Projectile p = new PlayerProjectile(gW, new Vector2(worldPosition.x + 10, worldPosition.y - 3));
            p.facing = facing;
            p.velocity.x = 16;
            if (facing == 0){
                p.velocity.x *= -1;
            }
            gW.projectiles.add(p);
        }
    }

    void loadSprites(){
        try{
            BufferedImage full = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/player.png"));
            sprites = new BufferedImage[full.getHeight() / 24][full.getWidth() / 24];
            for (int i = 0; i < full.getHeight() / 24; i++) {
                for (int j = 0; j < full.getWidth() / 24; j++) {
                    sprites[i][j] = full.getSubimage(j * 24, i * 24, 24, 24);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    int getPosXAtWallEdge(){
        int tileAt = hitBox.x / gW.RENDER_TILE_SIZE;
        if (velocity.x > 0){
            return ((tileAt * gW.RENDER_TILE_SIZE) + (gW.RENDER_TILE_SIZE - hitBox.height)) - 1;
        }
        else
            return tileAt * gW.RENDER_TILE_SIZE;
    }

    int getPosYAtWallEdge(){
        int tileAt = hitBox.y / gW.RENDER_TILE_SIZE;
        if (velocity.y > 0){
//            return ((tileAt * gW.RENDER_TILE_SIZE) + (gW.RENDER_TILE_SIZE - hitBox.height)) + gW.RENDER_TILE_SIZE - 1;
            return ((tileAt * gW.RENDER_TILE_SIZE) + (gW.RENDER_TILE_SIZE - hitBox.height)) - 1;
        }
        else
            return tileAt * gW.RENDER_TILE_SIZE;
    }

    boolean isOnGround(){
        return (gW.tileManager.isTileBlocking(hitBox.x, hitBox.y + hitBox.height + collisionCheckTileOffset) ||
                gW.tileManager.isTileBlocking(hitBox.x + hitBox.width, hitBox.y + hitBox.height + collisionCheckTileOffset)) &&
                (((hitBox.y + hitBox.height + velocity.y + 1) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE) - (hitBox.y + hitBox.height) < 2;
    }

//    boolean isCollidingWithWall(){
//        Vector2 left1 = new Vector2(hitBox.x  - collisionCheckTileOffset, hitBox.y);
//        Vector2 left2 = new Vector2(hitBox.x  - collisionCheckTileOffset, hitBox.y + hitBox.width);
//        Vector2 right1 = new Vector2(hitBox.x + hitBox.width + collisionCheckTileOffset, hitBox.y);
//        Vector2 right2 = new Vector2(hitBox.x  + hitBox.width + collisionCheckTileOffset, hitBox.y + hitBox.width);
//        Vector2 up = new Vector2(hitBox.x, hitBox.y + hitBox.height + collisionCheckTileOffset);
//
//        return (gW.tileManager.checkRectIntersectAnyTile(hitBox, left1) && gW.tileManager.checkRectIntersectAnyTile(hitBox, left2))
//                || (gW.tileManager.checkRectIntersectAnyTile(hitBox, right1) && gW.tileManager.checkRectIntersectAnyTile(hitBox, right2)) &&
//                gW.tileManager.checkRectIntersectAnyTile(hitBox, up);
//    }


}
