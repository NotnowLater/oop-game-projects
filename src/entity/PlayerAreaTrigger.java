package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public class PlayerAreaTrigger extends Enemy{

    private TriggerAction actionToDo;
    private Boolean reUseable = false;
    private Boolean playerInTrigger = false;

    public PlayerAreaTrigger(GameWindow gW, Vector2 worldPos, Vector2 size, Boolean reUseable, TriggerAction actionToDo){
        super(gW, worldPos);
        this.actionToDo = actionToDo;
        this.reUseable = reUseable;
        setHitBox(new Rectangle(worldPos.getX(), worldPos.getY(), size.getX(), size.getY()));
    }

    @Override
    public void process() {
        if (getgW().getPlayer().getHitBox().intersects(getHitBox()) && !playerInTrigger){
            // do stuff
//            getgW().getViewportManager().setViewportFollowingAnchor(false);
            actionToDo.doAction(getgW());
            if (!reUseable){
                getgW().getEntitiesToDelete().add(this);
            } else {
                playerInTrigger = true;
            }
        } else if (!getgW().getPlayer().getHitBox().intersects(getHitBox())){
            playerInTrigger = false;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        getgW().getUtil().drawDebugRect(g2d, getHitBox());
    }

    @Override
    void loadSprites(String spritePath) {

    }
}
