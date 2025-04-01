package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WhaleBossEnemy extends Enemy{

    BufferedImage[][] sprites;

    private int spriteActionId = 0;
    private int spriteWalkFrame = 0;
    private int spriteWalkId = 0;

    private boolean inAttackRange;

    private Vector2 notActiveWorldPos;
    private Vector2 notActiveHitBoxWorldPos;

    final int MAX_INVINCIBILITY_FRAME = 16;
    private int invincibilityFrame = 0;
    private int hp = 32;

    private int idleDone;
    private int currentState;

    public WhaleBossEnemy(GameWindow gW, Vector2 worldPos, String spritePath){
        super(gW, worldPos);
        loadSprites(spritePath);
        setNotActiveWorldPos(new Vector2(worldPos));
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPos));
        setHitBox(new Rectangle(worldPos.getX() + 156, worldPos.getY() + 240,176 * gW.TILE_SCALE - 83 * gW.TILE_SCALE, 176 * gW.TILE_SCALE - 130 * gW.TILE_SCALE ));
        setNotActiveHitBoxWorldPos(new Vector2(getHitBox().x, getHitBox().y));
        setActive(true);
        setDead(false);
    }

    @Override
    public void process() {
        setInAttackRange(getWorldPosition().distanceTo(getgW().getPlayer().getWorldPosition()) <= 528);
        // pick which action to do next after idle
        if (getCurrentState() == 0 && idleDone >= 2 && isInAttackRange()){
            setIdleDone(0);
            if (getgW().getRng().nextInt(100) <= 60){
                setSpriteActionId(1);
                changeStateTo(1);
            } else {
                setSpriteActionId(2);
                changeStateTo(4);
            }
        }
        // idle/prepare jump
        if (getCurrentState() < 2){
            if (!isOnGround()){
                getVelocity().setY(getVelocity().getY() + 2);
                if (getVelocity().getY() > 10){
                    getVelocity().setY(10);
                }
            }
        }
        // jump
        else if(getCurrentState() == 2){
            if (getVelocity().getY() <= 10){
                getVelocity().setY(getVelocity().getY() + 1);
            }
            if (getVelocity().getY() > 0){
                // jump recovery
                if (isOnGround()){
                    shootWave();
                    setSpriteWalkId(getSpriteWalkId() + 1);
                    changeStateTo(3);
                }
            }
        }
        invincibilityCheck();
        setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
        animateSprite();
        applyVelocity();
        checkCollisionWithPlayer();
    }


    @Override
    public void render(Graphics2D g2d) {
        if (getInvincibilityFrame() % 2 == 0) {
            g2d.drawImage(sprites[getSpriteActionId()][getSpriteWalkId()], getScreenPosition().getX(), getScreenPosition().getY(), 176 * getgW().TILE_SCALE, 176 * getgW().TILE_SCALE, null);
        }
        getgW().getUtil().drawDebugRect(g2d, getHitBox());
    }

    @Override
    public void animateSprite() {
        // idle
        if (getCurrentState() == 0){
            if (getSpriteWalkFrame() >= 16){
                setSpriteWalkFrame(0);
                setSpriteWalkId(getSpriteWalkId() + 1);
                if (getSpriteWalkId() == 1){
                    if (isInAttackRange()){
                        shootProjectile();
                    }
                }
                if (getSpriteWalkId() == 3){
                    setSpriteWalkId(0);
                    setIdleDone(getIdleDone() + 1);
//                    setSpriteActionId(1);
//                    changeStateTo(1);
                }
            } else {
                setSpriteWalkFrame(getSpriteWalkFrame() + 1);
            }
        }
        // prepare jump
        else if (getCurrentState() == 1){
            if (getSpriteWalkFrame() >= 16){
                setSpriteWalkFrame(0);
                setSpriteWalkId(getSpriteWalkId() + 1);
                if (getSpriteWalkId() == 5){
                    // start jump
                    setSpriteWalkId(getSpriteWalkId() + 1);
                    getVelocity().setY(-16);
                    changeStateTo(2);
                }
            } else {
                setSpriteWalkFrame(getSpriteWalkFrame() + 1);
            }
        }
        // recover jump
        else if (getCurrentState() == 3){
            if (getSpriteWalkFrame() >= 24){
                setSpriteWalkFrame(0);
                setSpriteWalkId(getSpriteWalkId() + 1);
                if (getSpriteWalkId() == 11){
                    setSpriteWalkId(0);
                    setSpriteActionId(0);
                    changeStateTo(0);
                }
            } else {
                setSpriteWalkFrame(getSpriteWalkFrame() + 1);
            }
        }
        // spawn enemy
        else if (getCurrentState() == 4){
            if (getSpriteWalkFrame() >= 8){
                setSpriteWalkFrame(0);
                setSpriteWalkId(getSpriteWalkId() + 1);
                if (getSpriteWalkId() == 11){
                    spawnEnemy();
                    setSpriteWalkId(0);
                    setSpriteActionId(0);
                    changeStateTo(0);
                }
            } else {
                spriteWalkFrame++;
            }
        }
    }

    @Override
    public void onHit(Vector2 hitPos){
        if (!isInvincible()){
            setHp(getHp() - 1);
            setInvincible(true);
            if (getHp() <= 0){
                getgW().getEntitiesToDelete().add(this);
                getgW().getViewportManager().setViewportFollowingAnchor(true);
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

    private void shootWave(){
        Projectile p = new DolphinProjectile(getgW(), new Vector2(getWorldPosition().getX() + 24 * getgW().TILE_SCALE, getWorldPosition().getY() + 135 * getgW().TILE_SCALE));
        p.setFacing(getFacing());
        p.getVelocity().setX(-16);
        getgW().getProjectiles().add(p);
    }

    private void shootProjectile(){
        Projectile p = new WhaleBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 32 * getgW().TILE_SCALE, getWorldPosition().getY() + 135 * getgW().TILE_SCALE));
        p.setFacing(getFacing());
        p.getVelocity().setX(-12);
        getgW().getProjectiles().add(p);
    }

    private void spawnEnemy(){
        Enemy e;
        if (getgW().getRng().nextInt(100) > 75){
            e = getgW().getEnemyFactory().getEnemy(1, new Vector2(getWorldPosition().getX() + 12 * getgW().TILE_SCALE, getWorldPosition().getY() + 127 * getgW().TILE_SCALE));
        } else {
            e = getgW().getEnemyFactory().getEnemy(2, new Vector2(getWorldPosition().getX() + 12 * getgW().TILE_SCALE, getWorldPosition().getY() + 127 * getgW().TILE_SCALE));
        }
        e.setRespawnable(false);
        getgW().addEnemyToSpawn(e);
    }

    public void loadSprites(String spritePath){
        sprites = getgW().getUtil().loadGraphic2D(spritePath, 176);
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


    public void changeStateTo(int newState){
        if (newState != currentState){
            currentState = newState;
        }
    }

    public int getCurrentState() {
        return currentState;
    }

    public int getSpriteWalkFrame() {
        return spriteWalkFrame;
    }

    public void setSpriteWalkFrame(int spriteWalkFrame) {
        this.spriteWalkFrame = spriteWalkFrame;
    }

    public int getSpriteActionId() {
        return spriteActionId;
    }

    public void setSpriteActionId(int spriteActionId) {
        this.spriteActionId = spriteActionId;
    }

    public int getSpriteWalkId() {
        return spriteWalkId;
    }

    public void setSpriteWalkId(int spriteWalkId) {
        this.spriteWalkId = spriteWalkId;
    }

    public int getIdleDone() {
        return idleDone;
    }

    public void setIdleDone(int idleDone) {
        this.idleDone = idleDone;
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

    public boolean isInAttackRange() {
        return inAttackRange;
    }

    public void setInAttackRange(boolean inAttackRange) {
        this.inAttackRange = inAttackRange;
    }
}
