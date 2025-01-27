package entity;

import main.GameWindow;
import main.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Projectile extends Entity{

    public Rectangle hitBox;
    public BufferedImage[][] sprites;
    public Vector2 screenPosition;
    public int facing = 0;

    public Projectile(GameWindow gW, Vector2 worldPosition){
        super(gW, worldPosition);
    }

    public abstract void checkCollisionWithEntity();


}
