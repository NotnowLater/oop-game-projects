package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DarkShootDownEnemy extends Enemy{
    private static BufferedImage[][] sprites;

    private int spriteWalkFrame = 0;
    private int spriteWalkId = 0;

    private Vector2 notActiveWorldPos;
    private Vector2 notActiveHitBoxWorldPos;

    private boolean inAttackRange;

    private boolean movingLeft;

    private final int MAX_INVINCIBILITY_FRAME = 30;
    private int invincibilityFrame = 0;
    private int hp = 8;

    private int currentState = 0;

    public DarkShootDownEnemy(GameWindow gW, Vector2 worldPos, String spritePath, int element){
        super(gW, worldPos);
        loadSprites(spritePath);
        setNotActiveWorldPos(new Vector2(worldPos));
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPos));
        setHitBox(new Rectangle(worldPos.getX() + 3, worldPos.getY() + 3,22 * gW.TILE_SCALE, 9 * gW.TILE_SCALE));
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
            if (Math.abs(getWorldPosition().getX() - getgW().getPlayer().getWorldPosition().getX()) <= 4 * getgW().RENDER_TILE_SIZE){
                changeStateTo(1);
            } else {
                changeStateTo(0);
            }
            invincibilityCheck();
            animateSprite();
            setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
            checkCollisionWithPlayer();
        } else {
            // respawn
            setInvincibilityFrame(0);
            setInvincible(false);
            invincibilityCheck();
            setWorldPosition(new Vector2(getNotActiveWorldPos()));
            getHitBox().x = getNotActiveHitBoxWorldPos().getX();
            getHitBox().y = getNotActiveHitBoxWorldPos().getY();
            setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
            setHp(8);
        }

    }

    public void changeStateTo(int newState){
        if (newState != getCurrentState()){
            setCurrentState(newState);
            setSpriteWalkFrame(0);
            setSpriteWalkId(0);
        }
    }

    @Override
    public void animateSprite() {
            if (getSpriteWalkFrame() >= 24) {
                setSpriteWalkFrame(0);
                setSpriteWalkId(getSpriteWalkId() + 1);
                if (getSpriteWalkId() == 3){
                    if (getCurrentState() == 1){
                        shootPlayer();
                    }
                    setSpriteWalkId(0);
                }
            } else {
                setSpriteWalkFrame(getSpriteWalkFrame() + 1);
            }
    }

    public void shootPlayer(){
        Projectile p = new DarkShootDownProjectile(getgW(), new Vector2(getWorldPosition().getX() , getWorldPosition().getY() + 16 * getgW().TILE_SCALE));
        p.setFacing(getFacing());
        p.getVelocity().setY(16);
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
                g2d.drawImage(sprites[getCurrentState()][getSpriteWalkId()], getScreenPosition().getX(), getScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            }
            getgW().getUtil().drawDebugRect(g2d, getHitBox());
        }
    }

    public void loadSprites(String spritePath){
        if (sprites == null){
            sprites = getgW().getUtil().loadGraphic2D(spritePath, 24);
        }
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
