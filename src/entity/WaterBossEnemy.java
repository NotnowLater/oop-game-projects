package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WaterBossEnemy extends Enemy{
    private static BufferedImage[][] sprites;

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
    private int maxJumpHeight;
    private boolean isAtMaxJumpHeight;

    final int MAX_INVINCIBILITY_FRAME = 30;
    private int invincibilityFrame = 0;
    private int hp = 32;

    private int currentState = 0;

    private int shootDir = 1;
    private int shootCooldown = 0;
    private final int MAX_SHOOT_COOLDOWN = 16;

    private int jumpShootCooldown;
    private final int MAX_JUMP_SHOOT_COOLDOWN = 60;

    private int bossDeadCooldown;
    private boolean bossDeadCooldowned = false;

    public WaterBossEnemy(GameWindow gW, Vector2 worldPos, String spritePath, int element){
        super(gW, worldPos);
        setElement(element);
        loadSprites(spritePath);
        setNotActiveWorldPos(new Vector2(worldPos));
        setScreenPosition(gW.getUtil().worldPosToScreenPos(worldPos));
        setHitBox(new Rectangle(worldPos.getX() + 27, worldPos.getY() + 21, 16 * 3, 24 * 3));
        setNotActiveHitBoxWorldPos(new Vector2(getHitBox().x, getHitBox().y));
        setActive(false);
        setMovingLeft(true);
        setDead(false);
        setMaxJumpHeight(0);
        setIsAtMaxJumpHeight(false);
    }

    @Override
    public void process(){
        if (getgW().getUtil().isRectOnScreen(getHitBox()) && !isActive() && !isDead()){
            setActive(true);
        } else if ((!getgW().getUtil().isRectOnScreen(getHitBox()) && isDead()) || !getgW().getUtil().isRectOnScreenPartial(getHitBox())){
            setDead(false);
            setActive(false);
        }
        if (!isDead() && isActive()){
            setInAttackRange(getWorldPosition().distanceTo(getgW().getPlayer().getWorldPosition()) <= 456);
            // walking state
            if (getCurrentState() == 0){
                setCanJump(false);
                if (isMovingLeft() && (!getgW().getTileManager().isTileBlocking(getHitBox().x - 2, getHitBox().y + getHitBox().height + getCollisionCheckTileOffset()) || getgW().getTileManager().isTileBlocking(getHitBox().x - getCollisionCheckTileOffset(), getHitBox().y))){
                    setMovingLeft(false);
                    changeStateTo(1);
                } else if (!isMovingLeft() && (!getgW().getTileManager().isTileBlocking(getHitBox().x + getHitBox().width + 2, getHitBox().y + getHitBox().height + getCollisionCheckTileOffset()) || getgW().getTileManager().isTileBlocking(getHitBox().x + getHitBox().width + getCollisionCheckTileOffset(), getHitBox().y))){
                    setMovingLeft(true);
                    changeStateTo(1);
                }
                if (!isMovingLeft()){
                    getVelocity().setX(6);
                    setFacing(1);
                } else {
                    getVelocity().setX(-6);
                    setFacing(0);
                }
                if (!isOnGround()){
                    getVelocity().setY(getVelocity().getY() + 2);
                    if (getVelocity().getY() > 10){
                        getVelocity().setY(10);
                    }
                }

                setJumpShootCooldown(getJumpShootCooldown() + 1);

            }
            // jump state
            else if(getCurrentState() == 2){
                if (getVelocity().getY() <= 10){
                    getVelocity().setY(getVelocity().getY() + 1);
                    if (isMovingLeft()){
                        getVelocity().setX(-1);
                    } else {
                        getVelocity().setX(1);
                    }
                }
                if (getVelocity().getY() > 0){
                    getVelocity().setY(getVelocity().getY() + 1);
                    if (isMovingLeft()){
                        getVelocity().setX(-1);
                    } else {
                        getVelocity().setX(1);
                    }
                    if (isOnGround()){
                        setJumpShootCooldown(0);
                        changeStateTo(0);
                        setSpriteWalkId(0);
                        getVelocity().setX(0);
                        setShootCooldown(0);
                    }
                }
                if (getVelocity().getY() == 0 && !isAtMaxJumpHeight()) {
                    setMaxJumpHeight(getWorldPosition().getY());
                    setIsAtMaxJumpHeight(true);
                }

                if (isAtMaxJumpHeight() && getWorldPosition().getY() == getMaxJumpHeight()) {
                    shootPlayer();
                    setIsAtMaxJumpHeight(false);
                }
            }
            invincibilityCheck();
            applyVelocity();
            animateSprite();
            setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
            checkCollisionWithPlayer();
        } else {
            // respawn
            if (isRespawnable()){
                setInvincibilityFrame(0);
                setJumpShootCooldown(0);
                changeStateTo(0);
                setInvincible(false);
                invincibilityCheck();
                setMovingLeft(getgW().getPlayer().getWorldPosition().getX() <= getWorldPosition().getX());
                setWorldPosition(new Vector2(getNotActiveWorldPos()));
                getHitBox().x = getNotActiveHitBoxWorldPos().getX();
                getHitBox().y = getNotActiveHitBoxWorldPos().getY();
                setVelocity(new Vector2(0, 0));
                setScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
                setHp(32);
                setShootCooldown(0);
            } else {
                doWinScreen();
            }

        }
    }

    public void animateSprite(){
        if (getCurrentState() == 0) {
            if (getSpriteWalkFrame() >= 6) {
                setSpriteWalkFrame(0);
                if (getSpriteWalkId() == 4) {
                    setSpriteWalkId(1);
                } else {
                    setSpriteWalkId(getSpriteWalkId() + 1);
                }
            } else {
                setSpriteWalkFrame(getSpriteWalkFrame() + 1);
            }
        } else if (getCurrentState() == 1){
            if (getSpriteWalkId() >= 0){
                // start jump
                getVelocity().setY(-20);
                setSpriteWalkId(5);
                changeStateTo(2);
            }
        }
    }

    public void shootPlayer() {
        if (WaterBossProjectile.spawnCount < 3) {
            setShootCooldown(1);
            Vector2 spawnPos;
            if (getgW().getPlayer().getWorldPosition().getX() < getWorldPosition().getX()) {
                setShootDir(-1);
            } else {
                setShootDir(1);
            }
            if (getShootDir() == -1) {
                spawnPos = new Vector2(getWorldPosition().getX() + (-18 * getgW().TILE_SCALE), getWorldPosition().getY() + (16 * getgW().TILE_SCALE));
            } else {
                spawnPos = new Vector2(getWorldPosition().getX() + (19 * getgW().TILE_SCALE), getWorldPosition().getY() + (16 * getgW().TILE_SCALE));
            }
//        for (int i = -1; i < 2; i++) {
//            Projectile p = new WaterBossProjectile(getgW(), spawnPos);
//            p.getVelocity().setX(((5 - Math.abs(i)) * getShootDir()/3));
//            p.getVelocity().setY((6 - Math.abs(i)) * i /3);
//            getgW().getProjectiles().add(p);
//        }
            Projectile p = new WaterBossProjectile(getgW(), spawnPos);
            p.getVelocity().setX(2 * getShootDir());
            getgW().getProjectiles().add(p);
        }
    }


    public void changeStateTo(int newState){
        if (newState != getCurrentState()){
            setCurrentState(newState);
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
                if (isRespawnable()){
//                    getgW().getEntitiesToDelete().add(this);
                    setRespawnable(false);
                    spawnDeathEffects();
                }
                setDead(true);
                getgW().getEffects().add(getgW().getEnemyFactory().getEnemy(-1, new Vector2(getWorldPosition())));
            }
        }
    }

    public void doWinScreen(){
        bossDeadCooldown++;
        if (bossDeadCooldown > 120 && !bossDeadCooldowned){
            bossDeadCooldowned = true;
            getgW().getSoundManager().playSFX("win");
        } else if (bossDeadCooldown >= 320) {
            PlayerProjectile.projectileCount = 0;
            getgW().setBackground(new Color(10, 10, 10));
            getgW().getSoundManager().stopBGM();
//            getgW().getSoundManager().playBGM("select");
            getgW().setCurrentGameState(4);
            getgW().getLevelSelect().levelBeat[0] = true;
            getgW().getSoundManager().playBGM("win_bgm");
        }
    }

    public void spawnDeathEffects(){
        for (Projectile p : getgW().getProjectiles()) {
            getgW().getEntitiesToDelete().add(p);
        }
        getgW().getSoundManager().playSFX("player_dead");
        getgW().getSoundManager().stopBGM();
        getgW().getEffects().add(getgW().getEnemyFactory().getEnemy(2048, getWorldPosition()));
        getgW().getEffects().add(new DeathEffectProjectile(getgW(), new Vector2(getWorldPosition()), new Vector2(12, 0)));
        getgW().getEffects().add(new DeathEffectProjectile(getgW(), new Vector2(getWorldPosition()), new Vector2(8, 8)));
        getgW().getEffects().add(new DeathEffectProjectile(getgW(), new Vector2(getWorldPosition()), new Vector2(8, -8)));
        getgW().getEffects().add(new DeathEffectProjectile(getgW(), new Vector2(getWorldPosition()), new Vector2(-12, 0)));
        getgW().getEffects().add(new DeathEffectProjectile(getgW(), new Vector2(getWorldPosition()), new Vector2(-8, -8)));
        getgW().getEffects().add(new DeathEffectProjectile(getgW(), new Vector2(getWorldPosition()), new Vector2(-8, 8)));
        getgW().getEffects().add(new DeathEffectProjectile(getgW(), new Vector2(getWorldPosition()), new Vector2(0, -12)));
        getgW().getEffects().add(new DeathEffectProjectile(getgW(), new Vector2(getWorldPosition()), new Vector2(0, 12)));
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
                if (getShootCooldown() <= 0){
                    g2d.drawImage(sprites[getFacing()][getSpriteWalkId()], getScreenPosition().getX(), getScreenPosition().getY(), 32 * getgW().TILE_SCALE, 32 * getgW().TILE_SCALE, null);
                } else {
                    g2d.drawImage(sprites[getFacing() + 2][Math.clamp(getSpriteWalkId(), 0, 5)], getScreenPosition().getX(), getScreenPosition().getY(), 32 * getgW().TILE_SCALE, 32 * getgW().TILE_SCALE, null);
                }
            }
            getgW().getUtil().drawDebugRect(g2d, getHitBox());
        }
    }

    public void loadSprites(String spritePath){
        if (sprites == null){
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

    public int getMaxJumpHeight() { return maxJumpHeight; }

    public void setMaxJumpHeight(int maxJumpHeight) { this.maxJumpHeight = maxJumpHeight; }

    public boolean isAtMaxJumpHeight() {return isAtMaxJumpHeight;}

    public void setIsAtMaxJumpHeight(boolean isAtMaxJumpHeight) {this.isAtMaxJumpHeight = isAtMaxJumpHeight;}

    public int getShootDir() {
        return shootDir;
    }

    public void setShootDir(int shootDir) {
        this.shootDir = shootDir;
    }

    public int getShootCooldown() {
        return shootCooldown;
    }

    public void setShootCooldown(int shootCooldown) {
        this.shootCooldown = shootCooldown;
    }

    public int getMAX_SHOOT_COOLDOWN() {
        return MAX_SHOOT_COOLDOWN;
    }

    public int getJumpShootCooldown() {
        return jumpShootCooldown;
    }

    public void setJumpShootCooldown(int jumpShootCooldown) {
        this.jumpShootCooldown = jumpShootCooldown;
    }

    public int getMAX_JUMP_SHOOT_COOLDOWN() {
        return MAX_JUMP_SHOOT_COOLDOWN;
    }
}
