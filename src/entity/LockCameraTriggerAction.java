package entity;

import main.GameWindow;

public class LockCameraTriggerAction implements TriggerAction{
    @Override
    public void doAction(GameWindow gW) {
        gW.getViewportManager().setViewportFollowingAnchor(false);
    }
}
