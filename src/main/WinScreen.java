package main;

import entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WinScreen implements Processable, Renderable{

    private GameWindow gW;

    BufferedImage panel;

    BufferedImage passwordDot;

    BufferedImage water;
    BufferedImage fire;
    BufferedImage grass;

    int bgAnimId;
    int bgFrame;


    private int spriteAnimateFrame = 0;
    private int moveSpriteAnimId = 0;
    private int lastMoveSpriteAnimId = 0;

    public WinScreen(GameWindow gW){
        this.gW = gW;
        loadGraphics();
    }

    @Override
    public void process() {

        if (bgFrame >= 1){
            bgFrame = 0;
            bgAnimId++;
            if (bgAnimId == 16){
                bgAnimId = 0;
            }
        } else {
            bgFrame++;
        }

        if (gW.getKeyInputHandler().cancelJustPressed.isJustPressed()){
            gW.setCurrentGameState(1);
            gW.getSoundManager().stopBGM();
            gW.getSoundManager().playBGM("select");
        }

        if (gW.getCurrentLevel().getLevelId() == 3){
            gW.changeBGColorTo(gW.getLevelSelect().bg4);
        } else if (gW.getCurrentLevel().getLevelId() == 0){
            gW.changeBGColorTo(gW.getLevelSelect().bg1);
        } else if (gW.getCurrentLevel().getLevelId() == 1){
            gW.changeBGColorTo(gW.getLevelSelect().bg2);
        } else {
            gW.changeBGColorTo(gW.getLevelSelect().bg3);
        }

        if (spriteAnimateFrame >= 8) {
            spriteAnimateFrame = 0;
            if (moveSpriteAnimId == 2) {
                if (lastMoveSpriteAnimId == 4) {
                    moveSpriteAnimId = 3;
                    lastMoveSpriteAnimId = moveSpriteAnimId;
                } else {
                    moveSpriteAnimId = 4;
                    lastMoveSpriteAnimId = moveSpriteAnimId;
                }
            } else {
                moveSpriteAnimId = 2;
            }
        } else {
            spriteAnimateFrame++;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(gW.getLevelSelect().bgSprites[bgAnimId], 0, 0, 384 * 3, 224 * 3, null);
        g2d.drawImage(panel, 0, 0, 24 * 48, 14 * 48, null);

        if (gW.getCurrentLevel().getLevelId() == 0){
            g2d.drawImage(Player.getSpritesWater()[0][moveSpriteAnimId], 5 * 48, 5 * 48, 24 * 3, 24 * 3, null);
            g2d.drawImage(water, 3 * 48, 9 * 48, 80 * 3, 48, null);
        } else if (gW.getCurrentLevel().getLevelId() == 1){
            g2d.drawImage(Player.getSpritesFire()[0][moveSpriteAnimId], 5 * 48, 5 * 48, 24 * 3, 24 * 3, null);
            g2d.drawImage(fire, 3 * 48, 9 * 48, 64 * 3, 48, null);
        } else if (gW.getCurrentLevel().getLevelId() == 2){
            g2d.drawImage(Player.getSpritesGrass()[0][moveSpriteAnimId], 5 * 48, 5 * 48, 24 * 3, 24 * 3, null);
            g2d.drawImage(grass, 3 * 48, 9 * 48, 80 * 3, 48, null);
        }
        if (gW.getLevelSelect().levelBeat[0]){
            g2d.drawImage(passwordDot, 15 * 48, 5 * 48, 48, 48, null);
            g2d.drawImage(passwordDot, 15 * 48, 7 * 48, 48, 48, null);
        }
        if (gW.getLevelSelect().levelBeat[1]){
            g2d.drawImage(passwordDot, 16 * 48, 6 * 48, 48, 48, null);
            g2d.drawImage(passwordDot, 18 * 48, 9 * 48, 48, 48, null);
        }
        if (gW.getLevelSelect().levelBeat[2]){
            g2d.drawImage(passwordDot, 19 * 48, 6 * 48, 48, 48, null);
            g2d.drawImage(passwordDot, 17 * 48, 8 * 48, 48, 48, null);
        }

    }

    public void loadGraphics(){

        passwordDot = gW.getUtil().loadGraphic("sprites/password_dot.png");
        water = gW.getUtil().loadGraphic("sprites/water.png");
        fire = gW.getUtil().loadGraphic("sprites/fire.png");
        grass = gW.getUtil().loadGraphic("sprites/grass.png");
        panel = gW.getUtil().loadGraphic("sprites/win_panel.png");
    }
}
