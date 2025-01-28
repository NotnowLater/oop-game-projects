package tilemap;

import main.GameWindow;
import main.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TileManager {

    public GameWindow gW;

    public Tile[] tileSet = new Tile[1060];

    public TileMap currentMap;

    public List<Integer> debugX = new ArrayList<Integer>();
    public List<Integer> debugY = new ArrayList<Integer>();

    public TileManager(GameWindow gW){
        this.gW = gW;
        loadTileSet();
        currentMap = new TileMap(460, 51, "tilemapdata/test_map.csv");

    }

    public void loadTileSet(){
        try {
            BufferedImage full = ImageIO.read(getClass().getClassLoader().getResourceAsStream("tilesets/test.png"));
            int id = 0;
            for (int i = 0; i < full.getHeight() / gW.TILE_SIZE; i++){
                for (int j = 0; j < full.getWidth() / gW.TILE_SIZE; j++){
                    tileSet[id] = new Tile(full.getSubimage(j * gW.TILE_SIZE, i * gW.TILE_SIZE, gW.TILE_SIZE, gW.TILE_SIZE), false);
                    id++;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void render(Graphics2D g2d){
//        for (int i = 0; i < currentMap.height; i++){
//            for (int j = 0; j < currentMap.width; j++){
//                int tileWorldX = j * gW.RENDER_TILE_SIZE;
//                int tileWorldY = i * gW.RENDER_TILE_SIZE;
//                Vector2 screen = gW.util.worldPosToScreenPos(new Vector2(tileWorldX, tileWorldY));
//
//                // check if current tile is within the game screen to improve game performance
//                if ((screen.x >= -gW.RENDER_TILE_SIZE && screen.x <= gW.SCREEN_WIDTH &&
//                        screen.y >= -gW.RENDER_TILE_SIZE && screen.y <= gW.SCREEN_HEIGHT)){
//                    g2d.drawImage(tileSet[currentMap.tileData[i][j]].tileImage, screen.x, screen.y, gW.RENDER_TILE_SIZE, gW.RENDER_TILE_SIZE, null);
//                }
//            }
//        }
        Vector2 start = new Vector2(Math.abs(gW.viewportPosition.x / gW.RENDER_TILE_SIZE), Math.abs(gW.viewportPosition.y / gW.RENDER_TILE_SIZE));
        for (int i = start.y; i < start.y + gW.SCREEN_TILE_HEIGHT + 1; i++){
            for (int j = start.x; j < start.x + gW.SCREEN_TILE_WIDTH + 1; j++){
                int tileWorldX = j * gW.RENDER_TILE_SIZE;
                int tileWorldY = i * gW.RENDER_TILE_SIZE;
                if ((j >= 0 && j < currentMap.width) && (i >= 0 && i < currentMap.height)){
                    Vector2 screen = gW.util.worldPosToScreenPos(new Vector2(tileWorldX, tileWorldY));
                    g2d.drawImage(tileSet[currentMap.tileData[i][j]].tileImage, screen.x, screen.y, gW.RENDER_TILE_SIZE, gW.RENDER_TILE_SIZE, null);
                }
            }
        }
        // debug
        for (int i = 0; i < debugX.size(); i++){
            Vector2 sPos = gW.util.worldPosToScreenPos(new Vector2(debugX.get(i), debugY.get(i)));
            g2d.setColor(Color.green);
            g2d.drawRect(sPos.x, sPos.y, gW.RENDER_TILE_SIZE, gW.RENDER_TILE_SIZE);
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

        debugX.add(tileX * gW.RENDER_TILE_SIZE);
        debugY.add(tileY * gW.RENDER_TILE_SIZE);

        int tile = currentMap.tileData[tileY][tileX];
//        return tile != 9 && tile != 10;
        return tile < 200 || (tile >= 840 && tile < 1020);
    }



}
