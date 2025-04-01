package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public abstract class Entity {

    private Vector2 worldPosition;
    private Vector2 velocity;
    private GameWindow gW;

    public Entity(GameWindow gW, Vector2 worldPosition){
        this.setgW(gW);
        this.setWorldPosition(worldPosition);
        this.setVelocity(new Vector2(0,0));
    }

    public void process(){

    }

    public void render(Graphics2D g2d){

    }

    public void onHit(Vector2 hitPos){

    }

    public Vector2 getWorldPosition() {
        return worldPosition;
    }

    public void setWorldPosition(Vector2 worldPosition) {
        this.worldPosition = worldPosition;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public GameWindow getgW() {
        return gW;
    }

    public void setgW(GameWindow gW) {
        this.gW = gW;
    }
}
