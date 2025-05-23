package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DolphinEnemy extends Enemy{
    private static BufferedImage[][] sprites;

    private int spriteWalkFrame = 0;
    private int spriteWalkId = 0;
    private int lastSpriteWalkId = 0;

    private Vector2 notActiveWorldPos;
    private Vector2 notActiveHitBoxWorldPos;

    private boolean inAttackRange;

    private boolean movingLeft;

    private boolean shooting;
    private int shootFrame;
    private final int MAX_SHOOT_FRAME = 16;

    private final int MAX_INVINCIBILITY_FRAME = 32;
    private int invincibilityFrame = 0;
    private int hp = 3;

    public DolphinEnemy(GameWindow gW, Vector2 worldPos, String spritePath, int element){
        super(gW, worldPos);
        loadSprites(spritePath);
        setElement(element);
        setNotActiveWorldPos(new Vector2(worldPos));
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPos));
        setHitBox(new Rectangle(worldPos.getX() + 27, worldPos.getY() + 3,32 * gW.TILE_SCALE - 15 * gW.TILE_SCALE, 32 * gW.TILE_SCALE - 2 * gW.TILE_SCALE ));
        setNotActiveHitBoxWorldPos(new Vector2(getHitBox().x, getHitBox().y));
        setActive(false);
        setMovingLeft(true);
        setDead(false);
    }

    @Override
    public void process(){
        // spawn when on screen
        if (getgW().getUtil().isRectOnScreen(getHitBox()) && !isActive() && !isDead()){
            setActive(true);
        }
        // respawn/despawn when off-screen
        else if ((!getgW().getUtil().isRectOnScreen(getHitBox()) && isDead()) || !getgW().getUtil().isRectOnScreenPartial(getHitBox())){
            setDead(false);
            setActive(false);
        }
        if (!isDead() && isActive()){
            setInAttackRange(getWorldPosition().distanceTo(getgW().getPlayer().getWorldPosition()) <= 456);
            if (!isInAttackRange()){
                setShooting(false);
                if (isMovingLeft() && (!(getgW().getTileManager().isTileBlocking(getHitBox().x - 2, getHitBox().y + getHitBox().height + getCollisionCheckTileOffset() + 12))
                    || getgW().getTileManager().isTileBlocking(getHitBox().x - getCollisionCheckTileOffset() - 4, (int)getHitBox().getCenterY()))){
                setMovingLeft(false);
            } else if (!isMovingLeft() && (!getgW().getTileManager().isTileBlocking(getHitBox().x + getHitBox().width + 2, getHitBox().y + getHitBox().height + getCollisionCheckTileOffset() + 12)
                    || getgW().getTileManager().isTileBlocking(getHitBox().x + getHitBox().width + getCollisionCheckTileOffset() + 4, (int)getHitBox().getCenterY()))){
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

            } else {
                // InPlayerRange
                getVelocity().setX(0);
                if (getgW().getPlayer().getWorldPosition().getX() > getWorldPosition().getX()){
                    setFacing(1);
                    setMovingLeft(false);
                } else {
                    setFacing(0);
                    setMovingLeft(true);
                }
                if (getShootFrame() >= getMAX_SHOOT_FRAME() && !isShooting()){
                    setShootFrame(0);
                    setShooting(true);
                } else {
                    setShootFrame(getShootFrame() + 1);
                }

            }
            invincibilityCheck();
            applyVelocity();
            animateSprite();
            setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
            checkCollisionWithPlayer();
        } else {
            // respawn
            setInvincibilityFrame(0);
            setInvincible(false);
            invincibilityCheck();
            setMovingLeft(getgW().getPlayer().getWorldPosition().getX() <= getWorldPosition().getX());
            setWorldPosition(new Vector2(getNotActiveWorldPos()));
            getHitBox().x = getNotActiveHitBoxWorldPos().getX();
            getHitBox().y = getNotActiveHitBoxWorldPos().getY();
            setVelocity(new Vector2(0, 0));
            setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
            setHp(3);
        }
    }

    public void animateSprite(){
        if (!isInAttackRange()) {
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
        } else {
            if (!isShooting()){
                setSpriteWalkId(0);
            } else {
                if (getSpriteWalkFrame() >= 8){
                    setSpriteWalkFrame(0);
                    setSpriteWalkId(getSpriteWalkId() + 1);
                    if (getSpriteWalkId() == 3){
                        shootPlayer();
                    } else if (getSpriteWalkId() == 5){
                        setShooting(false);
                        setShootFrame(0);
                        setSpriteWalkId(0);
                    }
                } else {
                    setSpriteWalkFrame(getSpriteWalkFrame() + 1);
                }
            }
        }
    }

    public void shootPlayer(){
        Vector2 spawnPoint;
        if (getFacing() == 0){
            spawnPoint = new Vector2(getWorldPosition().getX() - 24, getWorldPosition().getY() + 22);
        } else {
            spawnPoint = new Vector2(getWorldPosition().getX() + 24, getWorldPosition().getY() + 22);
        }
        Projectile p = new DolphinProjectile(getgW(), new Vector2(spawnPoint));
        p.setFacing(getFacing());
        p.getVelocity().setX(16);
        if (getFacing() == 0){
            p.getVelocity().setX(p.getVelocity().getX() * -1);
        }
        getgW().getProjectiles().add(p);
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
                if (!isShooting()){
                    g2d.drawImage(sprites[getFacing()][getSpriteWalkId()], getScreenPosition().getX(), getScreenPosition().getY(), 32 * getgW().TILE_SCALE, 32 * getgW().TILE_SCALE, null);
                } else {
                    g2d.drawImage(sprites[getFacing() + 2][getSpriteWalkId()], getScreenPosition().getX(), getScreenPosition().getY(), 32 * getgW().TILE_SCALE, 32 * getgW().TILE_SCALE, null);
                }
            }
            getgW().getUtil().drawDebugRect(g2d, getHitBox());
        }
    }

    public void loadSprites(String spritePath){
        if (sprites == null) {
            sprites = getgW().getUtil().loadGraphic2D(spritePath, 32);
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

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public int getShootFrame() {
        return shootFrame;
    }

    public void setShootFrame(int shootFrame) {
        this.shootFrame = shootFrame;
    }

    public int getMAX_SHOOT_FRAME() {
        return MAX_SHOOT_FRAME;
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
