package entity;

import main.GameWindow;
import main.Vector2;

public class ChangePlayerRespawnTriggerAction implements TriggerAction{

    Vector2 respawn;

    public ChangePlayerRespawnTriggerAction(Vector2 respawn){
        this.respawn = respawn;
    }

    @Override
    public void doAction(GameWindow gW) {
        gW.getPlayer().setRespawnPos(new Vector2(respawn));
    }
}
