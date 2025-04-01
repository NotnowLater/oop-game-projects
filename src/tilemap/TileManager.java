package tilemap;

import main.GameWindow;
import main.Processable;
import main.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TileManager implements Processable {

    private GameWindow gW;

    private int animFrame;
    private int animTileId;

    private ArrayList<Tile[]> tileSets = new ArrayList<Tile[]>();

//    private Tile[] tileSet = new Tile[80];

    private TileMap currentMap;

    private List<Integer> debugX = new ArrayList<Integer>();
    private List<Integer> debugY = new ArrayList<Integer>();

    public TileManager(GameWindow gW, String tileDataFile, Vector2 mapSize){
        this.gW = gW;
        addTileSet("tilesets/" + tileDataFile + "1.png");
        addTileSet("tilesets/" + tileDataFile + "2.png");
        addTileSet("tilesets/" + tileDataFile + "3.png");
        currentMap = new TileMap(mapSize.getX(), mapSize.getY(), "tilemapdata/" + tileDataFile + ".csv");
    }

    public void addTileSet(String fileName){
        try {
            BufferedImage full = ImageIO.read(getClass().getClassLoader().getResourceAsStream(fileName));
            Tile[] tileSet = new Tile[80];
            int id = 0;
            for (int i = 0; i < full.getHeight() / gW.TILE_SIZE; i++){
                for (int j = 0; j < full.getWidth() / gW.TILE_SIZE; j++){
                    tileSet[id] = new Tile(full.getSubimage(j * gW.TILE_SIZE, i * gW.TILE_SIZE, gW.TILE_SIZE, gW.TILE_SIZE));
                    id++;
                }
            }
            getTileSets().add(tileSet);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void process() {
        // animate the tileSet
        if (getAnimFrame() >= 12){
            setAnimFrame(0);
            setAnimTileId(getAnimTileId() + 1);
            if (getAnimTileId() >= getTileSets().size()){
                setAnimTileId(0);
            }
        } else {
            setAnimFrame(getAnimFrame() + 1);
        }
    }

    public void render(Graphics2D g2d){
        Vector2 start = new Vector2(Math.abs(gW.getViewportPosition().getX() / gW.RENDER_TILE_SIZE), Math.abs(gW.getViewportPosition().getY() / gW.RENDER_TILE_SIZE));
        for (int i = start.getY(); i < start.getY() + gW.SCREEN_TILE_HEIGHT + 1; i++){
            for (int j = start.getX(); j < start.getX() + gW.SCREEN_TILE_WIDTH + 1; j++){
                int tileWorldX = j * gW.RENDER_TILE_SIZE;
                int tileWorldY = i * gW.RENDER_TILE_SIZE;
                if ((j >= 0 && j < currentMap.getWidth()) && (i >= 0 && i < currentMap.getHeight())){
                    Vector2 screen = gW.getUtil().worldPosToScreenPos(new Vector2(tileWorldX, tileWorldY));
                    g2d.drawImage(tileSets.get(getAnimTileId())[currentMap.getTileData()[i][j]].tileImage, screen.getX(), screen.getY(), gW.RENDER_TILE_SIZE, gW.RENDER_TILE_SIZE, null);
                }
            }
        }
        // debug rect
        for (int i = 0; i < debugX.size(); i++){
            Vector2 sPos = gW.getUtil().worldPosToScreenPos(new Vector2(debugX.get(i), debugY.get(i)));
            g2d.setColor(Color.green);
            g2d.drawRect(sPos.getX(), sPos.getY(), gW.RENDER_TILE_SIZE, gW.RENDER_TILE_SIZE);
        }
        debugX.clear();
        debugY.clear();
    }

    public boolean checkRectNotIntersectAnyTile(Rectangle rect){
        return !isTileBlocking(rect.x, rect.y) && !isTileBlocking(rect.x + rect.width, rect.y) &&
                !isTileBlocking(rect.x, rect.y + rect.height) && !isTileBlocking(rect.x + rect.width, rect.y + rect.height);
    }

    public boolean checkRectNotIntersectAnyTile(int x, int y, int width, int height){
        return !isTileBlocking(x, y) &&! isTileBlocking(x + width, y) &&
                !isTileBlocking(x, y + height) && !isTileBlocking(x + width, y + height);
    }

    public boolean isTileBlocking(int x, int y){
        int tileX = x / gW.RENDER_TILE_SIZE;
        int tileY = y / gW.RENDER_TILE_SIZE;

        if (gW.isDebugEnable()){
            debugX.add(tileX * gW.RENDER_TILE_SIZE);
            debugY.add(tileY * gW.RENDER_TILE_SIZE);
        }

        int tile = currentMap.getTileData()[tileY][tileX];
//        return tile != 9 && tile != 10;
        return tile < 16 && tile != 13;
    }

    public boolean isTileDangerous(int x, int y){
        int tileX = x / gW.RENDER_TILE_SIZE;
        int tileY = y / gW.RENDER_TILE_SIZE;

        if (gW.isDebugEnable()){
            debugX.add(tileX * gW.RENDER_TILE_SIZE);
            debugY.add(tileY * gW.RENDER_TILE_SIZE);
        }

        int tile = currentMap.getTileData()[tileY][tileX];
//        return tile != 9 && tile != 10;
        if (gW.getCurrentLevel().getLevelId() == 2){
            return tile == 13 || tile == 22 || tile == 74 || tile == 75;
        } else {
            return tile == 13 || tile == 22;
        }
    }


    public int getAnimFrame() {
        return animFrame;
    }

    public void setAnimFrame(int animFrame) {
        this.animFrame = animFrame;
    }

    public int getAnimTileId() {
        return animTileId;
    }

    public void setAnimTileId(int animTileId) {
        this.animTileId = animTileId;
    }

    public ArrayList<Tile[]> getTileSets() {
        return tileSets;
    }

    public void setTileSets(ArrayList<Tile[]> tileSets) {
        this.tileSets = tileSets;
    }
}
