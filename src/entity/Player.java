package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{


    private static BufferedImage[][] spritesNormal;
    private static BufferedImage[][] spritesNormalInvincible;

    private static BufferedImage[][] spritesFire;
    private static BufferedImage[][] spritesFireInvincible;

    private static BufferedImage[][] spritesWater;
    private static BufferedImage[][] spritesWaterInvincible;

    private static BufferedImage[][] spritesGrass;
    private static BufferedImage[][] spritesGrassInvincible;

    private int currentElement = 0;

    final int collisionCheckTileOffset = 4;

    private Rectangle hitBox;

    private int facing = 1;  // 0 - left 1 - right
    private int spriteId = 0;
    private int spriteAnimateFrame = 0;
    private int moveSpriteAnimId = 0;
    private int lastMoveSpriteAnimId = 0;

    private boolean jumped = false;

    private final int MAX_JUMP_FRAME = 4;
    private int jumpFrame = 0;
    private int moveDelayFrame = 0;

    private Vector2 playerScreenPosition;

    private int shootTimer = 0;
    private int shootMaxTimer = 1;
    private int shootAnim = 0;
    private int shootAnimChangeBackFrame = 0;

    private final int MAX_INVINCIBILITY_FRAME = 60;
    private int knockBackDir = 0;
    private int invincibilityFrame = 0;
    private boolean invincible = false;
    private final int MAX_HP = 7;
    private int hp = 8;
    private boolean alive = true;

    private int waterElementEnergy = 24;
    private int fireElementEnergy = 24;
    private int grassElementEnergy = 24;

    private int respawnFrame;
    private Vector2 respawnPos;
    private final int RESPAWN_FRAME_MAX = 80;

    public Player(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        setRespawnPos(new Vector2(worldPosition));
        setVelocity(new Vector2(0,0));
        // set viewport to player position
        this.setHitBox(new Rectangle(worldPosition.getX() + 12, worldPosition.getY() + 6, 24 * 3 - 7 * 3, 24 * 3 - 3 * 3));

        setPlayerScreenPosition(gW.getUtil().worldPosToScreenPos(worldPosition));
        getgW().getViewportManager().setViewportPositionFromCenterRelative(new Vector2(getWorldPosition()));
        getgW().getViewportManager().setViewportFollowCenterAnchor(new Vector2(getWorldPosition()));
        loadSprites();
    }

    @Override
    public void process(){
//        System.out.println(getVelocity());
        if (isAlive()){
            // show menu
            if (getgW().getKeyInputHandler().menuJustPressed.isJustPressed()){
                getgW().setCurrentGameState(3);
            }
            // Shoot
            if (getgW().getKeyInputHandler().cancelJustPressed.isJustPressed()){
                shoot();
            }
            // Jump
            if (getgW().getKeyInputHandler().confirm && !(isInvincible() && getInvincibilityFrame() < 16)){
                if (!isJumped()){
                    getVelocity().setY(-8);
                    setJumped(true);
                    setJumpFrame(0);
                    getgW().getSoundManager().playSFX("jump");
                } else if (getJumpFrame() <= getMAX_JUMP_FRAME()){
                    getVelocity().setY(getVelocity().getY() + -3);
                    setJumpFrame(getJumpFrame() + 1);
                }
            }
            // Movements
            if (getgW().getKeyInputHandler().rightPressed && !(isInvincible() && getInvincibilityFrame() < 16)){
                if (!isJumped()){
                    if (getMoveDelayFrame() == 0){
                        getVelocity().setX(1);
                        setMoveDelayFrame(getMoveDelayFrame() + 1);
                    } else if(getMoveDelayFrame() < 8){
                        setMoveDelayFrame(getMoveDelayFrame() + 1);
                        getVelocity().setX(0);
                        setSpriteId(1);
                    } else {
                        getVelocity().setX(6);
                        setSpriteId(2);
                        setSpriteAnimateFrame(getSpriteAnimateFrame() + 1);
                    }
                } else {
                    getVelocity().setX(4);
                }
                setFacing(1);
            }
            else if (getgW().getKeyInputHandler().leftPressed && !(isInvincible() && getInvincibilityFrame() < 16)){
                if (!isJumped()){
                    if (getMoveDelayFrame() == 0){
                        getVelocity().setX(-1);
                        setMoveDelayFrame(getMoveDelayFrame() + 1);
                    } else if(getMoveDelayFrame() < 8){
                        setMoveDelayFrame(getMoveDelayFrame() + 1);
                        getVelocity().setX(0);
                        setSpriteId(1);
                    } else {
                        getVelocity().setX(-6);
                        setSpriteId(2);
                        setSpriteAnimateFrame(getSpriteAnimateFrame() + 1);
                    }
                } else {
                    getVelocity().setX(-4);
                }
                setFacing(0);
            }
            // knockBack
            else if (isInvincible()){
                if (Math.abs(getVelocity().getX()) > 0){
                    if (getVelocity().getX() > 0){
    //                    getVelocity().setX(9);
                        getVelocity().setX(getVelocity().getX() - 1);
                    } else {
    //                    getVelocity().setX(-9);
                        getVelocity().setX(getVelocity().getX() + 1);
                    }
                }
                setMoveDelayFrame(0);
                if (!isJumped())
                    setSpriteId(0);
                setMoveSpriteAnimId(0);
            } else {
                getVelocity().setX(0);
                setMoveDelayFrame(0);
                if (!isJumped())
                    setSpriteId(0);
                setMoveSpriteAnimId(0);
            }
            // Ground Check and apply gravity
            if (!isJumped()){
                if (!isOnGround()){
                    setJumped(true);
                }
            } else {
                if (isOnGround() && getVelocity().getY() > 0){
                    setJumped(false);
                    getVelocity().setY(0);
                }
                else
                    getVelocity().setY(getVelocity().getY() + 1);
            }
            // this should be in animateSprite but the game won't work if this is in animateSprite
            if (isJumped() || !isOnGround()){
                setSpriteId(5);
            }

            if (getShootTimer() < getShootMaxTimer()){
                setShootTimer(getShootTimer() + 1);
            }
            invincibilityCheck();
            applyVelocity();
            animateSprite();
            checkDangerTileToApplyDamage();
            getgW().getViewportManager().setViewportFollowCenterAnchor(new Vector2(getWorldPosition()));
            setPlayerScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
        } else {
            invincibilityCheck();
            if (getRespawnFrame() >= getRESPAWN_FRAME_MAX()){
                respawnPlayer();
            } else {
                setRespawnFrame(getRespawnFrame() + 1);
            }
        }
    }

    public void applyVelocity(){
        // limit the velocity
        setVelocity(new Vector2(getVelocity().getX(), Math.clamp(getVelocity().getY(), -48, 48)));
        // apply x
        // check if the player will overlap the any-tile with the current velocity, modify the velocity so that the player won't
        // overlap and get stuck inside a tile
        if (!getgW().getTileManager().checkRectNotIntersectAnyTile(getHitBox().x + getVelocity().getX(), getHitBox().y, getHitBox().width, getHitBox().height)) {
            int tileSnapPosX = 0;
            if (getVelocity().getX() > 0) {
                tileSnapPosX = ((getHitBox().x + getHitBox().width + getVelocity().getX() + 2) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE;
                getVelocity().setX(((getHitBox().x + getHitBox().width + 1) - tileSnapPosX) * -1);
            } else {
                tileSnapPosX = (((getHitBox().x + getVelocity().getX() - 2) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE) + getgW().RENDER_TILE_SIZE;
                getVelocity().setX(((getHitBox().x - 1) - tileSnapPosX) * -1);
//                velocity.x = 0;
            }
        }
        // apply y
        // check if the player will overlap the any-tile with the current velocity, modify the velocity so that the player won't
        // overlap and get stuck inside a tile
        if (!getgW().getTileManager().checkRectNotIntersectAnyTile(getHitBox().x, getHitBox().y + getVelocity().getY(), getHitBox().width, getHitBox().height)) {
            int tileSnapPosY = 0;
            if (getVelocity().getY() > 0) {
                tileSnapPosY = ((getHitBox().y + getHitBox().height + getVelocity().getY() + 4) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE;
                getVelocity().setY(((getHitBox().y + getHitBox().height + 1) - tileSnapPosY) * -1);
            } else {
                tileSnapPosY = (((getHitBox().y + getVelocity().getY() - 4) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE) + getgW().RENDER_TILE_SIZE;
                getVelocity().setY(Math.abs(((getHitBox().y - 1) - tileSnapPosY)));
            }
        }
        getWorldPosition().setX(getWorldPosition().getX() + getVelocity().getX());
        getWorldPosition().setY(getWorldPosition().getY() + getVelocity().getY());
        makeHitBoxFollowWorldPos();
    }

    public void animateSprite(){
        if (getShootAnimChangeBackFrame() <= 0){
            setShootAnim(0);
        } else {
            setShootAnimChangeBackFrame(getShootAnimChangeBackFrame() - 1);
        }
        if (!isJumped()){
            if (getSpriteAnimateFrame() >= 8){
                setSpriteAnimateFrame(0);
                if (getMoveSpriteAnimId() == 0){
                    if (getLastMoveSpriteAnimId() == 2){
                        setMoveSpriteAnimId(1);
                        setLastMoveSpriteAnimId(getMoveSpriteAnimId());
                    } else {
                        setMoveSpriteAnimId(2);
                        setLastMoveSpriteAnimId(getMoveSpriteAnimId());
                    }
                } else {
                    setMoveSpriteAnimId(0);
                }
            }
        } else {
            setSpriteAnimateFrame(0);
            setMoveSpriteAnimId(0);
            setLastMoveSpriteAnimId(getMoveSpriteAnimId());
        }
    }

    public void makeHitBoxFollowWorldPos(){
        getHitBox().x = getWorldPosition().getX() + 12;
        getHitBox().y = getWorldPosition().getY() + 6;
    }

    @Override
    public void render(Graphics2D g2d){
        if (getInvincibilityFrame() % 2 == 0 && isAlive()) {
            if (getCurrentElement() == 0){
                g2d.drawImage(getSpritesNormal()[getFacing() + getShootAnim()][getSpriteId() + getMoveSpriteAnimId()], getPlayerScreenPosition().getX(), getPlayerScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            } else if (getCurrentElement() == 1){
                g2d.drawImage(getSpritesWater()[getFacing() + getShootAnim()][getSpriteId() + getMoveSpriteAnimId()], getPlayerScreenPosition().getX(), getPlayerScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            } else if (getCurrentElement() == 2){
                g2d.drawImage(getSpritesFire()[getFacing() + getShootAnim()][getSpriteId() + getMoveSpriteAnimId()], getPlayerScreenPosition().getX(), getPlayerScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            } else if (getCurrentElement() == 3){
                g2d.drawImage(getSpritesGrass()[getFacing() + getShootAnim()][getSpriteId() + getMoveSpriteAnimId()], getPlayerScreenPosition().getX(), getPlayerScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            }
        } else if (isAlive()){
            if (getCurrentElement() == 0) {
                g2d.drawImage(getSpritesNormalInvincible()[getFacing() + getShootAnim()][getSpriteId() + getMoveSpriteAnimId()], getPlayerScreenPosition().getX(), getPlayerScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            } else if (getCurrentElement() == 1){
                g2d.drawImage(getSpritesWaterInvincible()[getFacing() + getShootAnim()][getSpriteId() + getMoveSpriteAnimId()], getPlayerScreenPosition().getX(), getPlayerScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            } else if (getCurrentElement() == 2){
                g2d.drawImage(getSpritesFireInvincible()[getFacing() + getShootAnim()][getSpriteId() + getMoveSpriteAnimId()], getPlayerScreenPosition().getX(), getPlayerScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            } else if (getCurrentElement() == 3){
                g2d.drawImage(getSpritesGrassInvincible()[getFacing() + getShootAnim()][getSpriteId() + getMoveSpriteAnimId()], getPlayerScreenPosition().getX(), getPlayerScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
            }
        }
        if (getgW().isDebugEnable()){
            Vector2 rPos = getgW().getUtil().worldPosToScreenPos(new Vector2(getHitBox().x, getHitBox().y));
            g2d.drawRect(rPos.getX(), rPos.getY(), getHitBox().width, getHitBox().height);
        }
    }

    public void checkDangerTileToApplyDamage(){
        if (getgW().getTileManager().isTileDangerous(getHitBox().x, getHitBox().y)
        || getgW().getTileManager().isTileDangerous(getHitBox().x + getHitBox().width, getHitBox().y)
        || getgW().getTileManager().isTileDangerous(getHitBox().x, getHitBox().y + getHitBox().height)
        || getgW().getTileManager().isTileDangerous(getHitBox().x + getHitBox().width, getHitBox().y + getHitBox().height)){
            if (getHp() > 0){
                applyDirectDamage(getHp());
            }
        }
    }

    @Override
    public void onHit(Vector2 hitPos){
        if (getgW().isDebugEnable()){
            setHp(8);
        }
        getVelocity().setX(0);
        if (!getgW().isDebugEnable()){
            if (hitPos.getX() < (int)getHitBox().getCenterX()) {
                getgW().getPlayer().setKnockBackDir(0);
                getVelocity().setX(14);
            } else {
                getgW().getPlayer().setKnockBackDir(1);
                getVelocity().setX(-14);
            }
        }
        if (!isInvincible()){
            setHp(getHp() - 1);
            if (hp > 0){
                setInvincible(true);
            } else {
                if (isAlive()){
                    spawnDeathEffects();
                }
                setAlive(false);
            }
        }
    }

    public void onHit(Vector2 hitPos, int element){
        if (hitPos.getX() < getWorldPosition().getX()) {
            getgW().getPlayer().setKnockBackDir(0);
        } else {
            getgW().getPlayer().setKnockBackDir(1);
        }
        if (!isInvincible()){
            int adv = checkElementAdvantage(element);
            if (adv == 1){
                setHp(getHp() - 2);
            } else if (adv == 0){
                setHp(getHp() - 1);
            }
            if (hp > 0){
                setInvincible(true);
            } else {
                if (isAlive()){
                    getgW().getEffects().add(getgW().getEnemyFactory().getEnemy(2048, getWorldPosition()));
                }
                setAlive(false);
            }
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

    public void applyDirectDamage(int damage){
        setHp(getHp() - damage);
        if (getHp() <= 0){
            if (isAlive()){
                spawnDeathEffects();
            }
            setAlive(false);
        } else if (getHp() > getMAX_HP()){
            setHp(getMAX_HP());
        }

    }

    public void addElementEnergy(int element, int toAdd){
        switch (element){
            case 1:
                setWaterElementEnergy(getWaterElementEnergy() + toAdd);
                if (getWaterElementEnergy() > 24){
                    setWaterElementEnergy(24);
                }
                break;
            case 2:
                setFireElementEnergy(getFireElementEnergy() + toAdd);
                if (getFireElementEnergy() > 24){
                    setFireElementEnergy(24);
                }
                break;
            case 3:
                setGrassElementEnergy(getGrassElementEnergy() + toAdd);
                if (getGrassElementEnergy() > 24){
                    setGrassElementEnergy(24);
                }
                break;
        }
    }

    public void respawnPlayer(){

        for (Enemy e : getgW().getEnemies()){
            if (e.getClass() == HpPowerUpEnemy.class){
            ((HpPowerUpEnemy)e).setUsed(false);
            } else if (e.getClass() == EnergyPowerUpEnemy.class){
                ((EnergyPowerUpEnemy)e).setUsed(false);
            }
        }

        getgW().getViewportManager().setViewportFollowingAnchor(true);
        if (getgW().getCurrentLevel().getLevelId() == 0){
            getgW().getSoundManager().playBGM("water");
        }else if (getgW().getCurrentLevel().getLevelId() == 1){
            getgW().getSoundManager().playBGM("fire");
        }else if (getgW().getCurrentLevel().getLevelId() == 2){
            getgW().getSoundManager().playBGM("grass");
        } else if (getgW().getCurrentLevel().getLevelId() == 3){
            getgW().getSoundManager().playBGM("dark");
        }
            
        setRespawnFrame(0);
        setAlive(true);
        setHp(8);
        setVelocity(new Vector2());
        setInvincibilityFrame(0);
        setInvincible(false);
        setWorldPosition(new Vector2(getRespawnPos()));
        setPlayerScreenPosition(getgW().getUtil().worldPosToScreenPos(getWorldPosition()));
        getgW().getViewportManager().setViewportPositionFromCenterRelative(new Vector2(getWorldPosition()));
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

    public void shoot(){
        if (PlayerProjectile.projectileCount < 3 && getShootTimer() >= getShootMaxTimer() && ((getWaterElementEnergy() > 0 && getCurrentElement() == 1) || (getFireElementEnergy() > 0 && getCurrentElement() == 2) || (getGrassElementEnergy() > 0 && getCurrentElement() == 3) || getCurrentElement() == 0)){
            int fireSpeedMod = 1;
            if (getCurrentElement() == 2){
                fireSpeedMod = 2;
            }
            setShootTimer(0);
            setShootAnim(2);
            setShootAnimChangeBackFrame(16);
            Projectile p = new PlayerProjectile(getgW(), new Vector2(getWorldPosition().getX() + 10, getWorldPosition().getY() - 3), getCurrentElement());
            p.setFacing(getFacing());
            p.getVelocity().setX(16 * fireSpeedMod);
            if (getFacing() == 0){
                p.getVelocity().setX(p.getVelocity().getX() * -1);
            }
            getgW().getProjectiles().add(p);
            if (getCurrentElement() == 3){
                p = new PlayerProjectile(getgW(), new Vector2(getWorldPosition().getX() + 10, getWorldPosition().getY() - 3), getCurrentElement());
                p.setFacing(getFacing());
                p.getVelocity().setX(16 * fireSpeedMod);
                p.getVelocity().setY(6);
                if (getFacing() == 0){
                    p.getVelocity().setX(p.getVelocity().getX() * -1);
                }
                getgW().getProjectiles().add(p);
                p = new PlayerProjectile(getgW(), new Vector2(getWorldPosition().getX() + 10, getWorldPosition().getY() - 3), getCurrentElement());
                p.setFacing(getFacing());
                p.getVelocity().setX(16 * fireSpeedMod);
                p.getVelocity().setY(-6);
                if (getFacing() == 0){
                    p.getVelocity().setX(p.getVelocity().getX() * -1);
                }
                getgW().getProjectiles().add(p);
            }

            if (!getgW().isDebugEnable()){
                if (getCurrentElement() == 1){
                    setWaterElementEnergy(getWaterElementEnergy() - 1);
                } else if (getCurrentElement() == 2){
                    setFireElementEnergy(getFireElementEnergy() - 1);
                } else if (getCurrentElement() == 3){
                    setGrassElementEnergy(getGrassElementEnergy() - 1);
                }
            }
            getgW().getSoundManager().playSFX("attack");
        }
    }

    void loadSprites(){
        if (getSpritesNormal() == null){
            setSpritesNormal(getgW().getUtil().loadGraphic2D("sprites/player.png", 24));
            setSpritesNormalInvincible(getgW().getUtil().loadGraphic2D("sprites/player_inv.png", 24));

            setSpritesFire(getgW().getUtil().loadGraphic2D("sprites/player_fire.png", 24));
            setSpritesFireInvincible(getgW().getUtil().loadGraphic2D("sprites/player_fire_inv.png", 24));

            setSpritesWater(getgW().getUtil().loadGraphic2D("sprites/player_water.png", 24));
            setSpritesWaterInvincible(getgW().getUtil().loadGraphic2D("sprites/player_water_inv.png", 24));

            setSpritesGrass(getgW().getUtil().loadGraphic2D("sprites/player_grass.png", 24));
            setSpritesGrassInvincible(getgW().getUtil().loadGraphic2D("sprites/player_grass_inv.png", 24));
        }

    }

    public int checkElementAdvantage(int attackElement){
        if ((attackElement == 1 && getCurrentElement() == 2) || (attackElement == 2 && getCurrentElement() == 3) || (attackElement == 3 && getCurrentElement() == 1)){
            return 1;
        }
        return 0;
    }

    boolean isOnGround(){
        return (getgW().getTileManager().isTileBlocking(getHitBox().x, getHitBox().y + getHitBox().height + collisionCheckTileOffset) ||
                getgW().getTileManager().isTileBlocking(getHitBox().x + getHitBox().width, getHitBox().y + getHitBox().height + collisionCheckTileOffset)) &&
                (((getHitBox().y + getHitBox().height + getVelocity().getY() + 1) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE) - (getHitBox().y + getHitBox().height) < 2;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public int getSpriteId() {
        return spriteId;
    }

    public void setSpriteId(int spriteId) {
        this.spriteId = spriteId;
    }

    public int getSpriteAnimateFrame() {
        return spriteAnimateFrame;
    }

    public void setSpriteAnimateFrame(int spriteAnimateFrame) {
        this.spriteAnimateFrame = spriteAnimateFrame;
    }

    public int getMoveSpriteAnimId() {
        return moveSpriteAnimId;
    }

    public void setMoveSpriteAnimId(int moveSpriteAnimId) {
        this.moveSpriteAnimId = moveSpriteAnimId;
    }

    public int getLastMoveSpriteAnimId() {
        return lastMoveSpriteAnimId;
    }

    public void setLastMoveSpriteAnimId(int lastMoveSpriteAnimId) {
        this.lastMoveSpriteAnimId = lastMoveSpriteAnimId;
    }

    public boolean isJumped() {
        return jumped;
    }

    public void setJumped(boolean jumped) {
        this.jumped = jumped;
    }

    public int getMAX_JUMP_FRAME() {
        return MAX_JUMP_FRAME;
    }

    public int getJumpFrame() {
        return jumpFrame;
    }

    public void setJumpFrame(int jumpFrame) {
        this.jumpFrame = jumpFrame;
    }

    public int getMoveDelayFrame() {
        return moveDelayFrame;
    }

    public void setMoveDelayFrame(int moveDelayFrame) {
        this.moveDelayFrame = moveDelayFrame;
    }

    public Vector2 getPlayerScreenPosition() {
        return playerScreenPosition;
    }

    public void setPlayerScreenPosition(Vector2 playerScreenPosition) {
        this.playerScreenPosition = playerScreenPosition;
    }

    public int getShootTimer() {
        return shootTimer;
    }

    public void setShootTimer(int shootTimer) {
        this.shootTimer = shootTimer;
    }

    public int getShootMaxTimer() {
        return shootMaxTimer;
    }

    public void setShootMaxTimer(int shootMaxTimer) {
        this.shootMaxTimer = shootMaxTimer;
    }

    public int getShootAnim() {
        return shootAnim;
    }

    public void setShootAnim(int shootAnim) {
        this.shootAnim = shootAnim;
    }

    public int getShootAnimChangeBackFrame() {
        return shootAnimChangeBackFrame;
    }

    public void setShootAnimChangeBackFrame(int shootAnimChangeBackFrame) {
        this.shootAnimChangeBackFrame = shootAnimChangeBackFrame;
    }

    public int getMAX_INVINCIBILITY_FRAME() {
        return MAX_INVINCIBILITY_FRAME;
    }

    public int getKnockBackDir() {
        return knockBackDir;
    }

    public void setKnockBackDir(int knockBackDir) {
        this.knockBackDir = knockBackDir;
    }

    public int getInvincibilityFrame() {
        return invincibilityFrame;
    }

    public void setInvincibilityFrame(int invincibilityFrame) {
        this.invincibilityFrame = invincibilityFrame;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
        if (this.hp > 8){
            this.hp = 8;
        }
    }


    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getRespawnFrame() {
        return respawnFrame;
    }

    public void setRespawnFrame(int respawnFrame) {
        this.respawnFrame = respawnFrame;
    }

    public int getRESPAWN_FRAME_MAX() {
        return RESPAWN_FRAME_MAX;
    }

    public Vector2 getRespawnPos() {
        return respawnPos;
    }

    public void setRespawnPos(Vector2 respawnPos) {
        this.respawnPos = respawnPos;
    }

    public int getMAX_HP() {
        return MAX_HP;
    }

    public int getCurrentElement() {
        return currentElement;
    }

    public void setCurrentElement(int currentElement) {
        this.currentElement = currentElement;
    }

    public int getWaterElementEnergy() {
        return waterElementEnergy;
    }

    public void setWaterElementEnergy(int waterElementEnergy) {
        this.waterElementEnergy = waterElementEnergy;
    }

    public int getFireElementEnergy() {
        return fireElementEnergy;
    }

    public void setFireElementEnergy(int fireElementEnergy) {
        this.fireElementEnergy = fireElementEnergy;
    }

    public int getGrassElementEnergy() {
        return grassElementEnergy;
    }

    public void setGrassElementEnergy(int grassElementEnergy) {
        this.grassElementEnergy = grassElementEnergy;
    }

    public static BufferedImage[][] getSpritesNormal() {
        return spritesNormal;
    }

    public static void setSpritesNormal(BufferedImage[][] spritesNormal) {
        Player.spritesNormal = spritesNormal;
    }

    public static BufferedImage[][] getSpritesNormalInvincible() {
        return spritesNormalInvincible;
    }

    public static void setSpritesNormalInvincible(BufferedImage[][] spritesNormalInvincible) {
        Player.spritesNormalInvincible = spritesNormalInvincible;
    }

    public static BufferedImage[][] getSpritesFire() {
        return spritesFire;
    }

    public static void setSpritesFire(BufferedImage[][] spritesFire) {
        Player.spritesFire = spritesFire;
    }

    public static BufferedImage[][] getSpritesFireInvincible() {
        return spritesFireInvincible;
    }

    public static void setSpritesFireInvincible(BufferedImage[][] spritesFireInvincible) {
        Player.spritesFireInvincible = spritesFireInvincible;
    }

    public static BufferedImage[][] getSpritesWater() {
        return spritesWater;
    }

    public static void setSpritesWater(BufferedImage[][] spritesWater) {
        Player.spritesWater = spritesWater;
    }

    public static BufferedImage[][] getSpritesWaterInvincible() {
        return spritesWaterInvincible;
    }

    public static void setSpritesWaterInvincible(BufferedImage[][] spritesWaterInvincible) {
        Player.spritesWaterInvincible = spritesWaterInvincible;
    }

    public static BufferedImage[][] getSpritesGrass() {
        return spritesGrass;
    }

    public static void setSpritesGrass(BufferedImage[][] spritesGrass) {
        Player.spritesGrass = spritesGrass;
    }

    public static BufferedImage[][] getSpritesGrassInvincible() {
        return spritesGrassInvincible;
    }

    public static void setSpritesGrassInvincible(BufferedImage[][] spritesGrassInvincible) {
        Player.spritesGrassInvincible = spritesGrassInvincible;
    }
}
