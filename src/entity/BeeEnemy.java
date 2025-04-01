package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BeeEnemy extends Enemy{
    private BufferedImage[][] sprites;

    private int spriteWalkFrame = 0;
    private int spriteWalkId = 0;

    private Vector2 notActiveWorldPos;
    private Vector2 notActiveHitBoxWorldPos;

    private final int MAX_INVINCIBILITY_FRAME = 30;
    private int invincibilityFrame = 0;
    private int hp = 2;

    public BeeEnemy(GameWindow gW, Vector2 worldPos, String spritePath, int element){
        super(gW, worldPos);
        setElement(element);
        loadSprites(spritePath);
        setNotActiveWorldPos(new Vector2(worldPos));
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPos));
        setHitBox(new Rectangle(worldPos.getX() + 18, worldPos.getY() + 9,12 * gW.TILE_SCALE, 21 * gW.TILE_SCALE));
        setNotActiveHitBoxWorldPos(new Vector2(getHitBox().x, getHitBox().y));
        setActive(false);
        setDead(false);
    }

    @Override
    public void process() {
        if (getgW().getUtil().isRectOnScreen(getHitBox()) && !isActive() && !isDead()){
            setActive(true);
        } else if ((!getgW().getUtil().isRectOnScreen(getHitBox()) && isDead()) || !getgW().getUtil().isRectOnScreenPartial(getHitBox())){
            setDead(false);
            setActive(false);
        }
        if (!isDead() && isActive()){
            if (getgW().getPlayer().getWorldPosition().getX() < getWorldPosition().getX()){
                if (getVelocity().getX() > -6){
                    getVelocity().setX(getVelocity().getX() - 2);
                }
                setFacing(0);
            } else {
                if (getVelocity().getX() < 6){
                    getVelocity().setX(getVelocity().getX() + 2);
                }
                setFacing(1);
            }

            if (getgW().getPlayer().getWorldPosition().getY() < getWorldPosition().getY()){
                if (getVelocity().getY() > -6){
                    getVelocity().setY(getVelocity().getY() - 2);
                }
            } else {
                if (getVelocity().getY() < 6){
                    getVelocity().setY(getVelocity().getY() + 2);
                }
            }
            invincibilityCheck();
            applyVelocity();
            animateSprite();
            setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
            checkCollisionWithPlayer();
        } else {
            // respawn
            setInvincible(false);
            setInvincibilityFrame(0);
            invincibilityCheck();
            setWorldPosition(new Vector2(getNotActiveWorldPos()));
            getHitBox().x = getNotActiveHitBoxWorldPos().getX();
            getHitBox().y = getNotActiveHitBoxWorldPos().getY();
            setVelocity(new Vector2(0, 0));
            setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
            setHp(2);
        }

    }

    @Override
    public void render(Graphics2D g2d){
        if (!isDead() && isActive()){
            if (getInvincibilityFrame() % 2 == 0) {
                g2d.drawImage(sprites[getFacing()][getSpriteWalkId()], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            }
            getgW().getUtil().drawDebugRect(g2d, getHitBox());
        }
    }

    @Override
    public void onHit(Vector2 hitPos, int element){
        if (!isInvincible()){
            int adv = checkElementAdvantage(element);
            if (adv == 1){
                setHp(getHp() - 2);
            } else if (adv == 0){
                setHp(getHp() - 1);
            }
            setInvincible(true);
            if (getHp() <= 0){
                if (!isRespawnable()){
                    getgW().getEntitiesToDelete().add(this);
                }
                setDead(true);
                getgW().getEffects().add(getgW().getEnemyFactory().getEnemy(-1, new Vector2(getWorldPosition())));
            }
        }
    }

    @Override
    public void animateSprite() {
        if (getSpriteWalkFrame() >= 4) {
            setSpriteWalkFrame(0);
            setSpriteWalkId(getSpriteWalkId() + 1);
            if (getSpriteWalkId() == 2){
                setSpriteWalkId(0);
            }
        } else {
            setSpriteWalkFrame(getSpriteWalkFrame() + 1);
        }
    }

    @Override
    public void applyVelocity(){
        getWorldPosition().setX(getWorldPosition().getX() + getVelocity().getX());
        getWorldPosition().setY(getWorldPosition().getY() + getVelocity().getY());
        getHitBox().x += getVelocity().getX();
        getHitBox().y += getVelocity().getY();
    }

    public void invincibilityCheck(){
        if (isInvincible()){
            if (getInvincibilityFrame() >= getMAX_INVINCIBILITY_FRAME()){
                setInvincible(false);
                setInvincibilityFrame(0);
            } else {
                setInvincibilityFrame(getInvincibilityFrame() + 1);
            }

        }
    }

    public void loadSprites(String spritePath){
        sprites = getgW().getUtil().loadGraphic2D(spritePath, 24);
    }

    public BufferedImage[][] getSprites() {
        return sprites;
    }

    public void setSprites(BufferedImage[][] sprites) {
        this.sprites = sprites;
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

    public int getMAX_INVINCIBILITY_FRAME() {
        return MAX_INVINCIBILITY_FRAME;
    }

    public int getInvincibilityFrame() {
        return invincibilityFrame;
    }

    public void setInvincibilityFrame(int invincibilityFrame) {
        this.invincibilityFrame = invincibilityFrame;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
