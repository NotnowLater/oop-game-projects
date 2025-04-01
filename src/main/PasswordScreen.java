package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PasswordScreen implements Processable, Renderable {


    // A1 C1 Level1
    // B2 E4 Level2
    // D3 B5 Level3

    private GameWindow gW;

    private BufferedImage panel;
    private BufferedImage cursor;
    private BufferedImage start;
    private BufferedImage passwordDot;
    private BufferedImage[] numText;

    private int bgAnimId;
    private int bgFrame;

    private int dotLeft = 6;

    private Vector2 cursorPos;

    private boolean[][] passwordGrid = new boolean[5][5];

    private boolean passwordValid = false;

    public PasswordScreen(GameWindow gW){
        this.gW = gW;
        cursorPos = new Vector2();
        loadSprites();
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

        if (gW.getKeyInputHandler().leftJustPressed.isJustPressed()){
            if (cursorPos.getX() > 0){
                cursorPos.setX(cursorPos.getX() - 1);
                gW.getSoundManager().playSFX("cursor");
            }
        } else if (gW.getKeyInputHandler().rightJustPressed.isJustPressed()) {
            if (cursorPos.getX() < 4){
                cursorPos.setX(cursorPos.getX() + 1);
                gW.getSoundManager().playSFX("cursor");
            }
        } else if (gW.getKeyInputHandler().upJustPressed.isJustPressed()){
            if (cursorPos.getY() > 0){
                cursorPos.setY(cursorPos.getY() - 1);
                gW.getSoundManager().playSFX("cursor");
            }
        } else if (gW.getKeyInputHandler().downJustPressed.isJustPressed()) {
            if (cursorPos.getY() < 4){
                cursorPos.setY(cursorPos.getY() + 1);
                gW.getSoundManager().playSFX("cursor");
            }
        }

        if (gW.getKeyInputHandler().cancelJustPressed.isJustPressed()){
            if (!passwordGrid[cursorPos.getY()][cursorPos.getX()]){
                if (dotLeft > 0){
                    dotLeft--;
                    passwordGrid[cursorPos.getY()][cursorPos.getX()] = true;
                }
            } else {
                if (dotLeft < 6){
                    dotLeft++;
                    passwordGrid[cursorPos.getY()][cursorPos.getX()] = false;
                }
            }
        }  else if (gW.getKeyInputHandler().confirmJustPressed.isJustPressed()){
            gW.getSoundManager().stopBGM();
            gW.getSoundManager().playBGM("main");
            gW.changeBGColorTo(new Color(10, 10, 10));
            gW.setCurrentGameState(0);
            resetPasswordGrid();
        }

        if ((passwordGrid[0][0] && passwordGrid[2][0]) || (passwordGrid[1][1] && passwordGrid[4][3]) || (passwordGrid[3][2] && passwordGrid[1][4])){
            if (passwordGrid[1][1] && passwordGrid[4][3]){
                if (passwordGrid[3][2] && passwordGrid[1][4]){
                    gW.changeBGColorTo(gW.getLevelSelect().bg4);
                } else {
                    gW.changeBGColorTo(gW.getLevelSelect().bg3);
                }
            } else {
                if (passwordGrid[3][2] && passwordGrid[1][4]){
                    gW.changeBGColorTo(gW.getLevelSelect().bg1);
                } else {
                    gW.changeBGColorTo(gW.getLevelSelect().bg2);
                }

            }
            passwordValid = true;
        } else {
            gW.changeBGColorTo(new Color(10, 10, 10));
            passwordValid = false;
        }

        if (gW.getKeyInputHandler().menuJustPressed.isJustPressed() && passwordValid){
            if (passwordGrid[3][2] && passwordGrid[1][4]){
                gW.getLevelSelect().levelBeat[2] = true;
            }
            if (passwordGrid[1][1] && passwordGrid[4][3]){
                gW.getLevelSelect().levelBeat[1] = true;
            }
            if (passwordGrid[0][0] && passwordGrid[2][0]){
                gW.getLevelSelect().levelBeat[0] = true;
            }
            gW.getSoundManager().stopBGM();
            gW.getSoundManager().playBGM("select");
            gW.setCurrentGameState(1);
            resetPasswordGrid();
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(gW.getLevelSelect().bgSprites[bgAnimId], 0, 0, 384 * 3, 224 * 3, null);
        g2d.drawImage(panel, 0, 0, 384 * 3, 224 * 3, null);
        if (passwordValid){
            g2d.drawImage(start, 9 * 48, 10 * 48, 48 * 3, 48, null);
        }
        g2d.drawImage(numText[dotLeft], 17 * 48, 4 * 48, 48, 48, null);
        g2d.drawImage(cursor, (8 + cursorPos.getX()) * 48, (5 + cursorPos.getY()) * 48, 48, 48, null);
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                if (passwordGrid[i][j]){
                    g2d.drawImage(passwordDot, (8 + j) * 48, (5 + i) * 48, 48, 48, null);
                }
            }
        }
    }

    public void resetPasswordGrid(){
        dotLeft = 6;
        passwordValid = false;
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                passwordGrid[i][j] = false;
            }
        }
    }

    public void loadSprites(){
        panel = gW.getUtil().loadGraphic("sprites/password_panel.png");
        cursor = gW.getUtil().loadGraphic("sprites/password_cursor2.png");
        start = gW.getUtil().loadGraphic("sprites/password_start.png");
        passwordDot = gW.getUtil().loadGraphic("sprites/password_dot.png");
        numText = gW.getUtil().loadGraphic1D("sprites/password_nums.png", 16);
    }
}
