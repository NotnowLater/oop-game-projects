package main;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EndScreen implements Processable, Renderable{

    private GameWindow gW;

    private BufferedImage[] endBG;
    private BufferedImage elementMann;
    private BufferedImage gameBy;
    private BufferedImage gameDev;

    private BufferedImage[] allName;


    private BufferedImage[] theEnd;
    private BufferedImage[] thanks;

    private int bgFrame;
    private int bgAnimId;

    private int creditProgress;
    private int creditFrame;
    private int creditFadeId;

    public EndScreen(GameWindow gW){
        this.gW = gW;
        loadSprites();
    }

    @Override
    public void process() {
        if (bgFrame >= 12){
            bgFrame = 0;
            bgAnimId++;
            if (bgAnimId == 3){
                bgAnimId = 0;
            }
        } else {
            bgFrame++;
        }
        if (creditProgress != 8){
            creditFrame++;
        }
        if (creditProgress == 0){
            if (creditFrame >= 120){
                creditFrame = 0;
                creditProgress = 1;
            }
        } else if (creditProgress <= 5){
            if (creditFrame >= 210){
                creditFrame = 0;
                creditProgress ++;
            }
        } else if (creditProgress == 6){
            if (creditFrame >= 45){
                creditFrame = 0;
                creditProgress ++;
            }
        } else if (creditProgress == 7){
            if (creditFrame >= 6){
                creditFrame = 0;
                creditFadeId ++;
                if (creditFadeId == 15){
                    creditProgress = 8;
                }
            }
        }

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(endBG[bgAnimId], 0, 0, 384 * 3, 224 * 3,null);
        if (creditProgress == 0){
            g2d.drawImage(elementMann, 3 * 48, 3 * 48, 192 * 3, 16 * 3,null);
            g2d.drawImage(gameBy, 6 * 48, 5 * 48, 112 * 3, 16 * 3,null);
            g2d.drawImage(gameDev, 3 * 48, 7 * 48, 192 * 3, 16 * 3,null);
        } else if (creditProgress <= 5){
            g2d.drawImage(allName[creditProgress - 1], 1 * 48, 4 * 48, 256 * 3, 48 * 3,null);
        } else {
            g2d.drawImage(theEnd[creditFadeId], 6 * 48, 4 * 48, 111 * 3, 15 * 3,null);
            g2d.drawImage(thanks[creditFadeId], 48, 7 * 48, 286 * 3, 15 * 3,null);
        }
    }

    public void loadSprites(){

        elementMann = gW.getUtil().loadGraphic("sprites/element_mann.png");
        gameBy = gW.getUtil().loadGraphic("sprites/game_by.png");
        gameDev = gW.getUtil().loadGraphic("sprites/gamedev.png");

        allName = gW.getUtil().loadGraphic1D("sprites/allname.png", new Vector2(256, 48));
        theEnd = gW.getUtil().loadGraphic1D("sprites/the_end.png", new Vector2(111, 15));
        thanks = gW.getUtil().loadGraphic1D("sprites/thanks.png", new Vector2(286, 15));
        endBG = gW.getUtil().loadGraphic1D("sprites/endbg.png", new Vector2(384, 224));

    }
}
