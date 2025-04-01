package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HUDManager {

    GameWindow gW;
    private BufferedImage normalBar[] = new BufferedImage[8];
    private BufferedImage waterBar[] = new BufferedImage[8];
    private BufferedImage fireBar[] = new BufferedImage[8];
    private BufferedImage grassBar[] = new BufferedImage[8];
    private BufferedImage greyedBar;

    private int menuCursorPos = 0;

    BufferedImage panel;
    BufferedImage iconCursor;
    BufferedImage normalIcon;
    BufferedImage waterIcon;
    BufferedImage fireIcon;
    BufferedImage grassIcon;

    BufferedImage waterIconGrey;
    BufferedImage fireIconGrey;
    BufferedImage grassIconGrey;

    boolean menuSelected = false;

    private int waterId;
    private int fireId;
    private int grassId;


    public HUDManager(GameWindow gW){
        this.gW = gW;

        greyedBar = gW.getUtil().loadGraphic("sprites/grey_bar.png");
        for (int i = 0; i < 8; i++){
            normalBar[i] = gW.getUtil().loadGraphic("sprites/normal_bar/normal_bar" + (i + 1) + ".png");
            waterBar[i] = gW.getUtil().loadGraphic("sprites/water_bar/water_bar" + (i + 1) + ".png");
            fireBar[i] = gW.getUtil().loadGraphic("sprites/fire_bar/fire_bar" + (i + 1) + ".png");
            grassBar[i] = gW.getUtil().loadGraphic("sprites/grass_bar/grass_bar" + (i + 1) + ".png");
        }
        panel = gW.getUtil().loadGraphic("sprites/skill_panel.png");
        iconCursor = gW.getUtil().loadGraphic("sprites/skill_selector.png");
        normalIcon = gW.getUtil().loadGraphic("sprites/skill1.png");
        waterIcon = gW.getUtil().loadGraphic("sprites/skill2.png");
        fireIcon = gW.getUtil().loadGraphic("sprites/skill3.png");
        grassIcon = gW.getUtil().loadGraphic("sprites/skill4.png");

        waterIconGrey = gW.getUtil().loadGraphic("sprites/skill2g.png");
        fireIconGrey = gW.getUtil().loadGraphic("sprites/skill3g.png");
        grassIconGrey = gW.getUtil().loadGraphic("sprites/skill4g.png");
    }

    public void process(){
        if (gW.getCurrentGameState() == 3){
            if (gW.getKeyInputHandler().quitJustPressed.isJustPressed()){
                gW.setCurrentGameState(1);
                switch (gW.getCurrentLevel().getLevelId()){
                    case 0:
                        gW.setLevel1(new Level1(gW, "water"));
                        break;
                    case 1:
                        gW.setLevel2(new Level2(gW, "fire"));
                        break;
                    case 2:
                        gW.setLevel3(new Level3(gW, "grass"));
                        break;
                    case 3:
                        gW.setLevel4(new Level4(gW, "dark"));
                        break;
                }
                gW.getSoundManager().stopBGM();
                gW.getSoundManager().playBGM("select");
                menuSelected = false;
                gW.setCurrentLevel(null);
            } else if (gW.getKeyInputHandler().menuJustPressed.isJustPressed() && menuSelected){
                if (menuCursorPos == 0 || (gW.getLevelSelect().levelBeat[0] && menuCursorPos == 1) || (gW.getLevelSelect().levelBeat[1] && menuCursorPos == 2) || (gW.getLevelSelect().levelBeat[2] && menuCursorPos == 3)){
                    gW.setCurrentGameState(2);
                    gW.getPlayer().setCurrentElement(menuCursorPos);
                    menuSelected = false;
                    gW.getSoundManager().playSFX("cursor");
                } else {
                    gW.getSoundManager().playSFX("error");
                }
            } else if (!menuSelected){
                    menuSelected = true;
                    menuCursorPos = gW.getPlayer().getCurrentElement();
            }
            if (gW.getKeyInputHandler().downJustPressed.isJustPressed()){
                if (menuCursorPos < 3){
                    menuCursorPos++;
                    gW.getSoundManager().playSFX("cursor");
                }
            }
            else if (gW.getKeyInputHandler().upJustPressed.isJustPressed()){
                if (menuCursorPos > 0){
                    menuCursorPos--;
                    gW.getSoundManager().playSFX("cursor");
                }
            }
            }


        }

    public void render(Graphics2D g2d){

        waterId = (int)Math.ceil((float)gW.getPlayer().getWaterElementEnergy()/24 * 8) - 1;
        fireId = (int)Math.ceil((float)gW.getPlayer().getFireElementEnergy()/24 * 8) - 1;
        grassId = (int)Math.ceil((float)gW.getPlayer().getGrassElementEnergy()/24 * 8) - 1;
        renderHpBar(gW.getPlayer().getHp() - 1, g2d);
        if (gW.getCurrentGameState() == 3){
            renderMenu(g2d);
        }
    }

    public void renderHpBar(int Hp, Graphics2D g2d){
        if (Hp >= 0){
            g2d.drawImage(normalBar[Hp], 72, 72, 144, 48, null);
            if (gW.getPlayer().getCurrentElement() == 1 && waterId >= 0){
                g2d.drawImage(waterBar[waterId], 72, 144, 144, 48, null);
            }else if (gW.getPlayer().getCurrentElement() == 2 && fireId >= 0){
                g2d.drawImage(fireBar[fireId], 72, 144, 144, 48, null);
            }else if (gW.getPlayer().getCurrentElement() == 3 && grassId >= 0){
                g2d.drawImage(grassBar[grassId], 72, 144, 144, 48, null);
            }
        }

    }

    public void renderMenu(Graphics2D g2d){
        g2d.drawImage(panel, 16 * 48, 1 * 48, 112 * 3, 144 * 3, null);

        g2d.drawImage(normalIcon, 17 * 48, 2 * 48, 48, 48, null);
        if (gW.getLevelSelect().levelBeat[0]){
            g2d.drawImage(waterIcon, 17 * 48, 4 * 48, 48, 48, null);
        } else {
            g2d.drawImage(waterIconGrey, 17 * 48, 4 * 48, 48, 48, null);
        }
        if (gW.getLevelSelect().levelBeat[1]) {
            g2d.drawImage(fireIcon, 17 * 48, 6 * 48, 48, 48, null);
        } else {
            g2d.drawImage(fireIconGrey, 17 * 48, 6 * 48, 48, 48, null);
        }
        if (gW.getLevelSelect().levelBeat[2]){
            g2d.drawImage(grassIcon, 17 * 48, 8 * 48, 48, 48, null);
        } else {
            g2d.drawImage(grassIconGrey, 17 * 48, 8 * 48, 48, 48, null);
        }

        g2d.drawImage(normalBar[7], 19 * 48, 2 * 48, 144, 48, null);
        if (gW.getLevelSelect().levelBeat[0]) {
            if (waterId >= 0){
                g2d.drawImage(waterBar[waterId], 19 * 48, 4 * 48, 144, 48, null);
            }
        } else {
            g2d.drawImage(greyedBar, 19 * 48, 4 * 48, 144, 48, null);
        }
        if (gW.getLevelSelect().levelBeat[1]) {
            if (fireId >= 0){
                g2d.drawImage(fireBar[fireId], 19 * 48, 6 * 48, 144, 48, null);
            }
        } else {
            g2d.drawImage(greyedBar, 19 * 48, 6 * 48, 144, 48, null);
        }
        if (gW.getLevelSelect().levelBeat[2]) {
            if (grassId >= 0){
                g2d.drawImage(grassBar[grassId], 19 * 48, 8 * 48, 144, 48, null);
            }
        } else {
            g2d.drawImage(greyedBar, 19 * 48, 8 * 48, 144, 48, null);
        }
        g2d.drawImage(iconCursor, 17 * 48, (2 + (menuCursorPos * 2)) * 48, 48, 48, null);
    }



}
