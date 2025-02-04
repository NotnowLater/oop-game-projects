package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public class PlayerAreaCameraAxisLockTrigger extends Enemy{

    public PlayerAreaCameraAxisLockTrigger(GameWindow gW, Vector2 worldPos, Vector2 size){
        super(gW, worldPos);
        setHitBox(new Rectangle(worldPos.getX(), worldPos.getY(), size.getX(), size.getY()));
    }

    @Override
    public void process() {
        if (getgW().player.getHitBox().intersects(getHitBox())){
            // do stuff
            getgW().getViewportManager().setViewportFollowingAnchor(false);
            getgW().entitiesToDelete.add(this);
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        getgW().util.drawDebugRect(g2d, getHitBox());
    }

    @Override
    void loadSprites(String spritePath) {

    }
}
