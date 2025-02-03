package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PenguinEnemy extends Enemy{
    BufferedImage[][] sprites;

    private int spriteWalkFrame = 0;
    private int spriteWalkId = 0;
    private int lastSpriteWalkId = 0;

    private Vector2 notActiveWorldPos;
    private Vector2 notActiveHitBoxWorldPos;

    private boolean inAttackRange;

    private boolean movingLeft;

    private boolean canJump;
    private boolean jumping;
    private int jumpToPlayerFacing = 0;
    private int jumpFrame;
    final int MAX_SHOOT_FRAME = 30;

    final int MAX_INVINCIBILITY_FRAME = 16;
    private int invincibilityFrame = 0;
    private int hp = 3;

    private int currentState = 0;

    public PenguinEnemy(GameWindow gW, Vector2 worldPos, String spritePath){
        super(gW, worldPos);
        loadSprites(spritePath);
        setNotActiveWorldPos(new Vector2(worldPos));
        setScreenPosition(gW.util.worldPosToScreenPos(worldPos));
        setHitBox(new Rectangle(worldPos.getX() + 27, worldPos.getY() + 3,24 * gW.TILE_SCALE - 15 * gW.TILE_SCALE, 24 * gW.TILE_SCALE - 2 * gW.TILE_SCALE ));
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
            setInAttackRange(getWorldPosition().distanceTo(getgW().player.getWorldPosition()) <= 456);
            // walking state
            if (getCurrentState() == 0){
                setCanJump(false);
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
                    getVelocity().setY(getVelocity().getY() + 2);
                    if (getVelocity().getY() > 10){
                        getVelocity().setY(10);
                    }
                }
                if (isInAttackRange()){
                    changeStateTo(1);
                    if (getgW().player.getWorldPosition().getX() <= getWorldPosition().getX()){
                        setFacing(0);
                        setMovingLeft(true);
                    } else {
                        setFacing(1);
                        setMovingLeft(false);
                    }
                    setSpriteWalkId(0);
                    setSpriteWalkFrame(0);
                    getVelocity().setX(0);
                }
            }
            // jump state
            else if(getCurrentState() == 2){
                if (getVelocity().getY() <= 10){
                    getVelocity().setY(getVelocity().getY() + 1);
                    if (isMovingLeft()){
                        getVelocity().setX(-14);
                    } else {
                        getVelocity().setX(14);
                    }
                }
                if (getVelocity().getY() > 0){
                    if (isMovingLeft()){
                        getVelocity().setX(-8);
                    } else {
                        getVelocity().setX(8);
                    }
                    if (isOnGround()){
                        changeStateTo(0);
                        setSpriteWalkId(0);
                        getVelocity().setX(0);
                    }
                }
            }
            invincibilityCheck();
            applyVelocity();
            animateSprite();
            setScreenPosition(getgW().util.worldPosToScreenPos(getWorldPosition()));
            checkCollisionWithPlayer();
        } else {
            // respawn
            changeStateTo(0);
            setInvincible(false);
            invincibilityCheck();
            setMovingLeft(getgW().player.getWorldPosition().getX() <= getWorldPosition().getX());
            setWorldPosition(new Vector2(getNotActiveWorldPos()));
            getHitBox().x = getNotActiveHitBoxWorldPos().getX();
            getHitBox().y = getNotActiveHitBoxWorldPos().getY();
            setVelocity(new Vector2(0, 0));
            setScreenPosition(getgW().util.worldPosToScreenPos(getWorldPosition()));
            setHp(3);
        }
    }

    public void animateSprite(){
        if (getCurrentState() == 0) {
            if (getSpriteWalkFrame() >= 8) {
                setSpriteWalkFrame(0);
                if (getSpriteWalkId() == 0) {
                    if (getLastSpriteWalkId() == 2) {
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
        } else if (getCurrentState() == 1){
            if (getSpriteWalkFrame() >= 8){
                setSpriteWalkFrame(0);
                setSpriteWalkId(getSpriteWalkId() + 1);
                if (getSpriteWalkId() == 3){
                    // start jump
                    getVelocity().setY(-12);
                    setSpriteWalkId(getSpriteWalkId() + 1);
                    changeStateTo(2);
                }
            } else {
                setSpriteWalkFrame(getSpriteWalkFrame() + 1);
            }
        }
    }

    public void changeStateTo(int newState){
        if (newState != getCurrentState()){
            setCurrentState(newState);
        }
    }

    public void setUpJumpToPlayer(boolean falling){
        setJumping(!falling);
        getVelocity().setX(0);
        getVelocity().setY(0);
        if (!falling){
            if (getgW().player.getWorldPosition().getX() <= getWorldPosition().getX()){
                setJumpToPlayerFacing(-1);
                setMovingLeft(true);
            } else {
                setJumpToPlayerFacing(1);
                setMovingLeft(false);
            }
        }
    }

    @Override
    public void onHit(Vector2 hitPos){
        if (!isInvincible()){
            setHp(getHp() - 1);
            setInvincible(true);
            if (getHp() <= 0){
                if (!isRespawnable()){
                    getgW().entitiesToDelete.add(this);
                }
                setDead(true);
                getgW().effects.add(getgW().enemyFactory.getEnemy(-1, new Vector2(getWorldPosition())));
            }
        }
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

    @Override
    public void render(Graphics2D g2d){
        if (!isDead() && isActive()){
            if (getInvincibilityFrame() % 2 == 0) {
                if (getCurrentState() == 0){
                    g2d.drawImage(sprites[getFacing()][getSpriteWalkId()], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
                } else {
                    g2d.drawImage(sprites[getFacing() + 2][Math.clamp(getSpriteWalkId(), 0, 4)], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
                }
            }
            getgW().util.drawDebugRect(g2d, getHitBox());
        }
    }

    public void loadSprites(String spritePath){
        sprites = getgW().util.loadGraphic2D(spritePath, 24);
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

    public boolean isInAttackRange() {
        return inAttackRange;
    }

    public void setInAttackRange(boolean inAttackRange) {
        this.inAttackRange = inAttackRange;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }


    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }


    public void setJumpToPlayerFacing(int jumpToPlayerFacing) {
        this.jumpToPlayerFacing = jumpToPlayerFacing;
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

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }
}
