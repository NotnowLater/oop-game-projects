package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Util {

    public GameWindow gW;
//    Graphics2D g2d;

    public Util (GameWindow gW){
        this.gW = gW;
    }

//    public void setGraphic2D(Graphics2D g2d){
//        this.g2d = g2d;
//    }


    public Vector2 worldPosToScreenPos(Vector2 worldPos){
        Vector2 vp = gW.getViewportPosition();
//        return new Vector2(vp.getX() - worldPos.getX() , vp.getY() - worldPos.getY());
        return new Vector2( worldPos.getX() - vp.getX() , worldPos.getY() - vp.getY());
    }

    public boolean isWorldPosOnScreen(Vector2 worldPos){
        Vector2 sc = worldPosToScreenPos(worldPos);
        return sc.getX() >= 0 && sc.getX() <= gW.SCREEN_WIDTH && sc.getY() >= 0 && sc.getY() <= gW.SCREEN_HEIGHT;
    }

    public boolean isRectOnScreen(Rectangle rect){
        Vector2 tl = new Vector2(rect.x, rect.y);
        Vector2 tr = new Vector2(rect.x + rect.width, rect.y);
        Vector2 bl = new Vector2(rect.x, rect.y + rect.height);
        Vector2 br = new Vector2(rect.x + rect.width, rect.y + rect.height);
        return isWorldPosOnScreen(tl) && isWorldPosOnScreen(tr) && isWorldPosOnScreen(bl) && isWorldPosOnScreen(br);
    }

    public boolean isRectOnScreenPartial(Rectangle rect){
        Vector2 tl = new Vector2(rect.x, rect.y);
        Vector2 tr = new Vector2(rect.x + rect.width, rect.y);
        Vector2 bl = new Vector2(rect.x, rect.y + rect.height);
        Vector2 br = new Vector2(rect.x + rect.width, rect.y + rect.height);
        return isWorldPosOnScreen(tl) || isWorldPosOnScreen(tr) || isWorldPosOnScreen(bl) || isWorldPosOnScreen(br);
    }

    public boolean isRectNotOnScreenPartial(Rectangle rect){
        Vector2 tl = new Vector2(rect.x, rect.y);
        Vector2 tr = new Vector2(rect.x + rect.width, rect.y);
        Vector2 bl = new Vector2(rect.x, rect.y + rect.height);
        Vector2 br = new Vector2(rect.x + rect.width, rect.y + rect.height);
        return !isWorldPosOnScreen(tl) || !isWorldPosOnScreen(tr) || !isWorldPosOnScreen(bl) || !isWorldPosOnScreen(br);
    }

    public void drawDebugRect(Graphics2D g2d, Rectangle rect){
        if (gW.isDebugEnable()){
//            System.out.println("here");
            g2d.setColor(Color.GREEN);
            Vector2 wp = worldPosToScreenPos(new Vector2(rect.x, rect.y));
            g2d.drawRect(wp.getX(), wp.getY(), rect.width, rect.height);
        }
    }

    public BufferedImage[][] loadGraphic2D(String spritePath, int TileSize){
        try{
            BufferedImage full = ImageIO.read(getClass().getClassLoader().getResourceAsStream(spritePath));
            BufferedImage[][] sprites = new BufferedImage[full.getHeight() / TileSize][full.getWidth() / TileSize];
            for (int i = 0; i < full.getHeight() / TileSize; i++) {
                for (int j = 0; j < full.getWidth() / TileSize; j++) {
                    sprites[i][j] = full.getSubimage(j * TileSize, i * TileSize, TileSize, TileSize);
                }
            }
            return sprites;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage[][] loadGraphic2D(String spritePath, Vector2 TileSize){
        try{
            BufferedImage full = ImageIO.read(getClass().getClassLoader().getResourceAsStream(spritePath));
            BufferedImage[][] sprites = new BufferedImage[full.getHeight() / TileSize.getY()][full.getWidth() / TileSize.getX()];
            for (int i = 0; i < full.getHeight() / TileSize.getY(); i++) {
                for (int j = 0; j < full.getWidth() / TileSize.getX(); j++) {
                    sprites[i][j] = full.getSubimage(j * TileSize.getX(), i * TileSize.getY(), TileSize.getX(), TileSize.getY());
                }
            }
            return sprites;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage[] loadGraphic1D(String spritePath, int TileSize){
        try{
            BufferedImage full = ImageIO.read(getClass().getClassLoader().getResourceAsStream(spritePath));
            BufferedImage[] sprites = new BufferedImage[full.getWidth() / TileSize];
            for (int i = 0; i < full.getWidth() / TileSize; i++) {
                sprites[i] = full.getSubimage(i * TileSize, 0, TileSize, TileSize);
            }
            return sprites;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage[] loadGraphic1D(String spritePath, Vector2 TileSize){
        try{
            BufferedImage full = ImageIO.read(getClass().getClassLoader().getResourceAsStream(spritePath));
            BufferedImage[] sprites = new BufferedImage[full.getWidth() / TileSize.getX()];
            for (int i = 0; i < full.getWidth() / TileSize.getX(); i++) {
                sprites[i] = full.getSubimage(i * TileSize.getX(), 0, TileSize.getX(), TileSize.getY());
            }
            return sprites;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage loadGraphic(String spritePath){
        try{
            BufferedImage full = ImageIO.read(getClass().getClassLoader().getResourceAsStream(spritePath));
            return full;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
