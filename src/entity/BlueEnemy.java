package entity;

import main.GameWindow;
import main.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BlueEnemy extends Enemy{
    BufferedImage[][] sprites;

    private int spriteWalkFrame = 0;
    private int spriteWalkId = 0;
    private int lastSpriteWalkId = 0;

    private boolean movingLeft = false;

    private Vector2 notActiveWorldPos;
    private Vector2 notActiveHitBoxWorldPos;

    public BlueEnemy(GameWindow gW, Vector2 worldPos, String spritePath){
        super(gW, worldPos);
        loadSprites(spritePath);
        setNotActiveWorldPos(new Vector2(worldPos));
        setScreenPosition(gW.util.worldPosToScreenPos(worldPos));
        setHitBox(new Rectangle(worldPos.getX() + 12, worldPos.getY() + 18,24 * gW.TILE_SCALE - 8 * gW.TILE_SCALE, 24 * gW.TILE_SCALE - 7 * gW.TILE_SCALE ));
        setNotActiveHitBoxWorldPos(new Vector2(getHitBox().x, getHitBox().y));
        setActive(false);
        setDead(false);
    }

    @Override
    public void process(){
        if (getgW().util.isRectOnScreen(getHitBox()) && !isActive() && !isDead()){
            setActive(true);
        } else if (!getgW().util.isRectOnScreen(getHitBox()) && isDead()){
            setDead(false);
            setActive(false);
        }
        if (!isDead() && isActive()){
            if (isMovingLeft() && (!getgW().tileManager.isTileBlocking(getHitBox().x - 2, getHitBox().y + getHitBox().height + getCollisionCheckTileOffset()) || getgW().tileManager.isTileBlocking(getHitBox().x - getCollisionCheckTileOffset(), getHitBox().y))){
                setMovingLeft(false);
            } else if (!isMovingLeft() && (!getgW().tileManager.isTileBlocking(getHitBox().x + getHitBox().width + 2, getHitBox().y + getHitBox().height + getCollisionCheckTileOffset()) || getgW().tileManager.isTileBlocking(getHitBox().x + getHitBox().width + getCollisionCheckTileOffset(), getHitBox().y))){
                setMovingLeft(true);
            }
            if (!isMovingLeft()){
                getVelocity().setX(4);
                setFacing(1);
            } else {
                getVelocity().setX(-4);
                setFacing(0);
            }
            if (!isOnGround()){
                getVelocity().setY(10);
            }
            applyVelocity();
            animateSprite();
            setScreenPosition(getgW().util.worldPosToScreenPos(getWorldPosition()));
            checkCollisionWithPlayer();
        } else {
            setMovingLeft(getgW().player.getWorldPosition().getX() <= getWorldPosition().getX());
            setWorldPosition(new Vector2(getNotActiveWorldPos()));
            getHitBox().x = getNotActiveHitBoxWorldPos().getX();
            getHitBox().y = getNotActiveHitBoxWorldPos().getY();
            setVelocity(new Vector2(0, 0));
            setScreenPosition(getgW().util.worldPosToScreenPos(getWorldPosition()));
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!isDead() && isActive()){
            g2d.drawImage(sprites[getFacing()][getSpriteWalkId()], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            getgW().util.drawDebugRect(g2d, getHitBox());
        }
    }

    public void animateSprite(){
        if (getSpriteWalkFrame() >= 8){
            setSpriteWalkFrame(0);
            if (getSpriteWalkId() == 0){
                if (getLastSpriteWalkId() == 2){
                    setSpriteWalkId(1);
                    setLastSpriteWalkId(getSpriteWalkId());
                } else {
                    setSpriteWalkId(2);
                    setLastSpriteWalkId(getSpriteWalkId());
                }
            } else {
                setSpriteWalkId(0);
            }
        } else {
            setSpriteWalkFrame(getSpriteWalkFrame() + 1);
        }
    }

    @Override
    public void onHit(Vector2 hitPos){
        if (!isRespawnable()){
            getgW().entitiesToDelete.add(this);
        }
        setDead(true);
        getgW().effects.add(getgW().enemyFactory.getEnemy(-1, new Vector2(getWorldPosition())));
    }

    @Override
    void loadSprites(String spritePath){
        try{
            BufferedImage full = ImageIO.read(getClass().getClassLoader().getResourceAsStream(spritePath));
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

    public int getSpriteWalkFrame() {
        return spriteWalkFrame;
    }

    public void setSpriteWalkFrame(int spriteWalkFrame) {
        this.spriteWalkFrame = spriteWalkFrame;
    }

    public int getSpriteWalkId() {
        return spriteWalkId;
    }

    public void setSpriteWalkId(int spriteWalkId) {
        this.spriteWalkId = spriteWalkId;
    }

    public int getLastSpriteWalkId() {
        return lastSpriteWalkId;
    }

    public void setLastSpriteWalkId(int lastSpriteWalkId) {
        this.lastSpriteWalkId = lastSpriteWalkId;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public Vector2 getNotActiveWorldPos() {
        return notActiveWorldPos;
    }

    public void setNotActiveWorldPos(Vector2 notActiveWorldPos) {
        this.notActiveWorldPos = notActiveWorldPos;
    }

    public Vector2 getNotActiveHitBoxWorldPos() {
        return notActiveHitBoxWorldPos;
    }

    public void setNotActiveHitBoxWorldPos(Vector2 notActiveHitBoxWorldPos) {
        this.notActiveHitBoxWorldPos = notActiveHitBoxWorldPos;
    }
}
