package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainMenu implements Processable, Renderable{

    private GameWindow gW;

    BufferedImage title;
    BufferedImage titleBG;
    BufferedImage cursor;
    BufferedImage startText;
    BufferedImage passwordText;

    int titleYPos = 48;
    int titleMoveFrame = 0;

    int cursorPosY;

    public MainMenu(GameWindow gW){
        this.gW = gW;
        loadSprites();
    }

    @Override
    public void process() {
        if (gW.getKeyInputHandler().cancel){
            if (cursorPosY == 0){
                gW.getSoundManager().stopBGM();
                gW.getSoundManager().playBGM("select");
                gW.setCurrentGameState(1);
            } else if (cursorPosY == 1){
                gW.getSoundManager().stopBGM();
                gW.getSoundManager().playBGM("password");
                gW.setCurrentGameState(5);
            }
        }

        if (gW.getKeyInputHandler().upJustPressed.isJustPressed() && cursorPosY != 0){
            cursorPosY = 0;
            gW.getSoundManager().playSFX("cursor");
        } else if (gW.getKeyInputHandler().downJustPressed.isJustPressed() && cursorPosY != 1) {
            cursorPosY = 1;
            gW.getSoundManager().playSFX("cursor");
        }

        if (titleMoveFrame >= 21){
            titleMoveFrame = 0;
            if (titleYPos == 48){
                titleYPos = 52;
            } else {
                titleYPos = 48;
            }
        } else {
            titleMoveFrame++;
        }


    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(titleBG, 0, 0, 384 * 3, 224 * 3, null);
        g2d.drawImage(title, (16 * 5) * 3, titleYPos, 224 * 3, 80 * 3, null);
        g2d.drawImage(cursor, (16 * 6) * 3, 16 * 3 * (7 + (cursorPosY * 2)), 16 * 3, 16 * 3, null);
        g2d.drawImage(startText, (16 * 8) * 3, 7 * 48, 80 * 3, 16 * 3, null);
        g2d.drawImage(passwordText, (16 * 8) * 3, 9 * 48, 48 * 8, 16 * 3, null);
    }

    public void loadSprites(){
        try{
            title = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/title.png"));
            titleBG = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/titlebg.png"));
            cursor = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/cursor.png"));
            startText = ImageIO.read(getClass().getClassLoader().getResourceAsStream("sprites/start_text.png"));
            passwordText = gW.getUtil().loadGraphic("sprites/password_text.png");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
