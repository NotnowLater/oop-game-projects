package main;

public class ViewportManager implements Processable{

    private GameWindow gW;
    private Vector2 viewportFollowCenterAnchor;
    private Vector2 viewportCurrentCenter;

    private boolean isViewportFollowingAnchor;

    final int viewportFollowSpeed = 8;

    public ViewportManager(GameWindow gW){
        this.gW = gW;
        setViewportCurrentCenter(new Vector2());
        setViewportFollowCenterAnchor(new Vector2());
        isViewportFollowingAnchor = true;
    }

    public void setViewportPositionTo(Vector2 newPos){
        gW.setViewportPosition(newPos);
    }

    public void setViewportPositionFromCenterRelative(Vector2 centerPos){
//        gW.setViewportPosition(new Vector2((gW.SCREEN_WIDTH / 2) - centerPos.getX(), (gW.SCREEN_HEIGHT / 2)  - centerPos.getY()));
        gW.setViewportPosition(new Vector2(centerPos.getX() - (gW.SCREEN_WIDTH / 2), centerPos.getY() - (gW.SCREEN_HEIGHT / 2)));
    }

    @Override
    public void process() {
        // calculate viewport center
        setViewportCurrentCenter(calculateViewportCenter());
        if (isViewportFollowingAnchor()){
            // if the viewport center is near the followCenterAnchor just set it to the anchor
            if (getViewportCurrentCenter().distanceTo(getViewportFollowCenterAnchor()) <= 720) {
                setViewportPositionFromCenterRelative(getViewportFollowCenterAnchor());
            }
            // else, slowly move the viewport to anchor
            // never mind, this code sucks...
            else {
                Vector2 dir = new Vector2();
                if (getViewportCurrentCenter().getX() < getViewportFollowCenterAnchor().getX()){
                    dir.setX(1);
                } else {
                    dir.setX(-1);
                }
                if (getViewportCurrentCenter().getY() < getViewportFollowCenterAnchor().getY()){
                    dir.setY(1);
                } else {
                    dir.setY(-1);
                }
                Vector2 done = new Vector2(getViewportCurrentCenter().getX() + (getViewportFollowSpeed() * dir.getX()), getViewportCurrentCenter().getY() + (getViewportFollowSpeed() * dir.getY()));
                setViewportPositionFromCenterRelative(done);
            }
        }
    }

    public Vector2 calculateViewportCenter(){
        Vector2 tl = gW.getViewportPosition();
        return new Vector2(tl.getX() + (gW.SCREEN_WIDTH / 2), tl.getY() + (gW.SCREEN_HEIGHT / 2));
    }

    public void setViewportFollowCenterAnchor(Vector2 viewportFollowCenterAnchor) {
        this.viewportFollowCenterAnchor = viewportFollowCenterAnchor;
    }

    public Vector2 getViewportFollowCenterAnchor() {
        return viewportFollowCenterAnchor;
    }

    public boolean isViewportFollowingAnchor() {
        return isViewportFollowingAnchor;
    }

    public void setViewportFollowingAnchor(boolean viewportFollowingAnchor) {
        isViewportFollowingAnchor = viewportFollowingAnchor;
    }

    public int getViewportFollowSpeed() {
        return viewportFollowSpeed;
    }

    public Vector2 getViewportCurrentCenter() {
        return viewportCurrentCenter;
    }

    public void setViewportCurrentCenter(Vector2 viewportCurrentCenter) {
        this.viewportCurrentCenter = viewportCurrentCenter;
    }
}
