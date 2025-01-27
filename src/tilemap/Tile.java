package tilemap;

import java.awt.image.BufferedImage;

public class Tile {

    public BufferedImage tileImage;
    public boolean collidable;

    public Tile(BufferedImage tileImage, boolean collidable){
        this.tileImage = tileImage;
        this.collidable = collidable;
    }

}
