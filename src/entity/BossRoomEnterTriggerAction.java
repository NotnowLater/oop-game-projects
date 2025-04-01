package entity;

import main.GameWindow;
import main.Vector2;

public class BossRoomEnterTriggerAction implements TriggerAction{

    Vector2 camLockCenterPos;

    public BossRoomEnterTriggerAction(Vector2 camLockCenterPos){
        this.camLockCenterPos = camLockCenterPos;
    }

    @Override
    public void doAction(GameWindow gW) {
        gW.getViewportManager().setViewportPositionFromCenterRelative(new Vector2(camLockCenterPos));
        gW.getViewportManager().setViewportFollowingAnchor(false);
        gW.getSoundManager().stopBGM();
        if (gW.getCurrentLevel().getLevelId() == 3){
            gW.getSoundManager().playBGM("boss2");
        } else {
            gW.getSoundManager().playBGM("boss");
        }
    }
}
