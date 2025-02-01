package entity;

import main.GameWindow;
import main.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{


    BufferedImage[][] sprites;

    final int collisionCheckTileOffset = 4;

//    public Vector2 screenPosition;

    private Rectangle hitBox;

    private int facing = 1;  // 0 - left 1 - right
    private int spriteId = 0;
    private int spriteAnimateFrame = 0;
    private int moveSpriteAnimId = 0;
    private int lastMoveSpriteAnimId = 0;

//    public Vector2 velocity = new Vector2(0, 0);
    private boolean jumped = false;

    private final int MAX_JUMP_FRAME = 4;
    private int jumpFrame = 0;
    private int moveDelayFrame = 0;

    private Vector2 playerScreenPosition;

    private int shootTimer = 0;
    private int shootMaxTimer = 1;
    private int shootAnim = 0;
    private int shootAnimChangeBackFrame = 0;

    private final int MAX_INVINCIBILITY_FRAME = 36;
    private int knockBackDir = 0;
    private int invincibilityFrame = 0;
    private boolean invincible = false;
    private int hp = 7;

    public Player(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
        setVelocity(new Vector2(0,0));
        // center the player on the screen
        setPlayerScreenPosition(gW.util.worldPosToScreenPos(worldPosition));
        this.setHitBox(new Rectangle(worldPosition.getX() + 12, worldPosition.getY() + 6, 24 * 3 - 7 * 3, 24 * 3 - 3 * 3));
        gW.viewportPosition = new Vector2((gW.SCREEN_WIDTH / 2) - worldPosition.getX(), (gW.SCREEN_HEIGHT / 2)  - worldPosition.getY());
        loadSprites();
    }

    @Override
    public void process(){
//        System.out.println(worldPosition);
        // Jump
        if (getgW().keyInputHandler.confirm && !(isInvincible() && getInvincibilityFrame() < 8)){
            if (!isJumped()){
                getVelocity().setY(-8);
                setJumped(true);
                setJumpFrame(0);
            } else if (getJumpFrame() <= getMAX_JUMP_FRAME()){
                getVelocity().setY(getVelocity().getY() + -3);
                setJumpFrame(getJumpFrame() + 1);
            }
        }
        // Movements
        if (getgW().keyInputHandler.rightPressed && !(isInvincible() && getInvincibilityFrame() < 8)){
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
        else if (getgW().keyInputHandler.leftPressed && !(isInvincible() && getInvincibilityFrame() < 8)){
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
        else if (isInvincible() && getInvincibilityFrame() < 8){
            if (getKnockBackDir() == 0){
                getVelocity().setX(8);
            } else {
                getVelocity().setX(-8);
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

//        if (gW.keyInputHandler.cancel){
//            shoot();
//        }

        if (getShootTimer() < getShootMaxTimer()){
            setShootTimer(getShootTimer() + 1);
        }

        invincibilityCheck();
        applyVelocity();
        animateSprite();
        getgW().viewportPosition = new Vector2((getgW().SCREEN_WIDTH / 2) - getWorldPosition().getX(), (getgW().SCREEN_HEIGHT / 2)  - getWorldPosition().getY());
//        gW.screenPosition = new Vector2((gW.SCREEN_WIDTH / 2) - worldPosition.x, gW.screenPosition.y);
        setPlayerScreenPosition(getgW().util.worldPosToScreenPos(getWorldPosition()));
//        System.out.println(moveSpriteAnimId);
//        System.out.println(velocity.x + " " + velocity.y);
//        System.out.println(((hitBox.y + hitBox.height + velocity.y + 2) / gW.RENDER_TILE_SIZE) * gW.RENDER_TILE_SIZE - (hitBox.y + hitBox.height));
    }

    public void applyVelocity(){
        // apply x
        // check if the player will overlap the any-tile with the current velocity, modify the velocity so that the player won't
        // overlap and get stuck inside a tile
        if (!getgW().tileManager.checkRectNotIntersectAnyTile(getHitBox().x + getVelocity().getX(), getHitBox().y, getHitBox().width, getHitBox().height)) {
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
        if (!getgW().tileManager.checkRectNotIntersectAnyTile(getHitBox().x, getHitBox().y + getVelocity().getY(), getHitBox().width, getHitBox().height)) {
            int tileSnapPosY = 0;
//            System.out.println(tileSnapPosY);
            if (getVelocity().getY() > 0) {
                tileSnapPosY = ((getHitBox().y + getHitBox().height + getVelocity().getY() + 4) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE;
                getVelocity().setY(((getHitBox().y + getHitBox().height + 1) - tileSnapPosY) * -1);
//                System.out.println("!");
            } else {
                tileSnapPosY = (((getHitBox().y + getVelocity().getY() - 4) / getgW().RENDER_TILE_SIZE) * getgW().RENDER_TILE_SIZE) + getgW().RENDER_TILE_SIZE;
                getVelocity().setY(Math.abs(((getHitBox().y - 1) - tileSnapPosY)));
//                System.out.println("*");
//                velocity.y = 0;
            }
        }
        getWorldPosition().setX(getWorldPosition().getX() + getVelocity().getX());
        getWorldPosition().setY(getWorldPosition().getY() + getVelocity().getY());
        makeHitBoxFollowWorldPos();
//        hitBox.x += velocity.x;
//        hitBox.y += velocity.y;

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
        if (getInvincibilityFrame() % 2 == 0) {
            g2d.drawImage(sprites[getFacing() + getShootAnim()][getSpriteId() + getMoveSpriteAnimId()], getPlayerScreenPosition().getX(), getPlayerScreenPosition().getY(), 24 * getgW().TILE_SCALE, 24 * getgW().TILE_SCALE, null);
        }
        Vector2 rPos = getgW().util.worldPosToScreenPos(new Vector2(getHitBox().x, getHitBox().y));
        g2d.drawRect(rPos.getX(), rPos.getY(), getHitBox().width, getHitBox().height);
//        rPos = gW.util.worldPosToScreenPos(new Vector2(worldPosition.x / gW.RENDER_TILE_SIZE, worldPosition.y / gW.RENDER_TILE_SIZE));
//        g2d.drawRect(rPos.x, rPos.y, gW.RENDER_TILE_SIZE, gW.RENDER_TILE_SIZE);
//        g2d.drawRect(worldPosition.x, worldPosition.y, 24, 24);

    }

    @Override
    public void onHit(Vector2 hitPos){
        if (hitPos.getX() < getWorldPosition().getX()) {
            getgW().player.setKnockBackDir(0);
        } else {
            getgW().player.setKnockBackDir(1);
        }
        if (!isInvincible()){
            setHp(getHp() - 1);
            setInvincible(true);
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
        if (PlayerProjectile.projectileCount < 3 && getShootTimer() >= getShootMaxTimer()){
            setShootTimer(0);
            setShootAnim(2);
            setShootAnimChangeBackFrame(16);
            Projectile p = new PlayerProjectile(getgW(), new Vector2(getWorldPosition().getX() + 10, getWorldPosition().getY() - 3));
            p.setFacing(getFacing());
            p.getVelocity().setX(16);
            if (getFacing() == 0){
                p.getVelocity().setX(p.getVelocity().getX() * -1);
            }
            getgW().projectiles.add(p);
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
        int tileAt = getHitBox().x / getgW().RENDER_TILE_SIZE;
        if (getVelocity().getX() > 0){
            return ((tileAt * getgW().RENDER_TILE_SIZE) + (getgW().RENDER_TILE_SIZE - getHitBox().height)) - 1;
        }
        else
            return tileAt * getgW().RENDER_TILE_SIZE;
    }

    int getPosYAtWallEdge(){
        int tileAt = getHitBox().y / getgW().RENDER_TILE_SIZE;
        if (getVelocity().getY() > 0){
//            return ((tileAt * gW.RENDER_TILE_SIZE) + (gW.RENDER_TILE_SIZE - hitBox.height)) + gW.RENDER_TILE_SIZE - 1;
            return ((tileAt * getgW().RENDER_TILE_SIZE) + (getgW().RENDER_TILE_SIZE - getHitBox().height)) - 1;
        }
        else
            return tileAt * getgW().RENDER_TILE_SIZE;
    }

    boolean isOnGround(){
        return (getgW().tileManager.isTileBlocking(getHitBox().x, getHitBox().y + getHitBox().height + collisionCheckTileOffset) ||
                getgW().tileManager.isTileBlocking(getHitBox().x + getHitBox().width, getHitBox().y + getHitBox().height + collisionCheckTileOffset)) &&
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
    }


}
