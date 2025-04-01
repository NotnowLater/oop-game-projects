package main;

import entity.PlayerProjectile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelSelect implements Processable, Renderable{

    private GameWindow gW;

    boolean cursorMoved = false;

    private int cursorPosX = 1;
    private int cursorPosY;

    BufferedImage levelSelect;

    BufferedImage stage1;
    BufferedImage stage2;
    BufferedImage stage3;
    BufferedImage stage4;

    BufferedImage stage1g;
    BufferedImage stage2g;
    BufferedImage stage3g;
    BufferedImage stage4g;

    BufferedImage selectCursor;

    public Color bg1 = new Color(77, 101, 180);
    public Color bg2 = new Color(174, 35, 52);
    public Color bg3 = new Color(35, 144, 99);
    public Color bg4 = new Color(144, 94, 169);

    public BufferedImage[] bgSprites;
    int bgAnimId;
    int bgFrame;

    public boolean[] levelBeat = new boolean[4];

    public LevelSelect(GameWindow gW){
        this.gW = gW;
        loadSprites();
    }

    @Override
    public void process() {

        if (cursorPosY == 1){
            gW.changeBGColorTo(bg4);
        } else if (cursorPosX == 0){
            gW.changeBGColorTo(bg1);
        } else if (cursorPosX == 1){
            gW.changeBGColorTo(bg2);
        } else {
            gW.changeBGColorTo(bg3);
        }

        if (bgFrame >= 1){
            bgFrame = 0;
            bgAnimId++;
            if (bgAnimId == 16){
                bgAnimId = 0;
            }
        } else {
            bgFrame++;
        }
        if (gW.getKeyInputHandler().leftJustPressed.isJustPressed()){
            if (cursorPosX > 0){
                cursorPosX--;
                gW.getSoundManager().playSFX("cursor");
            }
        } else if (gW.getKeyInputHandler().rightJustPressed.isJustPressed()) {
            if (cursorPosX < 2){
                cursorPosX++;
                gW.getSoundManager().playSFX("cursor");
            }
        } else if (gW.getKeyInputHandler().upJustPressed.isJustPressed()){
            cursorPosY = 1;
            gW.getSoundManager().playSFX("cursor");
        } else if (gW.getKeyInputHandler().downJustPressed.isJustPressed()) {
            cursorPosY = 0;
            gW.getSoundManager().playSFX("cursor");
        }
        if (gW.getKeyInputHandler().cancelJustPressed.isJustPressed()){
            if (cursorPosY == 1 && (levelBeat[0] && levelBeat[1] && levelBeat[2])){
                gW.setCurrentLevel(gW.getLevel4());
                gW.getViewportManager().setViewportPositionFromCenterRelative(new Vector2(gW.getCurrentLevel().getPlayer().getWorldPosition()));
                gW.getSoundManager().stopBGM();
                gW.getSoundManager().playBGM("dark");
                gW.setBackground(new Color(107, 62, 117));
                gW.setCurrentGameState(2);
                gW.getViewportManager().setViewportFollowingAnchor(true);
                PlayerProjectile.projectileCount = 0;
            } else if (cursorPosX == 0 && cursorPosY == 0 && !levelBeat[0]) {
                gW.setCurrentLevel(gW.getLevel1());
                gW.getViewportManager().setViewportPositionFromCenterRelative(new Vector2(gW.getCurrentLevel().getPlayer().getWorldPosition()));
                gW.getSoundManager().stopBGM();
                gW.getSoundManager().playBGM("water");
                gW.setBackground(new Color(77, 155, 230));
                gW.setCurrentGameState(2);
                gW.getViewportManager().setViewportFollowingAnchor(true);
                PlayerProjectile.projectileCount = 0;
            } else if (cursorPosX == 1 && cursorPosY == 0 &&!levelBeat[1]){
                gW.setCurrentLevel(gW.getLevel2());
                gW.getViewportManager().setViewportPositionFromCenterRelative(new Vector2(gW.getCurrentLevel().getPlayer().getWorldPosition()));
                gW.getSoundManager().stopBGM();
                gW.getSoundManager().playBGM("fire");
                gW.setBackground(new Color(238, 231, 203));
                gW.setCurrentGameState(2);
                gW.getViewportManager().setViewportFollowingAnchor(true);
                PlayerProjectile.projectileCount = 0;
            } else if (cursorPosX == 2 && cursorPosY == 0 && !levelBeat[2]){ //dew
                gW.setCurrentLevel(gW.getLevel3());
                gW.getViewportManager().setViewportPositionFromCenterRelative(new Vector2(gW.getCurrentLevel().getPlayer().getWorldPosition()));
                gW.getSoundManager().stopBGM();
                gW.getSoundManager().playBGM("grass");
                gW.setBackground(new Color(205, 223, 208));
                gW.setCurrentGameState(2);
                gW.getViewportManager().setViewportFollowingAnchor(true);
                PlayerProjectile.projectileCount = 0;
            } else {
                gW.getSoundManager().playSFX("error");
            }
        } else if (gW.getKeyInputHandler().confirmJustPressed.isJustPressed()){
            gW.getSoundManager().stopBGM();
            gW.getSoundManager().playBGM("main");
            gW.changeBGColorTo(new Color(10, 10, 10));
            gW.setCurrentGameState(0);
        }
        if (cursorPosX < 0){
            cursorPosX = 0;
        }
        if (cursorPosX > 2){
            cursorPosX = 2;
        }

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(bgSprites[bgAnimId], 0, 0, 384 * 3, 224 * 3, null);
        g2d.drawImage(levelSelect, (16 * 7) * 3, 16 * 3, 192 * 3, 16 * 3, null);
        if (levelBeat[0] && levelBeat[1] && levelBeat[2]){
            g2d.drawImage(stage4, (16 * 11) * 3, 16 * 3 * 3, 48 * 3, 48 * 3, null);
        } else {
            g2d.drawImage(stage4g, (16 * 11) * 3, 16 * 3 * 3, 48 * 3, 48 * 3, null);
        }
        if (!levelBeat[0]){
            g2d.drawImage(stage1, (16 * 5) * 3, 16 * 3 * 8, 48 * 3, 48 * 3, null);
        } else {
            g2d.drawImage(stage1g, (16 * 5) * 3, 16 * 3 * 8, 48 * 3, 48 * 3, null);
        }
        if (!levelBeat[1]){
            g2d.drawImage(stage2, (16 * 11) * 3, 16 * 3 * 8, 48 * 3, 48 * 3, null);
        } else {
            g2d.drawImage(stage2g, (16 * 11) * 3, 16 * 3 * 8, 48 * 3, 48 * 3, null);
        }
        if (!levelBeat[2]){
            g2d.drawImage(stage3, (16 * 17) * 3, 16 * 3 * 8, 48 * 3, 48 * 3, null);
        } else {
            g2d.drawImage(stage3g, (16 * 17) * 3, 16 * 3 * 8, 48 * 3, 48 * 3, null);
        }
        if (cursorPosY == 1){
            g2d.drawImage(selectCursor, (16 * 11) * 3, 16 * 3 * 3, 48 * 3, 48 * 3, null);
        } else {
            if (cursorPosX == 0){
                g2d.drawImage(selectCursor, (16 * 5) * 3, 16 * 3 * 8, 48 * 3, 48 * 3, null);
            } else if(cursorPosX == 1){
                g2d.drawImage(selectCursor, (16 * 11) * 3, 16 * 3 * 8, 48 * 3, 48 * 3, null);
            } else {
                g2d.drawImage(selectCursor, (16 * 17) * 3, 16 * 3 * 8, 48 * 3, 48 * 3, null);
            }
        }
    }

    public void loadSprites(){
        try{
            levelSelect = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/level_select.png"));
            stage1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/stage1.png"));
            stage2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/stage2.png"));
            stage3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/stage3.png"));
            stage4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/stage4.png"));

            stage1g = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/stage1g.png"));
            stage2g = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/stage2g.png"));
            stage3g = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/stage3g.png"));
            stage4g = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/stage4g.png"));

            selectCursor = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/select_cursor.png"));

            bgSprites = gW.getUtil().loadGraphic1D("sprites/select_bg.png", new Vector2(384, 224));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
