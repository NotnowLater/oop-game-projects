package entity;

import main.GameWindow;
import main.Vector2;

import java.awt.*;

public abstract class Entity {

    public Vector2 worldPosition;
    public Vector2 velocity;
    public GameWindow gW;

    public Entity(GameWindow gW, Vector2 worldPosition){
        this.gW = gW;
        this.worldPosition = worldPosition;
        this.velocity = new Vector2(0,0);
    }

    public void process(){

    }

    public void render(Graphics2D g2d){

    }

    public void onHit(Vector2 hitPos){

    }


}
