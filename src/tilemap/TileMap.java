package tilemap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class TileMap {

    private int[][] tileData;
    private int width;
    private int height;

    public TileMap(int width, int height, String dataPath){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(dataPath)))){
            this.setWidth(width);
            this.setHeight(height);
            setTileData(new int[height][width]);
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null){
                int[] toAdd = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
                getTileData()[lineCount] = toAdd.clone();
                lineCount++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int[][] getTileData() {
        return tileData;
    }

    public void setTileData(int[][] tileData) {
        this.tileData = tileData;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
