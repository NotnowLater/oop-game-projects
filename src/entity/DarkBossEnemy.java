package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DarkBossEnemy extends Enemy{
    private static BufferedImage[][] sprites;

    private int spriteWalkFrame = 0;
    private int spriteWalkId = 0;
    private int lastSpriteWalkId = 0;

    private Vector2 notActiveWorldPos;
    private Vector2 notActiveHitBoxWorldPos;

    private boolean inAttackRange;

    private boolean movingLeft;
    private boolean movingUp;

    private int maxJumpHeight;
    private boolean isAtMaxJumpHeight;

    final int MAX_INVINCIBILITY_FRAME = 30;
    private int invincibilityFrame = 0;
    private int hp = 52;

    private int currentState = 0;

    private int shootDir = 1;
    private int shootCooldown = 0;
    private final int MAX_SHOOT_COOLDOWN = 360;

    private int jumpShootCooldown;
    private final int MAX_JUMP_SHOOT_COOLDOWN = 60;

    private int bossDeadCooldown;
    private boolean bossDeadCooldowned = false;

    public DarkBossEnemy(GameWindow gW, Vector2 worldPos, String spritePath, int element){
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
        setMovingLeft(true);
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
            // walking state
            if (getCurrentState() == 0){
                if (isMovingLeft() && (getgW().getTileManager().isTileBlocking(getHitBox().x - getCollisionCheckTileOffset(), getHitBox().y))){
                    setMovingLeft(false);
                } else if (!isMovingLeft() && (getgW().getTileManager().isTileBlocking(getHitBox().x + getHitBox().width + getCollisionCheckTileOffset(), getHitBox().y))){
                    setMovingLeft(true);
                }
                if (isMovingUp() && (getgW().getTileManager().isTileBlocking(getHitBox().x, getHitBox().y - getCollisionCheckTileOffset()) || getWorldPosition().getY() < 58 * 48)){
                    setMovingUp(false);
                } else if (!isMovingUp() && (getgW().getTileManager().isTileBlocking(getHitBox().x, getHitBox().y + getHitBox().height + getCollisionCheckTileOffset()))){
                    setMovingUp(true);
                }
                if (!isMovingLeft()){
                    getVelocity().setX(6);
                } else {
                    getVelocity().setX(-6);
                }
                if (!isMovingUp()){
                    getVelocity().setY(6);
                } else {
                    getVelocity().setY(-6);
                }

                if (getShootCooldown() >= getMAX_JUMP_SHOOT_COOLDOWN() && getWorldPosition().getY() < 61 * 48){
                    changeStateTo(1);
                    setSpriteWalkId(0);
                } else {
                    setShootCooldown(getShootCooldown() + 1);
                }
            }
            // jump state
            else if(getCurrentState() == 1){
                setShootCooldown(0);
                setVelocity(new Vector2());
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
                setHp(52);
                setShootCooldown(0);
            } else {
                doWinScreen();
            }
        }
    }

    public void animateSprite(){
        if (getCurrentState() == 0) {
            if (getSpriteWalkFrame() >= 4) {
                setSpriteWalkFrame(0);
                if (getSpriteWalkId() == 4) {
                    setSpriteWalkId(0);
                } else {
                    setSpriteWalkId(getSpriteWalkId() + 1);
                }
            } else {
                setSpriteWalkFrame(getSpriteWalkFrame() + 1);
            }
        } else if (getCurrentState() == 1){
            if (getSpriteWalkFrame() >= 4) {
                setSpriteWalkFrame(0);
                if (getSpriteWalkId() == 4) {
                    setSpriteWalkId(0);
                    shootPlayer();
                } else {
                    setSpriteWalkId(getSpriteWalkId() + 1);
                }
            } else {
                setSpriteWalkFrame(getSpriteWalkFrame() + 1);
            }
        } else if (getCurrentState() == 2){
            if (getSpriteWalkFrame() >= 4) {
                setSpriteWalkFrame(0);
                if (getSpriteWalkId() == 0) {
                    changeStateTo(0);
                } else {
                    setSpriteWalkId(getSpriteWalkId() - 1);
                }
            } else {
                setSpriteWalkFrame(getSpriteWalkFrame() + 1);
            }
        }
    }

    public void shootPlayer() {
//        for (int i = -12; i < 12; i+=2) {
//            if (i < 0){
//                setShootDir(-1);
//            } else {
//                setShootDir(1);
//            }
//            if (i != 0){
//                Projectile p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
//                p.getVelocity().setX(((12 - Math.abs(i)) * getShootDir()));
//                p.getVelocity().setY((6 + Math.abs(i / 2)));
//                getgW().getProjectiles().add(p);
//            }
        Projectile p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(0);
        p.getVelocity().setY(-8);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(2);
        p.getVelocity().setY(-6);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(4);
        p.getVelocity().setY(-4);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(6);
        p.getVelocity().setY(-2);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(8);
        p.getVelocity().setY(0);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(6);
        p.getVelocity().setY(2);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(4);
        p.getVelocity().setY(4);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(2);
        p.getVelocity().setY(6);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(0);
        p.getVelocity().setY(8);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(-2);
        p.getVelocity().setY(6);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(-4);
        p.getVelocity().setY(4);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(-6);
        p.getVelocity().setY(2);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(-8);
        p.getVelocity().setY(0);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(-6);
        p.getVelocity().setY(-2);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(-4);
        p.getVelocity().setY(-4);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(-2);
        p.getVelocity().setY(-6);
        getgW().getProjectiles().add(p);

        p = new DarkBossProjectile(getgW(), new Vector2(getWorldPosition().getX() + 16,  getWorldPosition().getY() + 12));
        p.getVelocity().setX(0);
        p.getVelocity().setY(-8);
        getgW().getProjectiles().add(p);

        changeStateTo(2);
        setSpriteWalkId(4);
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
//            if (adv == 1){
//                setHp(getHp() - 2);
//            } else if (adv == 0){
//                setHp(getHp() - 1);
//            }
            if (element != 0){
                setHp(getHp() - 2);
            } else {
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
            getgW().getSoundManager().playSFX("win_end");
        } else if (bossDeadCooldown >= 356) {
            PlayerProjectile.projectileCount = 0;
            getgW().getSoundManager().stopBGM();
//            getgW().getSoundManager().playBGM("select");
            getgW().setCurrentGameState(6);
            getgW().getSoundManager().playBGM("end");
        }
    }

    public void spawnDeathEffects(){
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
                if (getCurrentState() == 0){
                    g2d.drawImage(sprites[0][getSpriteWalkId()], getScreenPosition().getX(), getScreenPosition().getY(), 32 * getgW().TILE_SCALE, 32 * getgW().TILE_SCALE, null);
                }
                else {
                    g2d.drawImage(sprites[1][getSpriteWalkId()], getScreenPosition().getX(), getScreenPosition().getY(), 32 * getgW().TILE_SCALE, 32 * getgW().TILE_SCALE, null);
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

    public boolean isMovingUp() {
        return movingUp;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }
}
