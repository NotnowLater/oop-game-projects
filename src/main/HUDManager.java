package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUDManager {

    GameWindow gW;
    private BufferedImage[] HpBar;

    public HUDManager(GameWindow gW){
        this.gW = gW;
        HpBar = gW.util.loadGraphic1D("sprites/hp_bar.png", 16);
    }

    public void process(){

    }

    public void render(Graphics2D g2d){
        renderHpBar(gW.player.getHp(), g2d);
    }

    public void renderHpBar(int Hp, Graphics2D g2d){
        for (int i = 0; i < Hp; i++){
            g2d.drawImage(HpBar[i], 72 + (i * 48), 72, 48, 48, null);
        }
    }


}
